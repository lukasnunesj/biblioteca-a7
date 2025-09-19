package com.biblioteca.infrastructure.exceptions;

/**
 * Exceção base para todas as exceções específicas da aplicação Biblioteca.
 * <p>
 * Esta classe estende RuntimeException e adiciona um campo para identificar o tipo
 * de erro ocorrido, facilitando o tratamento adequado pelos manipuladores de exceção.
 * Todas as exceções específicas da aplicação devem estender esta classe.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class BibliotecaException extends RuntimeException {
    
    /**
     * O tipo de erro associado a esta exceção.
     */
    private final TipoErro tipoErro;
    
    /**
     * Construtor que recebe apenas a mensagem de erro.
     * O tipo de erro é definido como ERRO_GENERICO por padrão.
     *
     * @param mensagem a mensagem de erro
     */
    public BibliotecaException(String mensagem) {
        super(mensagem);
        this.tipoErro = TipoErro.ERRO_GENERICO;
    }
    
    /**
     * Construtor que recebe a mensagem de erro e a causa.
     * O tipo de erro é definido como ERRO_GENERICO por padrão.
     *
     * @param mensagem a mensagem de erro
     * @param causa a exceção que causou este erro
     */
    public BibliotecaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.tipoErro = TipoErro.ERRO_GENERICO;
    }
    
    /**
     * Construtor que recebe a mensagem de erro e o tipo de erro.
     *
     * @param mensagem a mensagem de erro
     * @param tipoErro o tipo de erro associado a esta exceção
     */
    public BibliotecaException(String mensagem, TipoErro tipoErro) {
        super(mensagem);
        this.tipoErro = tipoErro;
    }
    
    /**
     * Construtor que recebe a mensagem de erro, a causa e o tipo de erro.
     *
     * @param mensagem a mensagem de erro
     * @param causa a exceção que causou este erro
     * @param tipoErro o tipo de erro associado a esta exceção
     */
    public BibliotecaException(String mensagem, Throwable causa, TipoErro tipoErro) {
        super(mensagem, causa);
        this.tipoErro = tipoErro;
    }
    
    /**
     * Retorna o tipo de erro associado a esta exceção.
     *
     * @return o tipo de erro
     */
    public TipoErro getTipoErro() {
        return tipoErro;
    }
}
