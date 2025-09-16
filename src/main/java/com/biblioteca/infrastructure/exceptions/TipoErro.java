package com.biblioteca.infrastructure.exceptions;

/**
 * Enum que define os tipos de erro da aplicação.
 * Facilita a categorização e tratamento adequado de cada tipo de erro.
 */
public enum TipoErro {
    ERRO_GENERICO,
    ERRO_VALIDACAO,
    ERRO_PERSISTENCIA,
    ERRO_NEGOCIO,
    ERRO_AUTENTICACAO,
    ERRO_AUTORIZACAO,
    RECURSO_NAO_ENCONTRADO,
    ERRO_INTEGRACAO
}
