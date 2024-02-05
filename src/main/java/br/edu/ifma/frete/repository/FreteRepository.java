package br.edu.ifma.frete.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifma.frete.model.Cliente;
import br.edu.ifma.frete.model.Frete;

@Repository
public interface FreteRepository extends JpaRepository<Frete, Long> {
	Frete findByValor(BigDecimal valor);
	
	List<Frete> findByClienteOrderByValorDesc(Cliente cliente);
	
	Frete findFirstByOrderByValorDesc();
}
