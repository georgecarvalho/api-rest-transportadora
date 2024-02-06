# Sistema de biblioteca

Implementação de Testes para uma API REST usando Spring Boot para um fragmento de um Sistema de Transportadora.
Foram realizados o testes de integração para as classes repository (com @DataJpaTest), service e controller (com @SpringBootTest).
Projeto desenvolvido durante a disciplina de Engenharia de Software II, do curso de Sistemas de Informação - IFMA, em 2021.

## 🔩 Testes de Integração

Os testes desenvolvidos nesse projeto têm como objetivo verificar as operações de CRUD, as requisições da API REST e as validações da entidades (Bean Validation).

### Testes de FreteRepository

```
Deve Buscar Frete Pelo Valor
Deve Buscar Fretes Por Cliente Ordenados Por Valor Decrescente
Deve Lancar Excecao Caso Descricao Seja Nulo
Deve Lancar Excecao Caso Peso Seja Nulo
Deve Lancar Excecao Caso Cidade Seja Nulo
```

### Testes de FreteService

```
Deve Cadastrar Novo Frete
Deve Remover Frete Cadastrado
Deve Lancar Excecao Caso Cidade Seja Nulo
Deve Lancar Excecao Caso Cliente Seja Nulo
Deve Recuperar Valor Do Frete
Deve Recuperar Frete De Maior Valor
```

### Testes de FreteController

```
Deve Mostrar Todos Fretes
Deve Buscar Um Frete Pelo Id
Deve Retornar 404 Quando Frete Nao For Encontrado
Nao Deve Encontrar Frete Inexistente
Nao Deve Salvar Frete Com Erro De Validacao
Deve Retornar Mensagem De Erro Quando Alterar Frete
Deve Alterar Frete
Deve Excluir Frete
```

## 🛠️ Construído com

* [Maven](https://maven.apache.org/) - Gerente de Dependências
* [Lombok Project](https://projectlombok.org/) - Biblioteca de Anotações para Getters, Setters, Builders, etc
* [Spring Boot](https://spring.io/projects/spring-boot/) - Framework para aplicações Spring

## ✒️ Autores

* **George Sanders** - [georgecarvalho](https://github.com/georgecarvalho)

## 📄 Licença

Este projeto está sob a licença - veja o arquivo [LICENSE.md](https://github.com/georgecarvalho/frete/blob/main/LICENSE) para detalhes.
