package br.edu.ifma.frete.model;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "frete")
public class Frete {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Cidade não pode ser nulo")
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Cidade cidade;
	
	@NotNull(message = "Cliente não pode ser nulo")
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	private Cliente cliente;
	
	@NotBlank(message = "Descricao deve ser preenchido")
	private String descricao;
	
	@NotNull(message = "Peso deve ser preenchido")
	private Double peso;
	
	private BigDecimal valor;
	
	public Long cidadeId() {
		return this.cidade.getId();
	}
	
	public BigDecimal taxaCidade() {
		return this.cidade.getTaxa();
	}
	
	public Long clienteId() {
		return this.cidade.getId();
	}
}