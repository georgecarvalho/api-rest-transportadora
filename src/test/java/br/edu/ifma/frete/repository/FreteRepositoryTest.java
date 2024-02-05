package br.edu.ifma.frete.repository;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.edu.ifma.frete.model.Cidade;
import br.edu.ifma.frete.model.Cliente;
import br.edu.ifma.frete.model.Frete;
import br.edu.ifma.frete.model.UnidadeFederacao;
import jakarta.validation.ConstraintViolationException;

@DataJpaTest
public class FreteRepositoryTest {
	
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
				.valor(new BigDecimal("1000"))
				.build();
	}
	
	@Test
	void deveBuscarFretePeloValor() {
		freteRepository.save(frete1);
		
		Frete freteDB = freteRepository.findByValor(new BigDecimal("1000"));
		Assertions.assertNotNull(freteDB);
	}
	
	@Test
	void deveBuscarFretesPorClienteOrdenadosPorValorDecrescente() {
		frete2 = Frete.builder()
				.cidade(Cidade.builder()
						.nome("Teresina")
						.uf(UnidadeFederacao.PIAUI)
						.taxa(new BigDecimal("120"))
						.build())
				.descricao("Frete 2")
				.peso(600.00)
				.valor(new BigDecimal("1200"))
				.build();
		frete2.setCliente(frete1.getCliente());
		
		freteRepository.save(frete1);
		freteRepository.save(frete2);
		
		List<Frete> freteDB = freteRepository.findByClienteOrderByValorDesc(frete1.getCliente());
		
		Assertions.assertNotNull(freteDB);
		Assertions.assertEquals(2, 
				freteDB.size(),
				"A lista deve conter 2 elementos");
		Assertions.assertTrue(freteDB.get(0).getValor() == frete2.getValor(),
				"O Frete 2 deve ser o primeiro da lista");
	}
	
	@Test
	void deveLancarExcecaoCasoDescricaoSejaNulo() {
		frete1.setDescricao(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> freteRepository.save(frete1),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Descricao deve ser preenchido"));
	}
	
	@Test
	void deveLancarExcecaoCasoPesoSejaNulo() {
		frete1.setPeso(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> freteRepository.save(frete1),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Peso deve ser preenchido"));
	}
	
	@Test
	void deveLancarExcecaoCasoCidadeSejaNulo() {
		frete1.setCidade(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> freteRepository.save(frete1),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Cidade não pode ser nulo"));
	}
	
	@Test
	void deveLancarExcecaoCasoClienteSejaNulo() {
		frete1.setCliente(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> freteRepository.save(frete1),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Cliente não pode ser nulo"));
	}
}