package com.biblioteca.infrastructure.exceptions;

/**
 * Exceção lançada quando ocorrem erros de persistência de dados.
 * <p>
 * Esta exceção é utilizada para encapsular erros que ocorrem durante
 * operações de acesso a dados, como salvar, atualizar, buscar ou excluir
 * entidades no banco de dados. Estende BibliotecaException e define
 * automaticamente o tipo de erro como ERRO_PERSISTENCIA.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class PersistenciaException extends BibliotecaException {
    
    /**
     * Construtor que recebe apenas a mensagem de erro.
     * O tipo de erro é automaticamente definido como ERRO_PERSISTENCIA.
     *
     * @param mensagem a mensagem de erro
     */
    public PersistenciaException(String mensagem) {
        super(mensagem, TipoErro.ERRO_PERSISTENCIA);
    }
    
    /**
     * Construtor que recebe a mensagem de erro e a causa.
     * O tipo de erro é automaticamente definido como ERRO_PERSISTENCIA.
     *
     * @param mensagem a mensagem de erro
     * @param causa a exceção que causou este erro de persistência
     */
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa, TipoErro.ERRO_PERSISTENCIA);
    }
}
