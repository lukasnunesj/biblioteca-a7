package com.biblioteca.infrastructure.exceptions;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 */
public class RecursoNaoEncontradoException extends BibliotecaException {
    
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem, TipoErro.RECURSO_NAO_ENCONTRADO);
    }
    
    public RecursoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa, TipoErro.RECURSO_NAO_ENCONTRADO);
    }
    
    public RecursoNaoEncontradoException(Class<?> tipoRecurso, Object identificador) {
        super(String.format("%s com identificador %s não encontrado", 
                tipoRecurso.getSimpleName(), identificador.toString()), 
                TipoErro.RECURSO_NAO_ENCONTRADO);
    }
}
