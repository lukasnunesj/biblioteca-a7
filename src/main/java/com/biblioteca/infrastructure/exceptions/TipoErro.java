package com.biblioteca.infrastructure.exceptions;

/**
 * Enum que define os tipos de erro da aplicação.
 * <p>
 * Facilita a categorização e tratamento adequado de cada tipo de erro.
 * Utilizado pelas exceções da aplicação para indicar a natureza do erro
 * e permitir que o ExceptionHandler exiba mensagens apropriadas.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public enum TipoErro {
    /**
     * Erro genérico, utilizado quando o erro não se encaixa em nenhuma outra categoria.
     */
    ERRO_GENERICO,
    
    /**
     * Erro de validação de dados, como campos obrigatórios não preenchidos ou formatos inválidos.
     */
    ERRO_VALIDACAO,
    
    /**
     * Erro relacionado à persistência de dados, como falhas de conexão com o banco de dados.
     */
    ERRO_PERSISTENCIA,
    
    /**
     * Erro relacionado às regras de negócio da aplicação.
     */
    ERRO_NEGOCIO,
    
    /**
     * Erro de autenticação, como credenciais inválidas.
     */
    ERRO_AUTENTICACAO,
    
    /**
     * Erro de autorização, quando o usuário não tem permissão para acessar um recurso.
     */
    ERRO_AUTORIZACAO,
    
    /**
     * Erro quando um recurso solicitado não é encontrado no sistema.
     */
    RECURSO_NAO_ENCONTRADO,
    
    /**
     * Erro de integração com sistemas externos.
     */
    ERRO_INTEGRACAO
}
