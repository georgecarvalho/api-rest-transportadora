package br.edu.ifma.frete.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifma.frete.exception.FreteException;
import br.edu.ifma.frete.model.Frete;
import br.edu.ifma.frete.repository.FreteRepository;
import jakarta.validation.ConstraintViolationException;

@Service
public class FreteService {
	
	private final BigDecimal VALOR_FIXO = new BigDecimal("10");
	
	@Autowired
	private FreteRepository freteRepository;
	
	public Frete inserirOuAlterar(Frete frete) throws FreteException {
		try {
			return freteRepository.save(frete);
		} catch (ConstraintViolationException e) {
			throw new FreteException(e);
		}
	}
	
	public List<Frete> buscarTodos() {
		return freteRepository.findAll();
	}
	
	public Optional<Frete> buscarPor(Long id) {
		return freteRepository.findById(id);
	}
	
	public void remover(Long id) {
		freteRepository.deleteById(id);
	}
	
	public boolean naoExisteFreteCom(Long id) {
		return !freteRepository.existsById(id);
	}
	
	public Frete recuperaFreteDeMaiorValor() {
		return this.freteRepository.findFirstByOrderByValorDesc();
	}
	
	public BigDecimal recuperaValorFrete(Frete frete) {
		return VALOR_FIXO.multiply(BigDecimal.valueOf(frete.getPeso()))
				.add(frete.taxaCidade()).setScale(2);
	}
}
