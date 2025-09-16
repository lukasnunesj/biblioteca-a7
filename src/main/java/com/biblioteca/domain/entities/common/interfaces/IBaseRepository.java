package com.biblioteca.domain.entities.common.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Interface base para todos os repositórios da aplicação.
 * 
 * @param <T> Tipo da entidade
 * @param <ID> Tipo do identificador da entidade
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
