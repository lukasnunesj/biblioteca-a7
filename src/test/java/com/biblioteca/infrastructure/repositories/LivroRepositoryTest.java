package com.biblioteca.infrastructure.repositories;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.livro.Livro;

public class LivroRepositoryTest {

    private LivroRepository livroRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Livro> query;

    private Livro livro;
    private Editora editora;
    private Autor autor;

    private final Long LIVRO_ID = 1L;
    private final String TITULO = "Dom Casmurro";
    private final String ISBN = "9788574801414";
    private final LocalDate DATA_PUBLICACAO = LocalDate.of(1899, 1, 1);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        livroRepository = spy(new LivroRepository(entityManager));

        editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999", "contato@companhia.com");
        editora.setId(1L);

        autor = new Autor("Machado de Assis", "123.456.789-00", "(21) 99999-9999", "machado@exemplo.com");
        autor.setId(1L);

        Set<Autor> autores = new HashSet<>();
        autores.add(autor);

        livro = new Livro(TITULO, ISBN, DATA_PUBLICACAO);
        livro.setId(LIVRO_ID);
        livro.setEditora(editora);
        livro.setAutores(autores);
    }

    @Test
    public void testSalvar() {
        when(entityManager.merge(any(Livro.class))).thenReturn(livro);

        Livro resultado = livroRepository.save(livro);

        assertNotNull(resultado);
        assertEquals(LIVRO_ID, resultado.getId());
        assertEquals(TITULO, resultado.getTitulo());
        assertEquals(ISBN, resultado.getIsbn());
        assertEquals(DATA_PUBLICACAO, resultado.getDataPublicacao());
        assertEquals(editora, resultado.getEditora());
        assertTrue(resultado.getAutores().contains(autor));

        verify(entityManager).merge(any(Livro.class));
    }

    @Test(expected = RuntimeException.class)
    public void testSalvarComErro() {
        when(entityManager.merge(any(Livro.class))).thenThrow(new RuntimeException("Erro simulado"));

        livroRepository.save(livro);
    }

    @Test
    public void testBuscarPorId() {
        when(entityManager.find(Livro.class, LIVRO_ID)).thenReturn(livro);

        Optional<Livro> resultado = livroRepository.findById(LIVRO_ID);

        assertTrue(resultado.isPresent());
        assertEquals(livro, resultado.get());

        verify(entityManager).find(Livro.class, LIVRO_ID);
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(entityManager.find(Livro.class, LIVRO_ID)).thenReturn(null);

        Optional<Livro> resultado = livroRepository.findById(LIVRO_ID);

        assertFalse(resultado.isPresent());

        verify(entityManager).find(Livro.class, LIVRO_ID);
    }

    @Test(expected = RuntimeException.class)
    public void testBuscarPorIdComErro() {
        when(entityManager.find(Livro.class, LIVRO_ID)).thenThrow(new RuntimeException("Erro simulado"));

        livroRepository.findById(LIVRO_ID);
    }

    @Test
    public void testBuscarPorIsbn() {
        when(entityManager.createQuery("SELECT l FROM Livro l WHERE l.isbn = :isbn", Livro.class)).thenReturn(query);
        when(query.setParameter("isbn", ISBN)).thenReturn(query);
        when(query.getResultStream()).thenReturn(java.util.stream.Stream.of(livro));

        Optional<Livro> resultado = livroRepository.buscarPorIsbn(ISBN);

        assertTrue(resultado.isPresent());
        assertEquals(livro, resultado.get());

        verify(entityManager).createQuery("SELECT l FROM Livro l WHERE l.isbn = :isbn", Livro.class);
        verify(query).setParameter("isbn", ISBN);
        verify(query).getResultStream();
    }

    @Test
    public void testBuscarPorIsbnNaoEncontrado() {
        when(entityManager.createQuery("SELECT l FROM Livro l WHERE l.isbn = :isbn", Livro.class)).thenReturn(query);
        when(query.setParameter("isbn", ISBN)).thenReturn(query);
        when(query.getResultStream()).thenReturn(java.util.stream.Stream.empty());

        Optional<Livro> resultado = livroRepository.buscarPorIsbn(ISBN);

        assertFalse(resultado.isPresent());

        verify(entityManager).createQuery("SELECT l FROM Livro l WHERE l.isbn = :isbn", Livro.class);
        verify(query).setParameter("isbn", ISBN);
        verify(query).getResultStream();
    }

    @Test
    public void testBuscarTodos() {
        List<Livro> livros = Arrays.asList(livro);
        when(entityManager.createQuery(
            "SELECT DISTINCT l FROM Livro l LEFT JOIN FETCH l.autores LEFT JOIN FETCH l.editora", 
            Livro.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(livros);

        List<Livro> resultado = livroRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(livro, resultado.get(0));
        
        verify(entityManager).createQuery(
            "SELECT DISTINCT l FROM Livro l LEFT JOIN FETCH l.autores LEFT JOIN FETCH l.editora", 
            Livro.class);
        verify(query).getResultList();
    }

    @Test(expected = RuntimeException.class)
    public void testBuscarTodosComErro() {
        when(entityManager.createQuery(
            "SELECT DISTINCT l FROM Livro l LEFT JOIN FETCH l.autores LEFT JOIN FETCH l.editora", 
            Livro.class)).thenThrow(new RuntimeException("Erro simulado"));

        livroRepository.findAll();
    }

    @Test
    public void testRemover() {
        when(entityManager.contains(any(Livro.class))).thenReturn(true);
        doNothing().when(entityManager).remove(any(Livro.class));

        livroRepository.delete(livro);

        verify(entityManager).remove(any(Livro.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRemoverComErro() {
        when(entityManager.contains(any(Livro.class))).thenReturn(true);
        doThrow(new RuntimeException("Erro simulado")).when(entityManager).remove(any(Livro.class));

        livroRepository.delete(livro);
    }
}
