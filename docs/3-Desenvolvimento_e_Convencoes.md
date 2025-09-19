# 3. Padrões de Desenvolvimento e Convenções

Esta seção descreve as convenções e boas práticas adotadas no desenvolvimento da aplicação **Biblioteca A7**. A adesão a estas diretrizes é fundamental para manter a qualidade, a legibilidade e a consistência do código-fonte.

## 3.1. Convenções de Nomenclatura

- **Pacotes:** Utilizam `lowercase` e seguem a estrutura de domínio reverso (ex: `com.biblioteca.domain.entities`). Os nomes devem ser concisos e representar claramente sua responsabilidade.

- **Classes e Interfaces:** Utilizam `PascalCase`.
  - **Interfaces:** Devem ser prefixadas com `I` (ex: `ILivroRepository`, `IAutorService`).
  - **Classes de Entidade:** Nomes de substantivos que representam o conceito de negócio (ex: `Livro`, `Autor`).
  - **Serviços:** Sufixo `Service` (ex: `LivroService`).
  - **Repositórios:** Sufixo `Repository` (ex: `LivroRepository`).
  - **DTOs:** Sufixo `DTO` (ex: `LivroDTO`).
  - **Controllers/Resources:** Sufixo `Resource` (ex: `LivroResource`).
  - **Telas (Swing):** Prefixadas com `Tela` ou `Form` (ex: `TelaPrincipal`, `FormAutores`).

- **Métodos:** Utilizam `camelCase` e devem ter nomes que indiquem claramente a ação que executam (ex: `buscarLivroPorId`, `salvarAutor`).

- **Variáveis:** Utilizam `camelCase`.
  - **Constantes:** Utilizam `UPPER_SNAKE_CASE` (ex: `MAX_TENTATIVAS`).

## 3.2. Boas Práticas de Commit e Versionamento

O projeto segue o padrão **Conventional Commits**. Cada mensagem de commit deve ter um formato claro e padronizado, o que facilita a automação de changelogs e o entendimento do histórico de alterações.

**Formato do Commit:**
```
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

- **Tipos Comuns:**
  - `feat`: Uma nova funcionalidade.
  - `fix`: Uma correção de bug.
  - `docs`: Alterações na documentação.
  - `style`: Alterações que não afetam o significado do código (espaços, formatação, etc.).
  - `refactor`: Uma alteração de código que não corrige um bug nem adiciona uma funcionalidade.
  - `test`: Adição ou correção de testes.
  - `chore`: Alterações em processos de build, ferramentas auxiliares, etc.

**Exemplo:**
```
feat(api): adicionar endpoint para busca de livros por ISBN

Implementa o GET /api/livros/isbn/{isbn} que permite a busca de livros
na base de dados a partir do seu código ISBN.
```

## 3.3. Convenções de Formatação de Código

- **Indentação:** Utilizar 4 espaços para indentação (padrão do Java).
- **Linhas em Branco:** Usar linhas em branco para separar métodos e blocos lógicos de código, melhorando a legibilidade.
- **Comentários:** Escrever comentários claros e concisos apenas quando necessário para explicar partes complexas do código. Evitar comentários óbvios.
- **Organização de Classes:** Manter uma ordem lógica dentro das classes:
  1.  Campos estáticos
  2.  Campos de instância
  3.  Construtores
  4.  Métodos públicos
  5.  Métodos protegidos/privados
  6.  Getters e Setters