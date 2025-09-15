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
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarAutorComCpfcnpjNulo() {
        new Autor("Carlos Drummond", null, "(31) 99999-9999", "carlos@exemplo.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarAutorComCpfcnpjVazio() {
        new Autor("Carlos Drummond", "", "(31) 99999-9999", "carlos@exemplo.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarAutorComTelefoneNulo() {
        new Autor("Carlos Drummond", "123.456.789-00", null, "carlos@exemplo.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarAutorComTelefoneVazio() {
        new Autor("Carlos Drummond", "123.456.789-00", "", "carlos@exemplo.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarAutorComEmailNulo() {
        new Autor("Carlos Drummond", "123.456.789-00", "(31) 99999-9999", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarAutorComEmailVazio() {
        new Autor("Carlos Drummond", "123.456.789-00", "(31) 99999-9999", "");
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
