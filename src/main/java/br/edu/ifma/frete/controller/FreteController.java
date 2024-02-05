package br.edu.ifma.frete.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.edu.ifma.frete.model.Frete;
import br.edu.ifma.frete.service.FreteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/frete")
public class FreteController {

	@Autowired
	private FreteService freteService;

	@GetMapping("/")
	public ResponseEntity<List<Frete>> fretes(){
		List<Frete> fretes = freteService.buscarTodos();
		return ResponseEntity.ok(fretes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Frete> frete(@PathVariable Long id){
		return freteService.buscarPor(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Frete> cadastrar(@Valid @RequestBody Frete frete,
											UriComponentsBuilder builder) {
		
		final Frete freteSalvo = freteService.inserirOuAlterar(frete);
		final URI uri = builder
				.path("/frete/{id}")
				.buildAndExpand(freteSalvo.getId()).toUri();

		return ResponseEntity.created(uri).body(freteSalvo);
	}
	
	@PutMapping ("/{id}")
	public ResponseEntity<Frete> alterar(@PathVariable Long id,
			@RequestBody @Valid Frete frete) throws URISyntaxException {

		if (freteService.naoExisteFreteCom(id) ) {
			return ResponseEntity.notFound().build();

		} else {
			frete.setId(id);
			Frete freteAtualizado = freteService.inserirOuAlterar(frete);
			return ResponseEntity.ok(freteAtualizado );
		}
	}

	
	@DeleteMapping ("/{id}")
	public ResponseEntity<Frete> remover(@PathVariable Long id) {

		Optional<Frete> optional = freteService.buscarPor(id);

		if (optional.isPresent()) {
			freteService.remover(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
