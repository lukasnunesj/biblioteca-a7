package com.biblioteca.validation;

import static org.junit.Assert.*;

import org.junit.Test;

import com.biblioteca.domain.entities.editora.Editora;

public class EditoraValidationTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarEditoraComNomeNulo() {
        new Editora(null, "12345678901234", "(11) 99999-9999", "contato@companhiadasletras.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarEditoraComNomeVazio() {
        new Editora("", "12345678901234", "(11) 99999-9999", "contato@companhiadasletras.com");
    }
    
    
    
    
    
    
    
    @Test
    public void testCriarEditoraComCnpjNulo() {
        Editora editora = new Editora("Editora Sem Documento", null, "(11) 12345-6789", "semcnpj@exemplo.com");
        assertNotNull(editora);
        assertNull(editora.getCnpj());
    }

    @Test
    public void testCriarEditoraValida() {
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999", "contato@companhiadasletras.com");
        
        assertNotNull(editora);
        assertEquals("Companhia das Letras", editora.getNome());
        assertEquals("12345678901234", editora.getCnpj());
        assertEquals("(11) 99999-9999", editora.getTelefone());
        assertEquals("contato@companhiadasletras.com", editora.getEmail());
    }
}
