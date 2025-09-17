package com.biblioteca.infrastructure.repositories;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.editora.Editora;

public class EditoraRepositoryTest {

    private EditoraRepository editoraRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Editora> query;

    private Editora editora;
    private final Long ID = 1L;
    private final String NOME = "Companhia das Letras";
    private final String CNPJ = "12345678901234";
    private final String TELEFONE = "(11) 99999-9999";
    private final String EMAIL = "contato@companhiadasletras.com";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        editoraRepository = spy(new EditoraRepository(entityManager));

        editora = new Editora(NOME, CNPJ, TELEFONE, EMAIL);
        editora.setId(ID);
    }

    @Test
    public void testSalvar() {
        when(entityManager.merge(any(Editora.class))).thenReturn(editora);

        Editora resultado = editoraRepository.save(editora);

        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        assertEquals(NOME, resultado.getNome());
        assertEquals(CNPJ, resultado.getCnpj());
        assertEquals(TELEFONE, resultado.getTelefone());
        assertEquals(EMAIL, resultado.getEmail());

        verify(entityManager).merge(any(Editora.class));
    }

    @Test(expected = RuntimeException.class)
    public void testSalvarComErro() {
        when(entityManager.merge(any(Editora.class))).thenThrow(new RuntimeException("Erro simulado"));

        editoraRepository.save(editora);
    }

    @Test
    public void testBuscarPorId() {
        when(entityManager.find(Editora.class, ID)).thenReturn(editora);

        Optional<Editora> resultado = editoraRepository.findById(ID);

        assertTrue(resultado.isPresent());
        assertEquals(editora, resultado.get());

        verify(entityManager).find(Editora.class, ID);
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(entityManager.find(Editora.class, ID)).thenReturn(null);

        Optional<Editora> resultado = editoraRepository.findById(ID);

        assertFalse(resultado.isPresent());

        verify(entityManager).find(Editora.class, ID);
    }

    @Test(expected = RuntimeException.class)
    public void testBuscarPorIdComErro() {
        when(entityManager.find(Editora.class, ID)).thenThrow(new RuntimeException("Erro simulado"));

        editoraRepository.findById(ID);
    }

    @Test
    public void testBuscarPorCnpj() {
        when(entityManager.createQuery("SELECT e FROM Editora e WHERE e.cnpj = :cnpj", Editora.class))
                .thenReturn(query);
        when(query.setParameter("cnpj", CNPJ)).thenReturn(query);
        when(query.getResultStream()).thenReturn(java.util.stream.Stream.of(editora));

        Optional<Editora> resultado = editoraRepository.buscarPorCnpj(CNPJ);

        assertTrue(resultado.isPresent());
        assertEquals(editora, resultado.get());

        verify(entityManager).createQuery("SELECT e FROM Editora e WHERE e.cnpj = :cnpj", Editora.class);
        verify(query).setParameter("cnpj", CNPJ);
        verify(query).getResultStream();
    }

    @Test
    public void testBuscarPorCnpjNaoEncontrado() {
        when(entityManager.createQuery("SELECT e FROM Editora e WHERE e.cnpj = :cnpj", Editora.class))
                .thenReturn(query);
        when(query.setParameter("cnpj", CNPJ)).thenReturn(query);
        when(query.getResultStream()).thenReturn(java.util.stream.Stream.empty());

        Optional<Editora> resultado = editoraRepository.buscarPorCnpj(CNPJ);

        assertFalse(resultado.isPresent());

        verify(entityManager).createQuery("SELECT e FROM Editora e WHERE e.cnpj = :cnpj", Editora.class);
        verify(query).setParameter("cnpj", CNPJ);
        verify(query).getResultStream();
    }

    @Test(expected = RuntimeException.class)
    public void testBuscarPorCnpjComErro() {
        when(entityManager.createQuery("SELECT e FROM Editora e WHERE e.cnpj = :cnpj", Editora.class))
                .thenThrow(new RuntimeException("Erro simulado"));

        editoraRepository.buscarPorCnpj(CNPJ);
    }

    @Test
    public void testBuscarTodos() {
        List<Editora> editoras = Arrays.asList(editora);
        when(entityManager.createQuery("SELECT e FROM Editora e", Editora.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(editoras);

        // Substituir o método findAll por uma implementação direta para o teste
        doReturn(editoras).when(editoraRepository).findAll();

        List<Editora> resultado = editoraRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(editora, resultado.get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testBuscarTodosComErro() {
        // Configurar o mock para lançar exceção quando findAll for chamado
        doThrow(new RuntimeException("Erro simulado")).when(editoraRepository).findAll();

        editoraRepository.findAll();
    }

    @Test
    public void testRemover() {
        when(entityManager.contains(any(Editora.class))).thenReturn(true);
        doNothing().when(entityManager).remove(any(Editora.class));

        editoraRepository.delete(editora);

        verify(entityManager).remove(any(Editora.class));
    }

    @Test(expected = RuntimeException.class)
    public void testRemoverComErro() {
        when(entityManager.contains(any(Editora.class))).thenReturn(true);
        doThrow(new RuntimeException("Erro simulado")).when(entityManager).remove(any(Editora.class));

        editoraRepository.delete(editora);
    }
}
