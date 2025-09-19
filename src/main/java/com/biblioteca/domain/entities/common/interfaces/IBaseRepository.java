package com.biblioteca.domain.entities.common.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Interface base para todos os repositórios da aplicação.
 * <p>
 * Define operações comuns de persistência que todos os repositórios
 * devem implementar, como salvar, buscar, listar, excluir e contar entidades.
 * Fornece um contrato genérico para acesso a dados independente da implementação.
 * </p>
 * 
 * @param <T> Tipo da entidade gerenciada pelo repositório
 * @param <ID> Tipo do identificador único da entidade
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public interface IBaseRepository<T, ID> {
    
    /**
     * Salva uma entidade.
     * 
     * @param entity Entidade a ser salva
     * @return Entidade salva
     */
    T save(T entity);
    
    /**
     * Busca uma entidade pelo seu identificador.
     * 
     * @param id Identificador da entidade
     * @return Optional contendo a entidade, se encontrada
     */
    Optional<T> findById(ID id);
    
    /**
     * Busca todas as entidades.
     * 
     * @return Lista de entidades
     */
    List<T> findAll();
    
    /**
     * Exclui uma entidade.
     * 
     * @param entity Entidade a ser excluída
     */
    void delete(T entity);
    
    /**
     * Exclui uma entidade pelo seu identificador.
     * 
     * @param id Identificador da entidade a ser excluída
     */
    void deleteById(ID id);
    
    /**
     * Verifica se existe uma entidade com o identificador especificado.
     * 
     * @param id Identificador da entidade
     * @return true se a entidade existir, false caso contrário
     */
    boolean existsById(ID id);
    
    /**
     * Conta o número de entidades.
     * 
     * @return Número de entidades
     */
    long count();
}
