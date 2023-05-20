# Rest com Spring Boot e Java - Erudio


Este repositório contém o código-fonte e os recursos relacionados ao projeto "Rest com Spring Boot e Java" desenvolvido durante o curso REST API's "RESTFul do 0 à AWS c. Spring Boot 3 Java e Docker" na Udemy.


## Descrição

O projeto "Rest com Spring Boot e Java" é uma aplicação de exemplo que demonstra como criar uma API RESTful usando o Spring Boot e a linguagem de programação Java. A aplicação tem o objetivo de fornecer informações sobre entidades como usuários, livros, cursos, etc.

## Funcionalidades

O projeto inclui as seguintes funcionalidades principais:

- Operações CRUD (Criar, Ler, Atualizar, Excluir) para entidades usuários e livros.
- Validação dos dados de entrada usando anotações do Spring.
- Paginação e ordenação dos resultados retornados pela API.
- Tratamento personalizado de exceções.
- Autenticação e autorização de usuários usando JSON Web Tokens (JWT).
- Testes unitários e de integração para as funcionalidades principais.

## Tecnologias Utilizadas

As principais tecnologias utilizadas neste projeto são:

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- Hibernate
- Banco de dados MySQL
- Maven
- JSON Web Tokens (JWT)

## Configuração

Para executar o projeto em seu ambiente local, siga as etapas abaixo:

1. Certifique-se de ter o Java e o Maven instalados em sua máquina.
2. Clone este repositório para o seu ambiente local.
3. Configure as informações do banco de dados MySQL no arquivo `application.properties`.
4. Execute o comando `mvn clean install` na raiz do projeto para baixar as dependências e compilar o código.
5. Execute o comando `mvn spring-boot:run` para iniciar a aplicação.

Após seguir essas etapas, a aplicação estará disponível em `http://localhost:8080`.

## Documentação da API

A documentação completa da API, incluindo os endpoints disponíveis e exemplos de uso, pode ser encontrada em [Swagger UI](http://localhost:8080/swagger-ui.html) após iniciar a aplicação.


