package br.edu.ifma.frete.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifma.frete.model.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long>{
	Cidade findByNome(String nome);
}
