package com.biblioteca.infrastructure.exceptions;

/**
 * Exceção lançada quando ocorrem erros de persistência de dados.
 */
public class PersistenciaException extends BibliotecaException {
    
    public PersistenciaException(String mensagem) {
        super(mensagem, TipoErro.ERRO_PERSISTENCIA);
    }
    
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa, TipoErro.ERRO_PERSISTENCIA);
    }
}
