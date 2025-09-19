package com.biblioteca.infrastructure.exceptions;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 * <p>
 * Esta exceção é utilizada quando uma operação tenta acessar um recurso
 * (como uma entidade) que não existe no sistema. Estende BibliotecaException
 * e define automaticamente o tipo de erro como RECURSO_NAO_ENCONTRADO.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class RecursoNaoEncontradoException extends BibliotecaException {
    
    /**
     * Construtor que recebe apenas a mensagem de erro.
     * O tipo de erro é automaticamente definido como RECURSO_NAO_ENCONTRADO.
     *
     * @param mensagem a mensagem de erro
     */
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem, TipoErro.RECURSO_NAO_ENCONTRADO);
    }
    
    /**
     * Construtor que recebe a mensagem de erro e a causa.
     * O tipo de erro é automaticamente definido como RECURSO_NAO_ENCONTRADO.
     *
     * @param mensagem a mensagem de erro
     * @param causa a exceção que causou este erro
     */
    public RecursoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa, TipoErro.RECURSO_NAO_ENCONTRADO);
    }
    
    /**
     * Construtor que recebe o tipo do recurso e seu identificador.
     * Cria uma mensagem formatada indicando qual recurso não foi encontrado.
     * O tipo de erro é automaticamente definido como RECURSO_NAO_ENCONTRADO.
     *
     * @param tipoRecurso a classe do recurso não encontrado
     * @param identificador o identificador do recurso não encontrado
     */
    public RecursoNaoEncontradoException(Class<?> tipoRecurso, Object identificador) {
        super(String.format("%s com identificador %s não encontrado", 
                tipoRecurso.getSimpleName(), identificador.toString()), 
                TipoErro.RECURSO_NAO_ENCONTRADO);
    }
}
