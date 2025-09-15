package com.biblioteca.domain.entities.autor;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import com.biblioteca.domain.entities.livro.Livro;
import java.util.HashSet;
import java.util.Set;

public class AutorTest {
    
    private Autor autor;
    private final String NOME = "Carlos Drummond";
    private final String CPFCNPJ = "123.456.789-00";
    private final String TELEFONE = "(31) 99999-9999";
    private final String EMAIL = "carlos@exemplo.com";
    
    @Before
    public void setUp() {
        autor = new Autor(NOME, CPFCNPJ, TELEFONE, EMAIL);
    }
    
    @Test
    public void testConstrutor() {
        assertEquals(NOME, autor.getNome());
        assertEquals(CPFCNPJ, autor.getCpfcnpj());
        assertEquals(TELEFONE, autor.getTelefone());
        assertEquals(EMAIL, autor.getEmail());
        assertNotNull(autor.getLivros());
        assertTrue(autor.getLivros().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorNomeNulo() {
        new Autor(null, CPFCNPJ, TELEFONE, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorNomeVazio() {
        new Autor("", CPFCNPJ, TELEFONE, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorCpfCnpjNulo() {
        new Autor(NOME, null, TELEFONE, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorCpfCnpjVazio() {
        new Autor(NOME, "", TELEFONE, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorTelefoneNulo() {
        new Autor(NOME, CPFCNPJ, null, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorTelefoneVazio() {
        new Autor(NOME, CPFCNPJ, "", EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorEmailNulo() {
        new Autor(NOME, CPFCNPJ, TELEFONE, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorEmailVazio() {
        new Autor(NOME, CPFCNPJ, TELEFONE, "");
    }
    
    @Test
    public void testSetId() {
        Long id = 1L;
        autor.setId(id);
        assertEquals(id, autor.getId());
    }
    
    @Test
    public void testSetNome() {
        String novoNome = "Machado de Assis";
        autor.setNome(novoNome);
        assertEquals(novoNome, autor.getNome());
    }
    
    @Test
    public void testSetCpfcnpj() {
        String novoCpfCnpj = "987.654.321-00";
        autor.setCpfcnpj(novoCpfCnpj);
        assertEquals(novoCpfCnpj, autor.getCpfcnpj());
    }
    
    @Test
    public void testSetTelefone() {
        String novoTelefone = "(21) 88888-8888";
        autor.setTelefone(novoTelefone);
        assertEquals(novoTelefone, autor.getTelefone());
    }
    
    @Test
    public void testSetEmail() {
        String novoEmail = "machado@exemplo.com";
        autor.setEmail(novoEmail);
        assertEquals(novoEmail, autor.getEmail());
    }
    
    @Test
    public void testAddLivro() {
        Livro livro = new Livro();
        autor.addLivro(livro);
        assertTrue(autor.getLivros().contains(livro));
        assertEquals(1, autor.getLivros().size());
    }
    
    @Test
    public void testRemoveLivro() {
        Livro livro = new Livro();
        autor.addLivro(livro);
        autor.removeLivro(livro);
        assertFalse(autor.getLivros().contains(livro));
        assertEquals(0, autor.getLivros().size());
    }
    
    @Test
    public void testSetLivros() {
        Set<Livro> livros = new HashSet<>();
        Livro livro1 = new Livro();
        Livro livro2 = new Livro();
        livros.add(livro1);
        livros.add(livro2);
        
        autor.setLivros(livros);
        assertEquals(livros, autor.getLivros());
        assertEquals(2, autor.getLivros().size());
    }
    
    @Test
    public void testToString() {
        String expected = "Autor [id=" + autor.getId() + ", nome=" + NOME + ", telefone=" + TELEFONE + ", email=" + EMAIL + "]";
        assertEquals(expected, autor.toString());
    }
    
    @Test
    public void testEquals() {
        Autor autorIgual = new Autor(NOME, CPFCNPJ, TELEFONE, EMAIL);
        autorIgual.setId(1L);
        autor.setId(1L);
        
        assertTrue(autor.equals(autor));
        assertTrue(autor.equals(autorIgual));
        assertFalse(autor.equals(null));
        assertFalse(autor.equals(new Object()));
        
        Autor autorDiferente = new Autor(NOME, "999.888.777-66", TELEFONE, EMAIL);
        autorDiferente.setId(2L);
        assertFalse(autor.equals(autorDiferente));
    }
}
