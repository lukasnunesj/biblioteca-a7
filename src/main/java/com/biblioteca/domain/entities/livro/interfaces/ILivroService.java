package com.biblioteca.domain.entities.livro.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;

/**
 * Interface para o serviço de livros.
 * Define operações de negócio relacionadas à entidade Livro.
 */
public interface ILivroService {
    /**
     * Salva um livro a partir dos dados do DTO.
     * 
     * @param livroDTO o DTO contendo os dados do livro a ser salvo
     * @return o livro salvo com possíveis atualizações (como ID gerado)
     */
    Livro salvar(LivroDTO livroDTO);

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
     * Retorna todos os livros cadastrados no sistema.
     * 
     * @return uma lista contendo todos os livros
     */
    List<Livro> buscarTodos();

    /**
     * Remove um livro do sistema a partir dos dados do DTO.
     * 
     * @param livroDTO o DTO contendo os dados do livro a ser removido
     */
    void remover(LivroDTO livroDTO);

    /**
     * Cadastra um livro buscando informações pelo ISBN na OpenLibrary.
     * 
     * @param isbn o ISBN do livro a ser cadastrado
     * @return um Optional contendo o livro cadastrado, se encontrado na API, ou vazio caso contrário
     */
    Optional<Livro> cadastrarPorIsbn(String isbn);
}
