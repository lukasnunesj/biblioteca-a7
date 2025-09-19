# 7. APIs e Endpoints

Esta seção documenta todos os endpoints da API REST da **Biblioteca A7**. O caminho base para todos os endpoints é `/biblioteca-a7/api`.

## 7.1. Recurso: Livros (`/livros`)

### Listar todos os livros

- **Método:** `GET`
- **URL:** `/livros`
- **Descrição:** Retorna uma lista de todos os livros cadastrados.
- **Resposta (200 OK):**
  ```json
  [
    {
      "id": 1,
      "titulo": "O Senhor dos Anéis",
      "isbn": "978-85-9508-080-0",
      "anoPublicacao": 1954,
      "autor": {"id": 1, "nome": "J.R.R. Tolkien"},
      "editora": {"id": 1, "nome": "HarperCollins"}
    }
  ]
  ```

### Buscar livro por ID

- **Método:** `GET`
- **URL:** `/livros/{id}`
- **Descrição:** Retorna um livro específico pelo seu ID.
- **Parâmetros:**
  - `id` (path): O ID do livro.
- **Resposta (200 OK):**
  ```json
  {
    "id": 1,
    "titulo": "O Senhor dos Anéis",
    "isbn": "978-85-9508-080-0",
    "anoPublicacao": 1954,
    "autor": {"id": 1, "nome": "J.R.R. Tolkien"},
    "editora": {"id": 1, "nome": "HarperCollins"}
  }
  ```
- **Resposta (404 Not Found):** Se o livro não for encontrado.

### Criar novo livro

- **Método:** `POST`
- **URL:** `/livros`
- **Descrição:** Cadastra um novo livro.
- **Corpo da Requisição:**
  ```json
  {
    "titulo": "O Hobbit",
    "isbn": "978-85-9508-082-4",
    "anoPublicacao": 1937,
    "autorId": 1,
    "editoraId": 1
  }
  ```
- **Resposta (201 Created):** Retorna o livro recém-criado.

### Atualizar livro

- **Método:** `PUT`
- **URL:** `/livros/{id}`
- **Descrição:** Atualiza os dados de um livro existente.
- **Parâmetros:**
  - `id` (path): O ID do livro a ser atualizado.
- **Corpo da Requisição:**
  ```json
  {
    "titulo": "O Hobbit (Edição de Colecionador)",
    "isbn": "978-85-9508-082-4",
    "anoPublicacao": 1937,
    "autorId": 1,
    "editoraId": 1
  }
  ```
- **Resposta (200 OK):** Retorna o livro com os dados atualizados.

### Deletar livro

- **Método:** `DELETE`
- **URL:** `/livros/{id}`
- **Descrição:** Remove um livro do sistema.
- **Parâmetros:**
  - `id` (path): O ID do livro a ser removido.
- **Resposta (204 No Content):** Em caso de sucesso.

### Funcionalidades Especiais

- **Buscar por termo:**
  - **Método:** `GET`
  - **URL:** `/livros/search?termo={termo}`
  - **Descrição:** Busca livros cujo título, nome do autor ou nome da editora contenham o termo pesquisado.

- **Cadastrar por ISBN (Open Library):**
  - **Método:** `POST`
  - **URL:** `/livros/isbn/{isbn}`
  - **Descrição:** Busca um livro na API externa da Open Library pelo ISBN e, se encontrado, o cadastra no sistema.

- **Importar CSV:**
  - **Método:** `POST`
  - **URL:** `/livros/importar-csv`
  - **Descrição:** Realiza a importação de livros em massa a partir de um arquivo CSV enviado como `multipart/form-data`.
  - **Corpo da Requisição:** `Content-Type: multipart/form-data`, com um campo `file` contendo o arquivo CSV.

---

## 7.2. Recurso: Autores (`/autores`)

Os endpoints para autores seguem o mesmo padrão de CRUD do recurso de livros:

- `GET /autores`: Lista todos os autores.
- `GET /autores/{id}`: Busca um autor por ID.
- `GET /autores/search?termo={termo}`: Busca autores por nome.
- `POST /autores`: Cria um novo autor.
  - **Corpo:** `{"nome": "George Orwell"}`
- `PUT /autores/{id}`: Atualiza um autor.
  - **Corpo:** `{"nome": "George R. R. Martin"}`
- `DELETE /autores/{id}`: Remove um autor.

---

## 7.3. Recurso: Editoras (`/editoras`)

Os endpoints para editoras também seguem o padrão de CRUD:

- `GET /editoras`: Lista todas as editoras.
- `GET /editoras/{id}`: Busca uma editora por ID.
- `GET /editoras/search?termo={termo}`: Busca editoras por nome.
- `POST /editoras`: Cria uma nova editora.
  - **Corpo:** `{"nome": "Rocco"}`
- `PUT /editoras/{id}`: Atualiza uma editora.
  - **Corpo:** `{"nome": "Editora Rocco"}`
- `DELETE /editoras/{id}`: Remove uma editora.