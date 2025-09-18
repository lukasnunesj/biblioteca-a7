package com.biblioteca.domain.entities.livro.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.common.interfaces.IBaseRepository;
import com.biblioteca.domain.entities.livro.Livro;

/**
 * Interface para o repositório de livros.
 * Define operações básicas de persistência para a entidade Livro.
 */
public interface ILivroRepository extends IBaseRepository<Livro, Long> {


    /**
     * Busca um livro pelo seu código ISBN.
     * 
     * @param isbn o ISBN do livro a ser buscado
     * @return um Optional contendo o livro, se encontrado, ou vazio caso contrário
     */
    Optional<Livro> buscarPorIsbn(String isbn);

    /**
     * Busca um livro pelo seu ID, com carregamento antecipado de autores e editora.
     * 
     * @param id o ID do livro a ser buscado
     * @return um Optional contendo o livro, se encontrado, ou vazio caso contrário
     */
        Optional<Livro> findById(Long id);

    /**
     * Busca livros por um termo de pesquisa em vários campos.
     * 
     * @param termo o termo a ser pesquisado
     * @return uma lista de livros que correspondem ao termo de pesquisa
     */
    List<Livro> findByTermo(String termo);

}
