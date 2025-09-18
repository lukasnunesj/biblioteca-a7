package com.biblioteca.validation;

import static org.junit.Assert.*;

import org.junit.Test;

import com.biblioteca.domain.entities.autor.Autor;

public class AutorValidationTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarAutorComNomeNulo() {
        new Autor(null, "123.456.789-00", "(31) 99999-9999", "carlos@exemplo.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarAutorComNomeVazio() {
        new Autor("", "123.456.789-00", "(31) 99999-9999", "carlos@exemplo.com");
    }
    
    
    
    
    
    
    
    @Test
    public void testCriarAutorComCpfCnpjNulo() {
        Autor autor = new Autor("Autor Sem Documento", null, "(11) 98765-4321", "semdoc@exemplo.com");
        assertNotNull(autor);
        assertNull(autor.getCpfcnpj());
    }

    @Test
    public void testCriarAutorValido() {
        Autor autor = new Autor("Carlos Drummond", "123.456.789-00", "(31) 99999-9999", "carlos@exemplo.com");
        
        assertNotNull(autor);
        assertEquals("Carlos Drummond", autor.getNome());
        assertEquals("123.456.789-00", autor.getCpfcnpj());
        assertEquals("(31) 99999-9999", autor.getTelefone());
        assertEquals("carlos@exemplo.com", autor.getEmail());
    }
}
