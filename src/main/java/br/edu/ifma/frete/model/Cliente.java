package br.edu.ifma.frete.model;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Nome deve ser preenchido")
	private String nome;
	
	@NotBlank(message = "Endereco deve ser preenchido")
	private String endereco;
	
	@NotBlank(message = "Telefone deve ser preenchido")
	@Length(min = 11, max = 11, message = "O telefone deverá ter no máximo {max} caracteres")
	private String telefone;
}
