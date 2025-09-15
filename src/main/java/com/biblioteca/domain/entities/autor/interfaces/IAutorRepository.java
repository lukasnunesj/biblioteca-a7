package com.biblioteca.domain.entities.autor.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.autor.Autor;

/**
 * Interface para o repositório de autores.
 * Define operações básicas de persistência para a entidade Autor.
 */
public interface IAutorRepository {

    /**
     * Salva um autor no repositório.
     * 
     * @param autor o autor a ser salvo
     * @return o autor salvo com possíveis atualizações (como ID gerado)
     */
    Autor salvar(Autor autor);

    /**
     * Busca um autor pelo seu identificador único.
     * 
     * @param id o ID do autor a ser buscado
     * @return um Optional contendo o autor, se encontrado, ou vazio caso contrário
     */
    Optional<Autor> buscarPorId(Long id);

    /**
     * Busca um autor pelo seu CPF/CNPJ.
     * 
     * @param cpfcnpj o CPF/CNPJ do autor a ser buscado
     * @return um Optional contendo o autor, se encontrado, ou vazio caso contrário
     */
    Optional<Autor> buscarPorCpfcnpj(String cpfcnpj);

    /**
     * Retorna todos os autores cadastrados no repositório.
     * 
     * @return uma lista contendo todos os autores
     */
    List<Autor> buscarTodos();

    /**
     * Remove um autor do repositório.
     * 
     * @param autor o autor a ser removido
     */
    void remover(Autor autor);
}
