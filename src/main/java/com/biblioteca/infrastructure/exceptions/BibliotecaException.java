package com.biblioteca.infrastructure.exceptions;

/**
 * Exceção base para todas as exceções específicas da aplicação Biblioteca.
 */
public class BibliotecaException extends RuntimeException {
    
    private final TipoErro tipoErro;
    
    public BibliotecaException(String mensagem) {
        super(mensagem);
        this.tipoErro = TipoErro.ERRO_GENERICO;
    }
    
    public BibliotecaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.tipoErro = TipoErro.ERRO_GENERICO;
    }
    
    public BibliotecaException(String mensagem, TipoErro tipoErro) {
        super(mensagem);
        this.tipoErro = tipoErro;
    }
    
    public BibliotecaException(String mensagem, Throwable causa, TipoErro tipoErro) {
        super(mensagem, causa);
        this.tipoErro = tipoErro;
    }
    
    public TipoErro getTipoErro() {
        return tipoErro;
    }
}
