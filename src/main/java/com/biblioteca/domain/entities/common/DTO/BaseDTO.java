package com.biblioteca.domain.entities.common.DTO;

import java.io.Serializable;

/**
 * Classe base para todos os DTOs (Data Transfer Objects) da aplicação.
 * <p>
 * Fornece funcionalidades comuns a todos os DTOs, como identificação e validação.
 * Todas as classes DTO da aplicação devem estender esta classe para garantir
 * um comportamento consistente na transferência de dados entre camadas.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public abstract class BaseDTO implements Serializable {
    
    /**
     * Número de serialização para garantir compatibilidade entre versões.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Identificador único do objeto.
     */
    private Long id;
    
    /**
     * Construtor padrão.
     */
    public BaseDTO() {
    }
    
    /**
     * Construtor que inicializa o DTO com um ID.
     *
     * @param id o identificador único do objeto
     */
    public BaseDTO(Long id) {
        this.id = id;
    }
    
    /**
     * Retorna o identificador único do objeto.
     *
     * @return o ID do objeto
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Define o identificador único do objeto.
     *
     * @param id o novo ID do objeto
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Método para validar os dados do DTO.
     * Deve ser implementado pelas subclasses para realizar validações específicas.
     * 
     * @throws IllegalArgumentException se os dados forem inválidos
     */
    public abstract void validar();
}
