package com.biblioteca.application.livro.service;

import com.biblioteca.domain.entities.livro.DTO.OpenLibraryResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ejb.Stateless;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Serviço para integração com a API OpenLibrary
 */
@Stateless
public class OpenLibraryService {

    private static final Logger LOGGER = Logger.getLogger(OpenLibraryService.class.getName());
    private static final String OPENLIBRARY_BASE_URL = "https://openlibrary.org";
    private static final String API_ENDPOINT = "/api/books?bibkeys=ISBN:%s&format=json&jscmd=data";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenLibraryService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Busca informações de um livro pelo ISBN na API OpenLibrary
     */
    public Optional<OpenLibraryResponseDTO> buscarLivroPorIsbn(String isbn) {
        try {
            String url = OPENLIBRARY_BASE_URL + String.format(API_ENDPOINT, isbn);
            LOGGER.info("Buscando livro na OpenLibrary: " + url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // A resposta é um objeto com a chave "ISBN:...". Precisamos extrair o valor.
                String responseBody = response.body();
                if (responseBody == null || responseBody.trim().equals("{}")) {
                    LOGGER.warning("Resposta vazia da OpenLibrary para ISBN: " + isbn);
                    return Optional.empty();
                }

                var jsonNode = objectMapper.readTree(responseBody);
                var bookNode = jsonNode.get("ISBN:" + isbn);

                if (bookNode != null) {
                    OpenLibraryResponseDTO bookData = objectMapper.treeToValue(bookNode, OpenLibraryResponseDTO.class);
                    return Optional.of(bookData);
                }
                return Optional.empty();

            } else if (response.statusCode() == 404) {
                LOGGER.warning("Livro não encontrado na OpenLibrary para ISBN: " + isbn);
                return Optional.empty();
            } else {
                LOGGER.warning("Erro ao buscar livro na OpenLibrary. Status: " + response.statusCode());
                return Optional.empty();
            }

        } catch (IOException | InterruptedException e) {
            LOGGER.severe("Erro ao comunicar com OpenLibrary: " + e.getMessage());
            return Optional.empty();
        }
    }

}
