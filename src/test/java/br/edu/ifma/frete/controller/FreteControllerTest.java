package br.edu.ifma.frete.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.edu.ifma.frete.model.Cidade;
import br.edu.ifma.frete.model.Cliente;
import br.edu.ifma.frete.model.Frete;
import br.edu.ifma.frete.model.UnidadeFederacao;
import br.edu.ifma.frete.repository.FreteRepository;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FreteControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private FreteRepository freteRepository;

	private Frete frete;
	
	@BeforeEach
	public void start() {
		frete = Frete.builder()
				.cidade(Cidade.builder()
						.nome("Sao Luis")
						.uf(UnidadeFederacao.MARANHAO)
						.taxa(new BigDecimal("100").setScale(2))
						.build())
				.cliente(Cliente.builder()
						.nome("João")
						.endereco("Rua dos Bobos, Numero Zero")
						.telefone("99999999999")
						.build())
				.descricao("Frete 1")
				.peso(500.00)
				.build();
		freteRepository.save(frete);
	}
	
	@AfterEach
	public void end() {
		freteRepository.deleteAll();
	}
	
	@Test
	public void deveMostrarTodosFretes() {
		ResponseEntity<String> resposta =
				testRestTemplate.exchange("/frete/",HttpMethod.GET,
						                   null, String.class);
		System.out.println("######## " + resposta.getBody() );
		
		Assertions.assertEquals(resposta.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void deveMostrarTodosFretesUsandoString() {
		ResponseEntity<String> resposta =
				testRestTemplate.exchange("/frete/", HttpMethod.GET, null, String.class);

		Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
		Assertions.assertEquals(resposta.getHeaders().getContentType(),
				     MediaType.parseMediaType("application/json"));
		
		String result = "[{\"id\":"+frete.getId()+",\"cidade\":{\"id\":"+frete.cidadeId()+","
				+ "\"nome\":\"Sao Luis\",\"uf\":\"MARANHAO\",\"taxa\":100.00},"
				+ "\"cliente\":{\"id\":"+frete.clienteId()+",\"nome\":\"João\","
				+ "\"endereco\":\"Rua dos Bobos, Numero Zero\",\"telefone\":\"99999999999\"},"
				+ "\"descricao\":\"Frete 1\",\"peso\":500.0,\"valor\":null}]";
		Assertions.assertEquals(result, resposta.getBody());
	}
	
	@Test
	public void deveMostrarTodosFretesUsandoList() {
		ParameterizedTypeReference<List<Frete>> tipoRetorno =
				new ParameterizedTypeReference<List<Frete>>() {};

		ResponseEntity<List<Frete>> resposta =
				testRestTemplate.exchange("/frete/",
						                   HttpMethod.GET,null,
						                   tipoRetorno);

		Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
		Assertions.assertEquals(resposta.getHeaders().getContentType(),
				     MediaType.parseMediaType("application/json"));
		Assertions.assertEquals(1, resposta.getBody().size());
		Assertions.assertEquals(frete, resposta.getBody().get(0));
	}
	
	@Test
	public void deveBuscarUmFretePeloId() {
		ResponseEntity<Frete> resposta =
				testRestTemplate.exchange("/frete/{id}",
						                   HttpMethod.GET,null,
						                   Frete.class, frete.getId() );

		Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
		Assertions.assertEquals(resposta.getHeaders().getContentType(),
				MediaType.parseMediaType("application/json"));
		Assertions.assertEquals(frete, resposta.getBody());
	}
	
	@Test
	public void deveRetornar404QuandoFreteNaoForEncontrado() {

		ResponseEntity<Frete> resposta =
				testRestTemplate.exchange("/frete/{id}",
						                   HttpMethod.GET,
						                   null,
						                   Frete.class,
						                   100 );

		Assertions.assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
		Assertions.assertNull(resposta.getBody());
	}
	
	// --------------------- GET --------------------
	
	@Test
	public void deveMostrarUmFreteComGetForEntity() {
		ResponseEntity<Frete> resposta =
				testRestTemplate.getForEntity("/frete/{id}",
						                       Frete.class,frete.getId());

		Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());

		Assertions.assertEquals(resposta.getHeaders().getContentType(),
				     MediaType.parseMediaType("application/json"));

		Assertions.assertEquals(frete, resposta.getBody());
	}

	@Test
	public void deveMostrarUmFreteComGetForObject() {
		Frete resposta = testRestTemplate
				.getForObject("/frete/{id}", Frete.class, frete.getId());
		Assertions.assertEquals(frete, resposta);
	}

	@Test
	public void deveRetornarFreteNaoEncontradoComGetForEntity() {
		ResponseEntity<Frete> resposta;
		resposta = testRestTemplate
				.getForEntity("/frete/{id}", Frete.class, 100);

		Assertions.assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
		Assertions.assertNull(resposta.getBody());
	}

	@Test
	public void naoDeveEncontrarFreteInexistente() {
		Frete resposta = testRestTemplate
				.getForObject("/frete/{id}", Frete.class, 100);
		Assertions.assertNull(resposta);
	}
	
	// ----------------------- POST -------------------------
	
	@Test
	public void naoDeveSalvarFreteComErroDeValidacao() {
		frete.setDescricao(null);
		frete.setPeso(null);
		
		HttpEntity<Frete> httpEntity = new HttpEntity<>(frete);
		
		ResponseEntity<String> resposta =
				testRestTemplate.exchange("/frete",
						HttpMethod.POST,httpEntity,
						String.class);

		Assertions.assertEquals(HttpStatus.BAD_REQUEST,resposta.getStatusCode());
		Assertions.assertTrue(resposta.getBody().contains("Descricao deve ser preenchido"));
		Assertions.assertTrue(resposta.getBody().contains("Peso deve ser preenchido"));
	}

	@Test
	public void deveSalvarFrete() {
		HttpEntity<Frete> httpEntity = new HttpEntity<>(frete);

		ResponseEntity<Frete> resposta =
				testRestTemplate.exchange("/frete",HttpMethod.POST,httpEntity, Frete.class);

		Assertions.assertEquals(HttpStatus.CREATED,resposta.getStatusCode());

		Frete resultado = resposta.getBody();

		Assertions.assertNotNull(resultado.getId());
		Assertions.assertEquals(frete.getDescricao(), resultado.getDescricao());
		Assertions.assertEquals(frete.getPeso(), resultado.getPeso());
		Assertions.assertEquals(frete.getCidade(), resultado.getCidade());
		Assertions.assertEquals(frete.getCliente(), resultado.getCliente());
	}

	@Test
	public void deveSalvarFreteComPostForEntity() {
		HttpEntity<Frete> httpEntity = new HttpEntity<>(frete);
		ResponseEntity<Frete> resposta =
				testRestTemplate.postForEntity("/frete",
						                       httpEntity, Frete.class);

		Assertions.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());

		Frete resultado = resposta.getBody();

		Assertions.assertNotNull(resultado.getId());
		Assertions.assertEquals(frete.getDescricao(), resultado.getDescricao());
		Assertions.assertEquals(frete.getPeso(), resultado.getPeso());
		Assertions.assertEquals(frete.getCidade(), resultado.getCidade());
		Assertions.assertEquals(frete.getCliente(), resultado.getCliente());
	}
	
	@Test
	public void deveSalvarFreteComPostForObject() {
		HttpEntity<Frete> httpEntity = new HttpEntity<>(frete);
		Frete resposta = testRestTemplate.postForObject("/frete",httpEntity, Frete.class);

		Assertions.assertNotNull(resposta.getId());
		Assertions.assertEquals(frete.getDescricao(), resposta.getDescricao());
		Assertions.assertEquals(frete.getPeso(), resposta.getPeso());
		Assertions.assertEquals(frete.getCidade(), resposta.getCidade());
		Assertions.assertEquals(frete.getCliente(), resposta.getCliente());
	}
	
	// ------------------- PUTS e DELETE ------------------------------
	
	@Test
	public void deveRetornarMensagemDeErroQuandoAlterarFrete() {
		frete.setDescricao(null);
		frete.setPeso(null);
		
		HttpEntity<Frete> httpEntity = new HttpEntity<>(frete);
		
		ResponseEntity<String> resposta =
				testRestTemplate.exchange("/frete/{id}",
						HttpMethod.PUT, httpEntity,
						String.class,
						frete.getId());

		Assertions.assertEquals(HttpStatus.BAD_REQUEST,resposta.getStatusCode());
		Assertions.assertTrue(resposta.getBody().contains("Descricao deve ser preenchido"));
		Assertions.assertTrue(resposta.getBody().contains("Peso deve ser preenchido"));
	}

	@Test
	public void deveAlterarFrete() {
		frete.setDescricao("Frete 1 Nova Descricao");
		HttpEntity<Frete> httpEntity = new HttpEntity<>(frete);

		ResponseEntity<Frete> resposta =
				testRestTemplate.exchange("/frete/{id}",HttpMethod.PUT,
						  httpEntity
						, Frete.class,frete.getId());

		Assertions.assertEquals(HttpStatus.OK,resposta.getStatusCode());
		Frete resultado = resposta.getBody();
		Assertions.assertEquals(frete.getId(), resultado.getId());
		Assertions.assertEquals(frete.getPeso(), resultado.getPeso());
		Assertions.assertEquals(frete.getCidade(), resultado.getCidade());
		Assertions.assertEquals(frete.getCliente(), resultado.getCliente());
		Assertions.assertEquals("Frete 1 Nova Descricao", resultado.getDescricao());
	}

	@Test
	public void deveAlterarFreteComPut() {
		frete.setDescricao("Frete 1 Nova Descricao");
		testRestTemplate.put("/frete/{id}",frete,frete.getId());

		Frete resultado = freteRepository.findById(frete.getId()).get();
		Assertions.assertEquals(frete.getPeso(), resultado.getPeso());
		Assertions.assertEquals(frete.getCidade(), resultado.getCidade());
		Assertions.assertEquals(frete.getCliente(), resultado.getCliente());
		Assertions.assertEquals("Frete 1 Nova Descricao", resultado.getDescricao());
	}

	@Test
	public void deveExcluirFrete() {
		ResponseEntity<Frete> resposta =
				testRestTemplate.exchange("/frete/{id}",
						    HttpMethod.DELETE,null
						, Frete.class,frete.getId());

		Assertions.assertEquals(HttpStatus.NO_CONTENT,resposta.getStatusCode());
		Assertions.assertNull(resposta.getBody());
	}

	@Test
	public void deveExcluirFreteComMetodoDelete() {
		testRestTemplate.delete("/frete/"+frete.getId());

		final Optional<Frete> resultado = freteRepository.findById(frete.getId());
		Assertions.assertEquals(Optional.empty(), resultado);
	}
}
