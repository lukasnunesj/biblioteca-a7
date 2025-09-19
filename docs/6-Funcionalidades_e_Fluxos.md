# 6. Funcionalidades e Fluxos

Esta seção descreve as principais funcionalidades da aplicação **Biblioteca A7** e os fluxos de trabalho associados a elas.

## 6.1. Lista de Funcionalidades

A aplicação oferece um conjunto completo de operações de CRUD (Create, Read, Update, Delete) para as seguintes entidades:

- **Gerenciamento de Livros:**
  - Cadastro manual de novos livros.
  - Edição de informações de livros existentes.
  - Remoção de livros do acervo.
  - Listagem e busca de livros por título, autor ou editora.

- **Gerenciamento de Autores:**
  - Cadastro, edição, remoção e busca de autores.

- **Gerenciamento de Editoras:**
  - Cadastro, edição, remoção e busca de editoras.

Além do CRUD básico, o sistema possui as seguintes funcionalidades especiais:

- **Cadastro de Livro por ISBN:** Permite cadastrar um livro rapidamente apenas informando seu código ISBN. O sistema busca as informações do livro (título, autor, etc.) em uma API externa (Open Library) e as preenche automaticamente.

- **Importação de Livros em Massa:** Permite importar múltiplos livros de uma vez a partir de um arquivo no formato CSV, agilizando a alimentação inicial do banco de dados.

- **Relacionamento de Livros Semelhantes:** Permite associar livros que são considerados semelhantes, facilitando a recomendação e a descoberta de novas obras.

## 6.2. Fluxos Principais

### Fluxo 1: Cadastro de um Novo Livro (Manual)

Este fluxo pode ser iniciado tanto pela API REST quanto pela interface desktop.

1.  **Usuário/Cliente** envia uma requisição `POST /api/livros` com um JSON contendo os dados do livro (título, ISBN, ano, ID do autor e ID da editora).
2.  O `LivroResource` recebe a requisição.
3.  O `Resource` chama o `LivroService`, passando um `LivroDTO` com os dados recebidos.
4.  O `LivroService` realiza as seguintes ações:
    a. Valida os dados recebidos (ex: verifica se o título não está em branco).
    b. Utiliza o `AutorRepository` e o `EditoraRepository` para buscar as entidades `Autor` e `Editora` a partir dos IDs fornecidos.
    c. Cria uma nova instância da entidade `Livro` com os dados validados e as entidades relacionadas.
    d. Chama o `livroRepository.salvar()` para persistir o novo livro no banco de dados.
5.  O `LivroRepository` utiliza o `EntityManager` do JPA para executar a operação de `persist`.
6.  O `LivroService` retorna a entidade `Livro` salva para o `LivroResource`.
7.  O `LivroResource` converte a entidade para um `LivroDTO` e a retorna no corpo da resposta HTTP com o status `201 Created`.

### Fluxo 2: Importação de Livros via CSV

Este fluxo é específico da API e pode ser acionado pela interface desktop.

1.  **Usuário/Cliente** envia uma requisição `POST /api/livros/importar-csv` do tipo `multipart/form-data`, contendo o arquivo CSV.
2.  O `LivroResource` recebe a requisição e extrai o `InputStream` do arquivo.
3.  O `Resource` chama o método `importar()` do `IImportacaoLivroService`.
4.  O `ImportacaoLivroService` realiza as seguintes ações:
    a. Utiliza a biblioteca **Apache Commons CSV** para ler o arquivo linha por linha.
    b. Para cada linha do CSV, ele extrai os dados do livro (título, ISBN, nome do autor, nome da editora).
    c. Para cada livro, ele verifica se o autor e a editora já existem no banco de dados. Se não existirem, ele os cria.
    d. Cria uma nova entidade `Livro` e a associa ao autor e à editora correspondentes.
    e. Salva o novo livro no banco de dados através do `LivroRepository`.
    f. Toda a operação é executada dentro de uma única transação para garantir a atomicidade.
5.  Ao final do processo, o serviço conclui a transação.
6.  O `LivroResource` retorna uma resposta HTTP `200 OK` com uma mensagem de sucesso.