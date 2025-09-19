# 1. Introdução e Visão Geral

## 1.1. Contexto do Projeto

A aplicação **Biblioteca A7** foi desenvolvida como parte de um teste técnico para o processo seletivo da empresa **Alpha7**, localizada em Limeira, para a vaga de Desenvolvedor Java Pleno. O nome "A7" é uma referência direta à empresa.

O projeto consiste em um sistema de gerenciamento de biblioteca (SGB) projetado para facilitar o controle e a organização de um acervo de livros, autores e editoras. Ele foi desenvolvido em Java e se destaca por sua arquitetura desacoplada, que consiste em um servidor robusto e uma interface de usuário cliente.

- **Servidor (API REST):** O núcleo do sistema, responsável por toda a lógica de negócio, persistência de dados e exposição de funcionalidades através de uma API RESTful.
- **Cliente (Desktop Swing):** Uma aplicação desktop que consome a API para fornecer uma interface gráfica rica e interativa para o usuário final.

## 1.2. Objetivo e Propósito

O principal objetivo do projeto é demonstrar as habilidades técnicas e o conhecimento em desenvolvimento de software do candidato, abordando os seguintes pontos:

- **Domínio de Java e Jakarta EE:** Aplicar conceitos modernos do ecossistema Java para construir uma aplicação robusta e escalável.
- **Arquitetura em Camadas e DDD:** Implementar uma arquitetura bem definida (Apresentação, Aplicação, Domínio, Infraestrutura) com separação clara de responsabilidades, inspirada no Domain-Driven Design.
- **Boas Práticas de Código:** Aplicar princípios como SOLID, Inversão de Dependência e o uso de padrões de projeto (Repository, DTO, etc.).
- **Ecossistema de Persistência:** Utilizar JPA e Hibernate para o mapeamento objeto-relacional e a interação com o banco de dados.
- **Testes Abrangentes:** Demonstrar a capacidade de escrever testes unitários (com Mockito) e de integração (com H2) para garantir a qualidade do software.
- **Construção de API e Cliente:** Desenvolver tanto uma API RESTful quanto um cliente (desktop) que a consome, mostrando a compreensão de arquiteturas cliente-servidor.

## 1.3. Público-Alvo

O público-alvo primário desta aplicação e de sua documentação é a **equipe de avaliação técnica da Alpha7**. Secundariamente, o projeto serve como um portfólio técnico para outros desenvolvedores e recrutadores interessados em avaliar as competências do autor.