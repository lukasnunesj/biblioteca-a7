# 2. Arquitetura e Padrões de Projeto

## 2.1. Arquitetura em Camadas

A aplicação **Biblioteca A7** adota uma **Arquitetura em Camadas (Layered Architecture)**, fortemente influenciada pelos princípios do **Domain-Driven Design (DDD)**. As responsabilidades são claramente segregadas em pacotes, promovendo baixo acoplamento, alta coesão e facilitando a manutenção e evolução do sistema.

As camadas são organizadas da seguinte forma:

1.  **Apresentação (`presentation` e `api`):** Responsável pela interação com o usuário ou sistemas externos. É dividida em duas partes:
    *   **Cliente (`com.biblioteca.presentation`):** Contém as classes da interface gráfica **Desktop (Swing)**. As telas (`TelaPrincipal`, `Formularios`) interagem com a camada de aplicação através de um cliente HTTP (`ApiClient`), consumindo a API REST do servidor.
    *   **Servidor (`com.biblioteca.api`):** Expõe as funcionalidades do sistema como uma **API RESTful** usando JAX-RS. Os `Resources` (ex: `LivroResource`) atuam como *Controllers*, recebendo as requisições HTTP, delegando a execução para a camada de aplicação e retornando as respostas (geralmente em JSON).

2.  **Aplicação (`application`):** Orquestra as regras de negócio e os casos de uso do sistema. Os `Services` (ex: `LivroService`) contêm a lógica de aplicação, mas não a lógica de domínio. Eles coordenam a comunicação entre os repositórios e outras entidades para executar uma tarefa específica (ex: cadastrar um novo livro).

3.  **Domínio (`domain`):** O coração do software. Contém as entidades de negócio (`Livro`, `Autor`), que encapsulam os dados e as regras de negócio mais importantes. Esta camada também define as **interfaces** para os repositórios e serviços (`ILivroRepository`, `IAutorService`), seguindo o **Princípio da Inversão de Dependência (DIP)**. Isso garante que o domínio não dependa de detalhes de implementação da infraestrutura.

4.  **Infraestrutura (`infrastructure`):** Fornece as implementações técnicas para as interfaces definidas na camada de domínio. Isso inclui:
    *   `Repositories`: Implementações concretas que acessam o banco de dados (ex: `LivroRepository` usando JPA/Hibernate).
    *   `Exceptions`: Classes de tratamento de exceções customizadas.
    *   `Config`: Configurações de beans, persistência (`persistence.xml`), etc.

## 2.2. Padrões de Projeto Aplicados

O projeto utiliza diversos padrões de projeto para garantir um código limpo, organizado e extensível:

- **Repository:** Abstrai o acesso aos dados. As interfaces (`ILivroRepository`) são definidas no domínio, e as implementações (`LivroRepository`) ficam na infraestrutura, isolando a lógica de negócio dos detalhes de persistência.

- **Data Transfer Object (DTO):** Usado para transferir dados entre as camadas, especialmente entre a camada de apresentação (API) e a de aplicação. Os DTOs (ex: `LivroDTO`) ajudam a evitar o vazamento de entidades de domínio para o mundo exterior e permitem a customização dos dados expostos nos endpoints.

- **Dependency Injection (DI):** Utilizado extensivamente através do **CDI (Contexts and Dependency Injection)** do Jakarta EE. As dependências (como repositórios e serviços) são injetadas automaticamente onde necessário (ex: `@Inject` em um serviço para injetar um repositório), o que reduz o acoplamento e facilita os testes.

- **Singleton:** Embora o CDI gerencie o ciclo de vida dos beans, o padrão Singleton é conceitualmente aplicado para garantir que exista uma única instância de serviços e repositórios gerenciados pelo container.

- **Facade (via API Resources):** Os `Resources` da API atuam como uma fachada, fornecendo uma interface simplificada e unificada para as funcionalidades complexas da camada de aplicação.

- **Template Method:** Identificado nas classes `FormPadrao` e `TelaListagemPadrao` da camada de apresentação, onde uma estrutura de formulário/tela é definida, e as subclasses (`FormAutores`, `TelaListagemAutores`) implementam os passos específicos.