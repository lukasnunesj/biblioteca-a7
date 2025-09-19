# Biblioteca A7

## Visão Geral

O projeto **Biblioteca A7** foi desenvolvido como um teste técnico para a empresa **Alpha7**, para uma vaga de Desenvolvedor Java Pleno. Trata-se de um sistema de gerenciamento de acervo de livros, desenvolvido em Java, e composto por duas partes principais:

1.  **Servidor (API REST):** Um serviço robusto construído com **Jakarta EE 10**, utilizando JAX-RS para os endpoints, CDI para injeção de dependências, e JPA/Hibernate para a persistência de dados em um banco de dados PostgreSQL. Ele expõe funcionalidades para gerenciar livros, autores e editoras.
2.  **Cliente (Desktop):** Uma aplicação de desktop desenvolvida com **Java Swing**, que consome a API REST do servidor para realizar as operações. Isso demonstra uma arquitetura desacoplada, onde a interface do usuário é um cliente do serviço principal.

O objetivo do sistema é demonstrar a aplicação de boas práticas de arquitetura e desenvolvimento de software no ecossistema Java, servindo como uma avaliação das competências técnicas do candidato.

---

## Documentação Técnica

Toda a documentação técnica do projeto está centralizada no diretório `/docs`. Abaixo está o índice para navegar pelos diferentes tópicos.

### Índice da Documentação

1.  [Introdução e Visão Geral](./docs/1-Introducao_e_Visao_Geral.md)
2.  [Arquitetura e Padrões de Projeto](./docs/2-Arquitetura_e_Padroes.md)
3.  [Padrões de Desenvolvimento e Convenções](./docs/3-Desenvolvimento_e_Convencoes.md)
4.  [Instalação e Inicialização](./docs/4-Instalacao_e_Inicializacao.md)
5.  [Camadas e Estrutura do Código](./docs/5-Camadas_e_Estrutura.md)
6.  [Funcionalidades e Fluxos](./docs/6-Funcionalidades_e_Fluxos.md)
7.  [APIs e Endpoints](./docs/7-APIs_e_Endpoints.md)
8.  [Banco de Dados](./docs/8-Banco_de_Dados.md)
9.  [Testes](./docs/9-Testes.md)
10. [Deployment e Ambiente](./docs/10-Deployment_e_Ambiente.md)
11. [Manutenção e Evolução](./docs/11-Manutencao_e_Evolucao.md)

---

### Disclaimer

Para informações sobre a autoria do projeto e o uso de ferramentas de apoio no desenvolvimento, consulte o [Disclaimer de Contribuição](./docs/DISCLAIMER.md).
