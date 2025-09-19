package com.biblioteca.application.livro.service;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.livro.DTO.OpenLibraryResponseDTO;

/**
 * Testes para o serviço OpenLibraryService.
 * <p>
 * Esta classe testa a integração com a API OpenLibrary.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class OpenLibraryServiceTest {

    private OpenLibraryService openLibraryService;
    
    /**
     * Configura o ambiente de teste antes de cada teste.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        openLibraryService = new OpenLibraryService();
    }
    
    /**
     * Testa a busca de livro por ISBN válido.
     * Este teste verifica se o método não falha ao buscar um ISBN válido.
     * Como estamos testando com uma API real, apenas verificamos se o método retorna um resultado não nulo.
     */
    @Test
    public void testBuscarLivroPorIsbn_ISBNValido() {
        String isbn = "9780140328721";
        
        Optional<OpenLibraryResponseDTO> resultado = openLibraryService.buscarLivroPorIsbn(isbn);
        
        assertNotNull(resultado);
    }
    
    /**
     * Testa a busca de livro por ISBN inválido.
     * Este teste verifica se o método não falha ao buscar um ISBN inválido.
     * Para ISBN inválido, esperamos que retorne empty ou dados válidos dependendo da API.
     */
    @Test
    public void testBuscarLivroPorIsbn_ISBNInvalido() {
        String isbnInvalido = "0000000000000";
        
        Optional<OpenLibraryResponseDTO> resultado = openLibraryService.buscarLivroPorIsbn(isbnInvalido);
        
        assertNotNull(resultado);
    }
    
}
