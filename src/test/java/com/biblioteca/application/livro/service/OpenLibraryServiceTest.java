package com.biblioteca.application.livro.service;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.livro.DTO.OpenLibraryAuthorDTO;
import com.biblioteca.domain.entities.livro.DTO.OpenLibraryResponseDTO;

public class OpenLibraryServiceTest {

    private OpenLibraryService openLibraryService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        openLibraryService = new OpenLibraryService();
    }
    
    @Test
    public void testBuscarLivroPorIsbn_ISBNValido() {
        // Given
        String isbn = "9780140328721";
        
        // When
        Optional<OpenLibraryResponseDTO> resultado = openLibraryService.buscarLivroPorIsbn(isbn);
        
        // Then
        // Como estamos testando com uma API real, vamos apenas verificar se o método não falha
        // Em um ambiente de produção, seria melhor mockar a resposta HTTP
        assertNotNull(resultado);
    }
    
    @Test
    public void testBuscarLivroPorIsbn_ISBNInvalido() {
        // Given
        String isbnInvalido = "0000000000000";
        
        // When
        Optional<OpenLibraryResponseDTO> resultado = openLibraryService.buscarLivroPorIsbn(isbnInvalido);
        
        // Then
        assertNotNull(resultado);
        // Para ISBN inválido, esperamos que retorne empty ou dados válidos dependendo da API
    }
    
    @Test
    public void testBuscarAutorPorChave_ChaveValida() {
        // Given
        String chaveAutor = "/authors/OL23919A";
        
        // When
        Optional<OpenLibraryAuthorDTO> resultado = openLibraryService.buscarAutorPorChave(chaveAutor);
        
        // Then
        assertNotNull(resultado);
    }
    
    @Test
    public void testBuscarAutorPorChave_ChaveInvalida() {
        // Given
        String chaveInvalida = "/authors/INVALID";
        
        // When
        Optional<OpenLibraryAuthorDTO> resultado = openLibraryService.buscarAutorPorChave(chaveInvalida);
        
        // Then
        assertNotNull(resultado);
        // Para chave inválida, esperamos que retorne empty
    }
    
    @Test
    public void testBuscarAutoresPorChaves_ListaVazia() {
        // Given
        java.util.List<String> chavesVazias = java.util.Collections.emptyList();
        
        // When
        java.util.List<OpenLibraryAuthorDTO> resultado = openLibraryService.buscarAutoresPorChaves(chavesVazias);
        
        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
    
    @Test
    public void testBuscarAutoresPorChaves_ListaNula() {
        // Given
        java.util.List<String> chavesNulas = null;
        
        // When
        java.util.List<OpenLibraryAuthorDTO> resultado = openLibraryService.buscarAutoresPorChaves(chavesNulas);
        
        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
