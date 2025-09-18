package com.biblioteca.domain.entities.livro;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.editora.Editora;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

public class LivroTest {
    
    private Livro livro;
    private final String TITULO = "Dom Casmurro";
    private final String ISBN = "9788574801414";
    private final Integer DATA_PUBLICACAO = 1899;
    
    @Before
    public void setUp() {
        livro = new Livro(TITULO, ISBN, DATA_PUBLICACAO);
    }
    
    @Test
    public void testConstrutor() {
        assertEquals(TITULO, livro.getTitulo());
        assertEquals(ISBN, livro.getIsbn());
        assertEquals(DATA_PUBLICACAO, livro.getDataPublicacao());
        assertNotNull(livro.getAutores());
        assertTrue(livro.getAutores().isEmpty());
        assertNotNull(livro.getLivrosSemelhantes());
        assertTrue(livro.getLivrosSemelhantes().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorTituloNulo() {
        new Livro(null, ISBN, DATA_PUBLICACAO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorTituloVazio() {
        new Livro("", ISBN, DATA_PUBLICACAO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorIsbnInvalido() {
        new Livro(TITULO, "123", DATA_PUBLICACAO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorIsbnNulo() {
        new Livro(TITULO, null, DATA_PUBLICACAO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorDataFutura() {
        Integer dataFutura = Year.now().getValue() + 1;
        new Livro(TITULO, ISBN, dataFutura);
    }
    
    @Test
    public void testIsbnValido10Digitos() {
        Livro livroIsbn10 = new Livro(TITULO, "1234567890", DATA_PUBLICACAO);
        assertEquals("1234567890", livroIsbn10.getIsbn());
    }
    
    @Test
    public void testIsbnValido13Digitos() {
        Livro livroIsbn13 = new Livro(TITULO, "1234567890123", DATA_PUBLICACAO);
        assertEquals("1234567890123", livroIsbn13.getIsbn());
    }
    
    @Test
    public void testSetId() {
        Long id = 1L;
        livro.setId(id);
        assertEquals(id, livro.getId());
    }
    
    @Test
    public void testSetTitulo() {
        String novoTitulo = "Memórias Póstumas de Brás Cubas";
        livro.setTitulo(novoTitulo);
        assertEquals(novoTitulo, livro.getTitulo());
    }
    
    @Test
    public void testSetIsbn() {
        String novoIsbn = "9788535921182";
        livro.setIsbn(novoIsbn);
        assertEquals(novoIsbn, livro.getIsbn());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetIsbnInvalido() {
        livro.setIsbn("123");
    }
    
    @Test
    public void testSetDataPublicacao() {
        Integer novaData = 1881;
        livro.setDataPublicacao(novaData);
        assertEquals(novaData, livro.getDataPublicacao());
    }
    
    @Test
    public void testSetEditora() {
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999", "contato@companhia.com");
        livro.setEditora(editora);
        assertEquals(editora, livro.getEditora());
    }
    
    @Test
    public void testAddAutor() {
        Autor autor = new Autor("Machado de Assis", "123.456.789-00", "(21) 99999-9999", "machado@exemplo.com");
        livro.addAutor(autor);
        assertTrue(livro.getAutores().contains(autor));
        assertEquals(1, livro.getAutores().size());
    }
    
    @Test
    public void testRemoveAutor() {
        Autor autor = new Autor("Machado de Assis", "123.456.789-00", "(21) 99999-9999", "machado@exemplo.com");
        livro.addAutor(autor);
        livro.removeAutor(autor);
        assertFalse(livro.getAutores().contains(autor));
        assertEquals(0, livro.getAutores().size());
    }
    
    @Test
    public void testSetAutores() {
        Set<Autor> autores = new HashSet<>();
        Autor autor1 = new Autor("Machado de Assis", "123.456.789-00", "(21) 99999-9999", "machado@exemplo.com");
        Autor autor2 = new Autor("José de Alencar", "987.654.321-00", "(85) 88888-8888", "alencar@exemplo.com");
        autores.add(autor1);
        autores.add(autor2);
        
        livro.setAutores(autores);
        assertEquals(autores, livro.getAutores());
        assertEquals(2, livro.getAutores().size());
    }
    
    @Test
    public void testAddLivroSemelhante() {
        Livro livroSemelhante = new Livro("Quincas Borba", "9788535921199", 1891);
        livro.addLivroSemelhante(livroSemelhante);
        assertTrue(livro.getLivrosSemelhantes().contains(livroSemelhante));
        assertEquals(1, livro.getLivrosSemelhantes().size());
    }
    
    @Test
    public void testRemoveLivroSemelhante() {
        Livro livroSemelhante = new Livro("Quincas Borba", "9788535921199", 1891);
        livro.addLivroSemelhante(livroSemelhante);
        livro.removeLivroSemelhante(livroSemelhante);
        assertFalse(livro.getLivrosSemelhantes().contains(livroSemelhante));
        assertEquals(0, livro.getLivrosSemelhantes().size());
    }
    
    @Test
    public void testSetLivrosSemelhantes() {
        Set<Livro> livrosSemelhantes = new HashSet<>();
        Livro livro1 = new Livro("Quincas Borba", "9788535921199", 1891);
        Livro livro2 = new Livro("Memórias Póstumas de Brás Cubas", "9788535921182", 1881);
        livrosSemelhantes.add(livro1);
        livrosSemelhantes.add(livro2);
        
        livro.setLivrosSemelhantes(livrosSemelhantes);
        assertEquals(livrosSemelhantes, livro.getLivrosSemelhantes());
        assertEquals(2, livro.getLivrosSemelhantes().size());
    }
    
    @Test
    public void testToString() {
        String expected = "Livro [id=" + livro.getId() + ", titulo=" + TITULO + ", isbn=" + ISBN + ", dataPublicacao=" + DATA_PUBLICACAO + "]";
        assertEquals(expected, livro.toString());
    }
    
    @Test
    public void testEquals() {
        Livro livroIgual = new Livro(TITULO, ISBN, DATA_PUBLICACAO);
        livroIgual.setId(1L);
        livro.setId(1L);
        
        assertTrue(livro.equals(livro));
        assertTrue(livro.equals(livroIgual));
        assertFalse(livro.equals(null));
        assertFalse(livro.equals(new Object()));
        
        Livro livroDiferente = new Livro("Outro Livro", "9788535921182", DATA_PUBLICACAO);
        livroDiferente.setId(2L);
        assertFalse(livro.equals(livroDiferente));
    }
}
