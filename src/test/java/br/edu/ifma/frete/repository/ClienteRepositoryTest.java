package br.edu.ifma.frete.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.edu.ifma.frete.model.Cliente;
import jakarta.validation.ConstraintViolationException;

@DataJpaTest
public class ClienteRepositoryTest {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	private Cliente cliente;
	
	@BeforeEach
	void init() {
		cliente = Cliente.builder()
				.nome("João")
				.endereco("Rua dos Bobos, Numero Zero")
				.telefone("99999999999")
				.build();
	}
	
	@Test
	void deveBuscarClientePeloTelefone() {
		clienteRepository.save(cliente);
		
		Cliente clienteDB = clienteRepository.findByTelefone("99999999999");
		Assertions.assertNotNull(clienteDB);
	}
	
	@Test
	void deveLancarExcecaoCasoNomeSejaNulo() {
		cliente.setNome(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> clienteRepository.save(cliente),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Nome deve ser preenchido"));
	}
	
	@Test
	void deveLancarExcecaoCasoEnderecoSejaNulo() {
		cliente.setEndereco(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> clienteRepository.save(cliente),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Endereco deve ser preenchido"));
	}
	
	@Test
	void deveLancarExcecaoCasoTelefoneSejaNulo() {
		cliente.setTelefone(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> clienteRepository.save(cliente),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Telefone deve ser preenchido"));
	}
}