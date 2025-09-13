package com.biblioteca.domain.entities.livro.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.livro.Livro;

/**
 * Interface para o repositório de livros.
 * Define operações básicas de persistência para a entidade Livro.
 */
public interface ILivroRepository {

    /**
     * Salva um livro no repositório.
     * 
     * @param livro o livro a ser salvo
     * @return o livro salvo com possíveis atualizações (como ID gerado)
     */
    Livro salvar(Livro livro);

    /**
     * Busca um livro pelo seu identificador único.
     * 
     * @param id o ID do livro a ser buscado
     * @return um Optional contendo o livro, se encontrado, ou vazio caso contrário
     */
    Optional<Livro> buscarPorId(Long id);

    /**
     * Busca um livro pelo seu código ISBN.
     * 
     * @param isbn o ISBN do livro a ser buscado
     * @return um Optional contendo o livro, se encontrado, ou vazio caso contrário
     */
    Optional<Livro> buscarPorIsbn(String isbn);

    /**
     * Retorna todos os livros cadastrados no repositório.
     * 
     * @return uma lista contendo todos os livros
     */
    List<Livro> buscarTodos();

    /**
     * Remove um livro do repositório.
     * 
     * @param livro o livro a ser removido
     */
    void remover(Livro livro);
}
