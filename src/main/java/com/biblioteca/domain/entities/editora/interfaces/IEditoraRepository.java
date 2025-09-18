package com.biblioteca.domain.entities.editora.interfaces;

import java.util.Optional;

import com.biblioteca.domain.entities.common.interfaces.IBaseRepository;
import com.biblioteca.domain.entities.editora.Editora;

/**
 * Interface para o repositório de editoras.
 * Define operações básicas de persistência para a entidade Editora.
 */
public interface IEditoraRepository extends IBaseRepository<Editora, Long> {

    /**
     * Busca uma editora pelo seu CNPJ.
     * 
     * @param cnpj o CNPJ da editora a ser buscada
     * @return um Optional contendo a editora, se encontrada, ou vazio caso
     *         contrário
     */
    Optional<Editora> buscarPorCnpj(String cnpj);

}
