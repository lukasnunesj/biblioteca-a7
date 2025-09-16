package com.biblioteca.domain.entities.editora.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.editora.Editora;

/**
 * Interface para o repositório de editoras.
 * Define operações básicas de persistência para a entidade Editora.
 */
public interface IEditoraRepository {

    /**
     * Salva uma editora no repositório.
     * 
     * @param editora a editora a ser salva
     * @return a editora salva com possíveis atualizações (como ID gerado)
     */
    Editora salvar(Editora editora);

    /**
     * Busca uma editora pelo seu identificador único.
     * 
     * @param id o ID da editora a ser buscada
     * @return um Optional contendo a editora, se encontrada, ou vazio caso
     *         contrário
     */
    Optional<Editora> buscarPorId(Long id);

    /**
     * Busca uma editora pelo seu CNPJ.
     * 
     * @param cnpj o CNPJ da editora a ser buscada
     * @return um Optional contendo a editora, se encontrada, ou vazio caso
     *         contrário
     */
    Optional<Editora> buscarPorCnpj(String cnpj);

    /**
     * Retorna todas as editoras cadastradas no repositório.
     * 
     * @return uma lista contendo todas as editoras
     */
    List<Editora> buscarTodos();

    /**
     * Remove uma editora do repositório.
     * 
     * @param editora a editora a ser removida
     */
    void remover(Editora editora);
}
