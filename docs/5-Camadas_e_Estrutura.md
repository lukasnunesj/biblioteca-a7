# 5. Camadas e Estrutura do Código

Esta seção detalha a responsabilidade de cada camada e dos principais componentes da aplicação, fornecendo um guia para a navegação e o entendimento do código-fonte.

## 5.1. Relação entre as Camadas

A comunicação entre as camadas segue um fluxo unidirecional e bem definido para garantir o baixo acoplamento:

`Apresentação (API/UI)` → `Aplicação` → `Domínio` ← `Infraestrutura`

- A camada de **Apresentação** conhece a camada de **Aplicação**, mas não o Domínio ou a Infraestrutura.
- A camada de **Aplicação** conhece o **Domínio** (suas entidades e interfaces de repositório/serviço).
- A camada de **Domínio** é o núcleo e não conhece nenhuma outra camada. Ela é completamente isolada.
- A camada de **Infraestrutura** conhece o **Domínio** para implementar suas interfaces (ex: um repositório de infraestrutura implementa uma interface de repositório do domínio).

## 5.2. Detalhamento das Camadas

### Camada de Apresentação (`com.biblioteca.api` e `com.biblioteca.presentation`)

- **`com.biblioteca.api.resources`**: Contém os endpoints JAX-RS (Controllers).
  - **Classes Principais:** `LivroResource`, `AutorResource`, `EditoraResource`.
  - **Responsabilidade:** Receber requisições HTTP, validar dados de entrada (DTOs), chamar os serviços da camada de aplicação e formatar a resposta HTTP (status codes e JSON).

- **`com.biblioteca.presentation`**: Contém as classes da interface gráfica Swing.
  - **Classes Principais:**
    - `TelaPrincipal`: A janela principal da aplicação, que serve como ponto de entrada e navegação.
    - `formularios` (ex: `FormLivros`): Janelas para cadastro e edição de entidades.
    - `telasListagem` (ex: `TelaListagemLivros`): Janelas para exibir listas de entidades em tabelas.
    - `util.ApiClient`: Classe responsável por fazer as chamadas HTTP para a API REST do servidor, desacoplando a UI da lógica de comunicação.

### Camada de Aplicação (`com.biblioteca.application`)

- **`com.biblioteca.application`**: Contém os serviços que orquestram os casos de uso.
  - **Classes Principais:** `LivroService`, `AutorService`, `EditoraService`.
  - **Responsabilidade:** Implementar a lógica de aplicação. Eles recebem dados (geralmente DTOs) da camada de apresentação, utilizam os repositórios para interagir com o banco de dados, executam regras de negócio e retornam os resultados. Eles implementam as interfaces definidas no domínio (ex: `ILivroService`).

- **`com.biblioteca.application.livro.service`**: Contém serviços mais específicos relacionados a livros.
  - **Classes Principais:** `ImportacaoLivroService`, `CadastrarLivroPorIsbnService`.
  - **Responsabilidade:** Lidar com casos de uso complexos, como a importação de livros de um arquivo CSV ou a busca e cadastro de um livro a partir de uma API externa (Open Library).

### Camada de Domínio (`com.biblioteca.domain`)

- **`com.biblioteca.domain.entities`**: O coração da aplicação, contendo as entidades de negócio e suas regras.
  - **Classes Principais:** `Livro`, `Autor`, `Editora`.
  - **Responsabilidade:** Representar os conceitos fundamentais do negócio. Contêm os atributos e os métodos que validam o estado e o comportamento da própria entidade (ex: um método `validar()` dentro da entidade `Livro`).

- **`com.biblioteca.domain.entities.*.interfaces`**: Define os contratos (interfaces) para os serviços e repositórios.
  - **Interfaces Principais:** `ILivroRepository`, `IAutorRepository`, `ILivroService`.
  - **Responsabilidade:** Desacoplar o domínio das implementações concretas. Ao depender de interfaces, o domínio se torna imune a mudanças em tecnologias de banco de dados ou frameworks.

### Camada de Infraestrutura (`com.biblioteca.infrastructure`)

- **`com.biblioteca.infrastructure.repositories`**: Contém as implementações dos repositórios.
  - **Classes Principais:** `LivroRepository`, `AutorRepository`.
  - **Responsabilidade:** Implementar as interfaces de repositório do domínio utilizando uma tecnologia de persistência específica, neste caso, JPA/Hibernate. Toda a lógica de acesso a dados (consultas JPQL, `EntityManager`) está contida aqui.

- **`com.biblioteca.infrastructure.exceptions`**: Define as exceções customizadas da aplicação.
  - **Classes Principais:** `RecursoNaoEncontradoException`, `ValidacaoException`, `PersistenciaException`.
  - **Responsabilidade:** Criar um sistema de exceções semântico, que permite à camada de apresentação (API) capturar exceções específicas e retornar os códigos de status HTTP apropriados (ex: `404 Not Found` para `RecursoNaoEncontradoException`). A classe `ExceptionHandler` mapeia essas exceções para respostas HTTP.