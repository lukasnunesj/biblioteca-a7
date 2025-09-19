# 9. Testes

A aplicação **Biblioteca A7** possui uma suíte de testes robusta para garantir a qualidade, a estabilidade e a corretude do código. A estratégia de testes abrange diferentes níveis, desde testes unitários focados em pequenas partes do código até testes de integração que validam a colaboração entre as camadas.

As principais ferramentas utilizadas para os testes são:

- **JUnit 4:** Framework base para a escrita dos testes.
- **Mockito:** Para a criação de *mocks* e *stubs*, permitindo o isolamento de componentes nos testes unitários.
- **AssertJ:** Para a escrita de asserções fluentes e legíveis.
- **H2 Database:** Um banco de dados em memória utilizado para os testes de integração, garantindo um ambiente de teste rápido e isolado.

## 9.1. Estrutura de Testes

O código de teste está localizado em `src/test/java`, seguindo a mesma estrutura de pacotes do código-fonte (`src/main/java`). Isso facilita a localização dos testes correspondentes a cada classe da aplicação.

## 9.2. Tipos de Testes Utilizados

### 9.2.1. Testes Unitários

Os testes unitários focam em validar as menores unidades de código de forma isolada. Eles são rápidos e essenciais para garantir que a lógica interna de cada componente funcione como esperado.

- **Entidades de Domínio (`com.biblioteca.domain.entities.*`):**
  - **Exemplos:** `LivroTest.java`, `AutorTest.java`.
  - **Objetivo:** Testar a lógica de negócio encapsulada nas próprias entidades, como validações em construtores, regras de estado e comportamento de métodos internos.

- **Serviços da Camada de Aplicação (`com.biblioteca.application.*`):**
  - **Exemplos:** `LivroServiceTest.java`, `ImportacaoLivroServiceTest.java`.
  - **Objetivo:** Testar a lógica de orquestração dos serviços. Nesses testes, as dependências externas (como os repositórios) são substituídas por *mocks* criados com Mockito. Isso permite testar a lógica do serviço (ex: `salvar`, `buscarPorTermo`) sem depender da camada de persistência.

### 9.2.2. Testes de Integração

Os testes de integração validam a colaboração entre diferentes camadas do sistema, garantindo que elas funcionem corretamente em conjunto. Na **Biblioteca A7**, esses testes estão concentrados no pacote `com.biblioteca.integration` e utilizam o banco de dados H2.

- **Repositórios (`com.biblioteca.integration.repositories.*`):**
  - **Exemplos:** `LivroRepositoryIntegrationTest.java`, `AutorRepositoryIntegrationTest.java`.
  - **Objetivo:** Testar a camada de persistência de ponta a ponta. Eles validam se as anotações de mapeamento JPA estão corretas e se as consultas JPQL nos repositórios funcionam como esperado contra um banco de dados real.

- **Serviços (`com.biblioteca.integration.services.*`):**
  - **Exemplos:** `LivroServiceIntegrationTest.java`, `AutorServiceIntegrationTest.java`.
  - **Objetivo:** Testar a integração completa entre a camada de serviço e a camada de persistência, sem o uso de *mocks*. Esses testes garantem que um caso de uso funcione desde a chamada do serviço até a correta manipulação dos dados no banco.

## 9.3. Como Executar os Testes

Para executar todos os testes da aplicação, utilize o seguinte comando Maven na raiz do projeto:

```bash
mvn test
```

O Maven irá compilar o código de teste, executar todas as suítes (unitárias e de integração) e apresentar um relatório dos resultados.