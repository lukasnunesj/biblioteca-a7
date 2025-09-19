package com.biblioteca.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * Configuração do ObjectMapper para serialização/deserialização JSON na API.
 * <p>
 * Esta classe configura o ObjectMapper do Jackson para lidar corretamente com
 * datas e outros tipos de dados em conversões JSON.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
@Provider
public class ObjectMapperConfig implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    /**
     * Construtor que inicializa e configura o ObjectMapper.
     * Registra o módulo JavaTimeModule para suporte a classes do Java 8 Date/Time API
     * e configura para não serializar datas como timestamps.
     */
    public ObjectMapperConfig() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Retorna a instância configurada do ObjectMapper.
     *
     * @param type o tipo de classe para o qual o contexto é solicitado (não utilizado)
     * @return a instância configurada do ObjectMapper
     */
    @Override
    public ObjectMapper getContext(Class<?> type) {
        return objectMapper;
    }
}
