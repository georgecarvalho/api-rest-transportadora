package br.edu.ifma.frete.service;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.ifma.frete.exception.FreteException;
import br.edu.ifma.frete.model.Cidade;
import br.edu.ifma.frete.model.Cliente;
import br.edu.ifma.frete.model.Frete;
import br.edu.ifma.frete.model.UnidadeFederacao;
import br.edu.ifma.frete.repository.FreteRepository;

@SpringBootTest
public class FreteServiceTest {

	@Autowired
	private FreteService freteService;
	
	@Autowired
	private FreteRepository freteRepository;
	
	private Frete frete1;
	private Frete frete2;
	
	@BeforeEach
	void init() {
		frete1 = Frete.builder()
				.cidade(Cidade.builder()
						.nome("Sao Luis")
						.uf(UnidadeFederacao.MARANHAO)
						.taxa(new BigDecimal("100"))
						.build())
				.cliente(Cliente.builder()
						.nome("João")
						.endereco("Rua dos Bobos, Numero Zero")
						.telefone("99999999999")
						.build())
				.descricao("Frete 1")
				.peso(500.00)
				.build();
	}
	
	@AfterEach
	void end() {
		freteRepository.deleteAll();
	}
	
	@Test
	void deveCadastrarNovoFrete() throws FreteException {
		freteService.inserirOuAlterar(frete1);
		List<Frete> fretes = freteRepository.findAll();
		
		Assertions.assertEquals(1, 
				fretes.size(),
				"Lista deve conter 1 elemento");
	}
	
	@Test
	void deveRemoverFreteCadastrado() throws FreteException {
		freteService.inserirOuAlterar(frete1);
		List<Frete> fretes = freteRepository.findAll();
		
		Assertions.assertEquals(1, 
				fretes.size(),
				"Lista deve conter 1 elemento");
		
		freteService.remover(frete1.getId());
		List<Frete> resultado = freteRepository.findAll();
		
		Assertions.assertEquals(0, 
				resultado.size(),
				"Lista não deve conter nenhum elemento");
	}
	
	@Test
	void deveLancarExcecaoCasoCidadeSejaNulo() {
		frete1.setCidade(null);
		
		FreteException exception = 
				Assertions.assertThrows(FreteException.class, 
						() -> freteService.inserirOuAlterar(frete1),
						"Deve lançar uma FreteException");
		
		Assertions.assertTrue(exception.getMessage().contains("Cidade não pode ser nulo"));
	}
	
	@Test
	void deveLancarExcecaoCasoClienteSejaNulo() {
		frete1.setCliente(null);
		
		FreteException exception = 
				Assertions.assertThrows(FreteException.class, 
						() -> freteService.inserirOuAlterar(frete1),
						"Deve lançar uma FreteException");
		
		Assertions.assertTrue(exception.getMessage().contains("Cliente não pode ser nulo"));
	}
	
	@Test
	void deveRecuperarValorDoFrete() throws FreteException {
		freteService.inserirOuAlterar(frete1);
		
		BigDecimal valorFrete = freteService.recuperaValorFrete(frete1);
		Assertions.assertEquals(new BigDecimal("5100").setScale(2), valorFrete);
	}
	
	@Test
	void deveRecuperarFreteDeMaiorValor() throws FreteException {
		frete1.setValor(new BigDecimal("1000"));
		
		frete2 = Frete.builder()
				.cidade(Cidade.builder()
						.nome("Teresina")
						.uf(UnidadeFederacao.PIAUI)
						.taxa(new BigDecimal("120"))
						.build())
				.cliente(Cliente.builder()
						.nome("Pedro")
						.endereco("Rua dos Bobos, Numero Zero")
						.telefone("88888888888")
						.build())
				.descricao("Frete 2")
				.peso(600.00)
				.valor(new BigDecimal("1200"))
				.build();
		
		freteService.inserirOuAlterar(frete1);
		freteService.inserirOuAlterar(frete2);
		
		Frete freteDB = freteService.recuperaFreteDeMaiorValor();
		
		Assertions.assertEquals(new BigDecimal("1200").setScale(2), freteDB.getValor());
	}
}
