package com.biblioteca.domain.entities.common.DTO;

import java.io.Serializable;

/**
 * Classe base para todos os DTOs da aplicação.
 * Fornece funcionalidades comuns a todos os DTOs.
 */
public abstract class BaseDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    public BaseDTO() {
    }
    
    public BaseDTO(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
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
