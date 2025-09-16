package com.biblioteca.infrastructure.exceptions;

/**
 * Exceção lançada quando ocorrem erros de validação de dados.
 */
public class ValidacaoException extends BibliotecaException {
    
    public ValidacaoException(String mensagem) {
        super(mensagem, TipoErro.ERRO_VALIDACAO);
    }
    
    public ValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa, TipoErro.ERRO_VALIDACAO);
    }
}
