package com.biblioteca.domain.entities.livro.DTO;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.livro.Livro;

public class LivroDTOTest {

    private final Long ID = 1L;
    private final String TITULO = "Dom Casmurro";
    private final String ISBN = "9788574801414";
    private final Integer DATA_PUBLICACAO = 1899;
    private final Long EDITORA_ID = 1L;
    private final List<Long> AUTORES_IDS = Arrays.asList(1L, 2L);

    @Test
    public void testConstrutor() {
        LivroDTO livroDTO = new LivroDTO(ID, TITULO, ISBN, DATA_PUBLICACAO, EDITORA_ID, AUTORES_IDS, new java.util.ArrayList<>());

        assertEquals(ID, livroDTO.getId());
        assertEquals(TITULO, livroDTO.getTitulo());
        assertEquals(ISBN, livroDTO.getIsbn());
        assertEquals(DATA_PUBLICACAO, livroDTO.getDataPublicacao());
        assertEquals(EDITORA_ID, livroDTO.getEditoraId());
        assertEquals(AUTORES_IDS, livroDTO.getAutoresIds());
    }

    @Test
    public void testToEntity() {
        LivroDTO livroDTO = new LivroDTO(ID, TITULO, ISBN, DATA_PUBLICACAO, EDITORA_ID, AUTORES_IDS, new java.util.ArrayList<>());

        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhia.com");
        editora.setId(EDITORA_ID);

        HashSet<Autor> autores = new HashSet<>();
        Autor autor1 = new Autor("Machado de Assis", "123.456.789-00", "(21) 99999-9999", "machado@exemplo.com");
        autor1.setId(1L);
        Autor autor2 = new Autor("José de Alencar", "987.654.321-00", "(85) 88888-8888", "alencar@exemplo.com");
        autor2.setId(2L);
        autores.add(autor1);
        autores.add(autor2);

        Livro livro = livroDTO.toEntity(editora, autores);

        assertEquals(ID, livro.getId());
        assertEquals(TITULO, livro.getTitulo());
        assertEquals(ISBN, livro.getIsbn());
        assertEquals(DATA_PUBLICACAO, livro.getDataPublicacao());
        assertEquals(editora, livro.getEditora());
        assertEquals(autores, livro.getAutores());
    }

    @Test
    public void testSettersAndGetters() {
        LivroDTO livroDTO = new LivroDTO(ID, TITULO, ISBN, DATA_PUBLICACAO, EDITORA_ID, AUTORES_IDS, new java.util.ArrayList<>());

        Long novoId = 2L;
        String novoTitulo = "Memórias Póstumas de Brás Cubas";
        String novoIsbn = "9788535921182";
        Integer novaData = 1881;
        Long novaEditoraId = 3L;
        List<Long> novosAutoresIds = Arrays.asList(3L, 4L);

        livroDTO.setId(novoId);
        livroDTO.setTitulo(novoTitulo);
        livroDTO.setIsbn(novoIsbn);
        livroDTO.setDataPublicacao(novaData);
        livroDTO.setEditoraId(novaEditoraId);
        livroDTO.setAutoresIds(novosAutoresIds);

        assertEquals(novoId, livroDTO.getId());
        assertEquals(novoTitulo, livroDTO.getTitulo());
        assertEquals(novoIsbn, livroDTO.getIsbn());
        assertEquals(novaData, livroDTO.getDataPublicacao());
        assertEquals(novaEditoraId, livroDTO.getEditoraId());
        assertEquals(novosAutoresIds, livroDTO.getAutoresIds());
    }
    
    @Test
    public void testFromEntityWithNullEditora() {
        // Create a Livro with null Editora
        Livro livro = new Livro(TITULO, ISBN, DATA_PUBLICACAO);
        livro.setId(ID);
        
        // Add authors
        HashSet<Autor> autores = new HashSet<>();
        Autor autor = new Autor("Machado de Assis", "123.456.789-00", "(21) 99999-9999", "machado@exemplo.com");
        autor.setId(1L);
        autores.add(autor);
        livro.setAutores(autores);
        
        // Explicitly set editora to null
        livro.setEditora(null);
        
        // Convert to DTO
        LivroDTO livroDTO = LivroDTO.fromEntity(livro);
        
        // Verify conversion worked correctly
        assertNotNull(livroDTO);
        assertEquals(ID, livroDTO.getId());
        assertEquals(TITULO, livroDTO.getTitulo());
        assertEquals(ISBN, livroDTO.getIsbn());
        assertEquals(DATA_PUBLICACAO, livroDTO.getDataPublicacao());
        assertNull(livroDTO.getEditoraId()); // Editora ID should be null
        assertEquals(1, livroDTO.getAutoresIds().size());
        assertEquals(Long.valueOf(1L), livroDTO.getAutoresIds().get(0));
    }
    
    @Test
    public void testFromEntityWithMultiplosAutores() {
        // Create a Livro with multiple authors
        Livro livro = new Livro("Grande Sertão: Veredas", "9788535921182", 1956);
        livro.setId(2L);
        
        // Create editora
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999", "contato@companhia.com");
        editora.setId(2L);
        livro.setEditora(editora);
        
        // Add multiple authors
        HashSet<Autor> autores = new HashSet<>();
        
        Autor autor1 = new Autor("Guimarães Rosa", "123.456.789-00", "(31) 99999-9999", "rosa@exemplo.com");
        autor1.setId(2L);
        autores.add(autor1);
        
        Autor autor2 = new Autor("Carlos Drummond", "987.654.321-00", "(31) 88888-8888", "drummond@exemplo.com");
        autor2.setId(3L);
        autores.add(autor2);
        
        Autor autor3 = new Autor("Clarice Lispector", "111.222.333-44", "(21) 77777-7777", "clarice@exemplo.com");
        autor3.setId(4L);
        autores.add(autor3);
        
        livro.setAutores(autores);
        
        // Convert to DTO
        LivroDTO livroDTO = LivroDTO.fromEntity(livro);
        
        // Verify conversion worked correctly
        assertNotNull(livroDTO);
        assertEquals(Long.valueOf(2L), livroDTO.getId());
        assertEquals("Grande Sertão: Veredas", livroDTO.getTitulo());
        assertEquals("9788535921182", livroDTO.getIsbn());
        assertEquals(Integer.valueOf(1956), livroDTO.getDataPublicacao());
        assertEquals(Long.valueOf(2L), livroDTO.getEditoraId());
        
        // Verify all authors were converted correctly
        assertEquals(3, livroDTO.getAutoresIds().size());
        assertTrue(livroDTO.getAutoresIds().contains(2L));
        assertTrue(livroDTO.getAutoresIds().contains(3L));
        assertTrue(livroDTO.getAutoresIds().contains(4L));
    }
}
