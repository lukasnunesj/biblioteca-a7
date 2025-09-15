package com.biblioteca.validation;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import com.biblioteca.domain.entities.livro.Livro;

public class LivroValidationTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarLivroComTituloNulo() {
        new Livro(null, "9788574801414", LocalDate.of(1899, 1, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarLivroComTituloVazio() {
        new Livro("", "9788574801414", LocalDate.of(1899, 1, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarLivroComIsbnNulo() {
        new Livro("Dom Casmurro", null, LocalDate.of(1899, 1, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarLivroComIsbnInvalido() {
        new Livro("Dom Casmurro", "123", LocalDate.of(1899, 1, 1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCriarLivroComDataFutura() {
        LocalDate dataFutura = LocalDate.now().plusDays(1);
        new Livro("Dom Casmurro", "9788574801414", dataFutura);
    }
    
    @Test
    public void testCriarLivroComIsbn10Digitos() {
        Livro livro = new Livro("Dom Casmurro", "1234567890", LocalDate.of(1899, 1, 1));
        
        assertNotNull(livro);
        assertEquals("Dom Casmurro", livro.getTitulo());
        assertEquals("1234567890", livro.getIsbn());
    }
    
    @Test
    public void testCriarLivroComIsbn13Digitos() {
        Livro livro = new Livro("Dom Casmurro", "1234567890123", LocalDate.of(1899, 1, 1));
        
        assertNotNull(livro);
        assertEquals("Dom Casmurro", livro.getTitulo());
        assertEquals("1234567890123", livro.getIsbn());
    }
    
    @Test
    public void testCriarLivroValido() {
        Livro livro = new Livro("Dom Casmurro", "9788574801414", LocalDate.of(1899, 1, 1));
        
        assertNotNull(livro);
        assertEquals("Dom Casmurro", livro.getTitulo());
        assertEquals("9788574801414", livro.getIsbn());
        assertEquals(LocalDate.of(1899, 1, 1), livro.getDataPublicacao());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetIsbnInvalido() {
        Livro livro = new Livro("Dom Casmurro", "9788574801414", LocalDate.of(1899, 1, 1));
        livro.setIsbn("123");
    }
}
