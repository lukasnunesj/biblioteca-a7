# 11. Manutenção e Evolução

Esta seção fornece orientações para desenvolvedores que desejam contribuir com o projeto, estendendo suas funcionalidades ou realizando manutenções, sempre respeitando a arquitetura e os padrões estabelecidos.

## 11.1. Como Estender a Aplicação

A arquitetura em camadas e o baixo acoplamento da aplicação facilitam a adição de novas funcionalidades. A seguir, um guia passo a passo sobre como adicionar uma nova entidade de negócio ao sistema (ex: `Categoria`).

**Passo 1: Criar a Entidade de Domínio**

- Crie a classe `Categoria.java` no pacote `com.biblioteca.domain.entities.categoria`.
- Adicione os atributos, construtores, getters, setters e as anotações JPA (`@Entity`, `@Table`, `@Id`, etc.).

**Passo 2: Criar o DTO**

- Crie a classe `CategoriaDTO.java` no pacote `com.biblioteca.domain.entities.categoria.DTO`.
- Adicione os atributos que serão expostos na API e os métodos de conversão (`fromEntity` e `toEntity`).

**Passo 3: Definir as Interfaces de Contrato**

- No pacote `com.biblioteca.domain.entities.categoria.interfaces`, crie:
  - `ICategoriaRepository.java`: Interface que estende `IBaseRepository<Categoria>` e define métodos de consulta específicos para categorias.
  - `ICategoriaService.java`: Interface que define os métodos para os casos de uso de categoria (ex: `salvar`, `buscarPorId`).

**Passo 4: Implementar o Repositório**

- Crie a classe `CategoriaRepository.java` no pacote `com.biblioteca.infrastructure.repositories`.
- Implemente a interface `ICategoriaRepository`.
- Injete o `EntityManager` e implemente os métodos de acesso a dados usando JPQL.

**Passo 5: Implementar o Serviço**

- Crie a classe `CategoriaService.java` no pacote `com.biblioteca.application`.
- Implemente a interface `ICategoriaService`.
- Injete o `ICategoriaRepository` e implemente a lógica de negócio para os casos de uso.

**Passo 6: Criar o Endpoint na API**

- Crie a classe `CategoriaResource.java` no pacote `com.biblioteca.api.resources`.
- Adicione a anotação `@Path("/categorias")`.
- Injete o `ICategoriaService`.
- Crie os métodos para cada operação HTTP (`GET`, `POST`, `PUT`, `DELETE`), que irão receber e retornar `CategoriaDTO`.

**Passo 7: Adicionar Testes**

- Crie os testes unitários para a entidade `Categoria` e para o `CategoriaService` (usando mocks).
- Crie os testes de integração para o `CategoriaRepository` e para o `CategoriaService` (usando o banco H2).

**Passo 8 (Opcional): Adicionar a Interface Gráfica**

- Se desejar, crie as telas `FormCategorias` e `TelaListagemCategorias` no pacote `com.biblioteca.presentation`, seguindo o padrão das outras entidades.
- Atualize a `TelaPrincipal` para dar acesso à nova funcionalidade.

## 11.2. Boas Práticas para Contribuição

- **Siga as Convenções:** Antes de codificar, leia a seção [Padrões de Desenvolvimento e Convenções](./3-Desenvolvimento_e_Convencoes.md).
- **Escreva Testes:** Nenhuma nova funcionalidade ou correção de bug será aceita sem os testes correspondentes.
- **Mantenha a Documentação Atualizada:** Se você adicionar ou modificar uma funcionalidade, atualize a documentação relevante (principalmente a de endpoints e fluxos).
- **Commits Atômicos:** Faça commits pequenos e focados, seguindo o padrão de [Conventional Commits](./3-Desenvolvimento_e_Convencoes.md#32-boas-praticas-de-commit-e-versionamento).