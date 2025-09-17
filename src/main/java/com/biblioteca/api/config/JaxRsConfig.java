package com.biblioteca.api.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Classe de configuração para a API REST (JAX-RS).
 * Define o caminho base para todos os endpoints da API.
 */
@ApplicationPath("/api")
public class JaxRsConfig extends Application {
    // Nenhuma implementação é necessária aqui, a configuração é feita pela
    // anotação.
}
