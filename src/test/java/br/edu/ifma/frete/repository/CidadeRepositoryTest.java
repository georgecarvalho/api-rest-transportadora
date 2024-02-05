package br.edu.ifma.frete.repository;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.edu.ifma.frete.model.Cidade;
import br.edu.ifma.frete.model.UnidadeFederacao;
import jakarta.validation.ConstraintViolationException;

@DataJpaTest
public class CidadeRepositoryTest {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	private Cidade cidade;
	
	@BeforeEach
	void init() {
		cidade = Cidade.builder()
				.nome("Sao Luis")
				.uf(UnidadeFederacao.MARANHAO)
				.taxa(new BigDecimal("100"))
				.build();
	}
	
	@Test
	void deveBuscarCidadePeloNome() {
		cidadeRepository.save(cidade);
		
		Cidade cidadeDB = cidadeRepository.findByNome("Sao Luis");
		Assertions.assertNotNull(cidadeDB);
	}
	
	@Test
	void deveLancarExcecaoCasoNomeSejaNulo() {
		cidade.setNome(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> cidadeRepository.save(cidade),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Nome deve ser preenchido"));
	}
	
	@Test
	void deveLancarExcecaoCasoUfSejaNulo() {
		cidade.setUf(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> cidadeRepository.save(cidade),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("UF deve ser preenchido"));
	}
	
	@Test
	void deveLancarExcecaoCasoTaxaSejaNulo() {
		cidade.setTaxa(null);
		
		ConstraintViolationException exception = 
				Assertions.assertThrows(ConstraintViolationException.class, 
						() -> cidadeRepository.save(cidade),
						"Deve lançar uma ConstraintViolationException");
		
		Assertions.assertTrue(exception.getMessage().contains("Taxa deve ser preenchido"));
	}
}
