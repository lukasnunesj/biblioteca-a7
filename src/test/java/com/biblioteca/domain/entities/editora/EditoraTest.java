package com.biblioteca.domain.entities.editora;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

import com.biblioteca.domain.entities.livro.Livro;
import java.util.HashSet;
import java.util.Set;

public class EditoraTest {
    
    private Editora editora;
    private final String NOME = "Companhia das Letras";
    private final String CNPJ = "12345678901234";
    private final String TELEFONE = "(11) 99999-9999";
    private final String EMAIL = "contato@companhiadasletras.com";
    
    @Before
    public void setUp() {
        editora = new Editora(NOME, CNPJ, TELEFONE, EMAIL);
    }
    
    @Test
    public void testConstrutor() {
        assertEquals(NOME, editora.getNome());
        assertEquals(CNPJ, editora.getCnpj());
        assertEquals(TELEFONE, editora.getTelefone());
        assertEquals(EMAIL, editora.getEmail());
        assertNotNull(editora.getLivros());
        assertTrue(editora.getLivros().isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorNomeNulo() {
        new Editora(null, CNPJ, TELEFONE, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorNomeVazio() {
        new Editora("", CNPJ, TELEFONE, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorCnpjNulo() {
        new Editora(NOME, null, TELEFONE, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorCnpjVazio() {
        new Editora(NOME, "", TELEFONE, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorTelefoneNulo() {
        new Editora(NOME, CNPJ, null, EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorTelefoneVazio() {
        new Editora(NOME, CNPJ, "", EMAIL);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorEmailNulo() {
        new Editora(NOME, CNPJ, TELEFONE, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstrutorEmailVazio() {
        new Editora(NOME, CNPJ, TELEFONE, "");
    }
    
    @Test
    public void testSetId() {
        Long id = 1L;
        editora.setId(id);
        assertEquals(id, editora.getId());
    }
    
    @Test
    public void testSetNome() {
        String novoNome = "Editora Rocco";
        editora.setNome(novoNome);
        assertEquals(novoNome, editora.getNome());
    }
    
    @Test
    public void testSetCnpj() {
        String novoCnpj = "98765432101234";
        editora.setCnpj(novoCnpj);
        assertEquals(novoCnpj, editora.getCnpj());
    }
    
    @Test
    public void testSetTelefone() {
        String novoTelefone = "(21) 88888-8888";
        editora.setTelefone(novoTelefone);
        assertEquals(novoTelefone, editora.getTelefone());
    }
    
    @Test
    public void testSetEmail() {
        String novoEmail = "contato@rocco.com.br";
        editora.setEmail(novoEmail);
        assertEquals(novoEmail, editora.getEmail());
    }
    
    @Test
    public void testAddLivro() {
        Livro livro = new Livro();
        editora.addLivro(livro);
        assertTrue(editora.getLivros().contains(livro));
        assertEquals(1, editora.getLivros().size());
    }
    
    @Test
    public void testRemoveLivro() {
        Livro livro = new Livro();
        editora.addLivro(livro);
        editora.removeLivro(livro);
        assertFalse(editora.getLivros().contains(livro));
        assertEquals(0, editora.getLivros().size());
    }
    
    @Test
    public void testSetLivros() {
        Set<Livro> livros = new HashSet<>();
        Livro livro1 = new Livro();
        Livro livro2 = new Livro();
        livros.add(livro1);
        livros.add(livro2);
        
        editora.setLivros(livros);
        assertEquals(livros, editora.getLivros());
        assertEquals(2, editora.getLivros().size());
    }
    
    @Test
    public void testToString() {
        String expected = "Editora [id=" + editora.getId() + ", nome=" + NOME + ", cnpj=" + CNPJ + ", telefone=" + TELEFONE + ", email=" + EMAIL + "]";
        assertEquals(expected, editora.toString());
    }
    
    @Test
    public void testEquals() {
        Editora editoraIgual = new Editora(NOME, CNPJ, TELEFONE, EMAIL);
        editoraIgual.setId(1L);
        editora.setId(1L);
        
        assertTrue(editora.equals(editora));
        assertTrue(editora.equals(editoraIgual));
        assertFalse(editora.equals(null));
        assertFalse(editora.equals(new Object()));
        
        Editora editoraDiferente = new Editora(NOME, "99887766554433", TELEFONE, EMAIL);
        editoraDiferente.setId(2L);
        assertFalse(editora.equals(editoraDiferente));
    }
}
