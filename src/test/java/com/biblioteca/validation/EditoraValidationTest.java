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
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarEditoraComCnpjNulo() {
        new Editora("Companhia das Letras", null, "(11) 99999-9999", "contato@companhiadasletras.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarEditoraComCnpjVazio() {
        new Editora("Companhia das Letras", "", "(11) 99999-9999", "contato@companhiadasletras.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarEditoraComTelefoneNulo() {
        new Editora("Companhia das Letras", "12345678901234", null, "contato@companhiadasletras.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarEditoraComTelefoneVazio() {
        new Editora("Companhia das Letras", "12345678901234", "", "contato@companhiadasletras.com");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarEditoraComEmailNulo() {
        new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarEditoraComEmailVazio() {
        new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999", "");
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
