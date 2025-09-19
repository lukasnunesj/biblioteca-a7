package com.biblioteca.infrastructure.exceptions;

/**
 * Exceção lançada quando ocorrem erros de validação de dados.
 * <p>
 * Esta exceção é utilizada quando os dados fornecidos pelo usuário ou
 * por outras partes do sistema não atendem aos requisitos de validação,
 * como campos obrigatórios não preenchidos, formatos inválidos ou valores
 * fora dos limites aceitáveis. Estende BibliotecaException e define
 * automaticamente o tipo de erro como ERRO_VALIDACAO.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class ValidacaoException extends BibliotecaException {
    
    /**
     * Construtor que recebe apenas a mensagem de erro.
     * O tipo de erro é automaticamente definido como ERRO_VALIDACAO.
     *
     * @param mensagem a mensagem de erro de validação
     */
    public ValidacaoException(String mensagem) {
        super(mensagem, TipoErro.ERRO_VALIDACAO);
    }
    
    /**
     * Construtor que recebe a mensagem de erro e a causa.
     * O tipo de erro é automaticamente definido como ERRO_VALIDACAO.
     *
     * @param mensagem a mensagem de erro de validação
     * @param causa a exceção que causou este erro de validação
     */
    public ValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, causa, TipoErro.ERRO_VALIDACAO);
    }
}
