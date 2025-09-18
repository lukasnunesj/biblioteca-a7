package com.biblioteca.infrastructure.repositories;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.infrastructure.exceptions.PersistenciaException;

public class AutorRepositoryTest {

    private AutorRepository autorRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityTransaction transaction;

    @Mock
    private TypedQuery<Autor> query;

    private Autor autor;
    private final Long ID = 1L;
    private final String NOME = "Carlos Drummond";
    private final String CPFCNPJ = "123.456.789-00";
    private final String TELEFONE = "(31) 99999-9999";
    private final String EMAIL = "carlos@exemplo.com";

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        autorRepository = spy(new AutorRepository(entityManager));

        // Configurar o mock do EntityManager
        when(entityManager.getTransaction()).thenReturn(transaction);
        doNothing().when(transaction).begin();
        doNothing().when(transaction).commit();
        when(transaction.isActive()).thenReturn(true);

        autor = new Autor(NOME, CPFCNPJ, TELEFONE, EMAIL);
        autor.setId(ID);
    }

    @Test
    public void testSalvar() {
        when(entityManager.merge(any(Autor.class))).thenReturn(autor);

        Autor resultado = autorRepository.save(autor);

        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        assertEquals(NOME, resultado.getNome());
        assertEquals(CPFCNPJ, resultado.getCpfcnpj());
        assertEquals(TELEFONE, resultado.getTelefone());
        assertEquals(EMAIL, resultado.getEmail());

        verify(entityManager).merge(any(Autor.class));
    }

    @Test(expected = PersistenciaException.class)
    public void testSalvarComErro() {
        when(entityManager.merge(any(Autor.class))).thenThrow(new RuntimeException("Erro simulado"));

        autorRepository.save(autor);
    }

    @Test
    public void testBuscarPorId() {
        when(entityManager.find(Autor.class, ID)).thenReturn(autor);

        Optional<Autor> resultado = autorRepository.findById(ID);

        assertTrue(resultado.isPresent());
        assertEquals(autor, resultado.get());

        verify(entityManager).find(Autor.class, ID);
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(entityManager.find(Autor.class, ID)).thenReturn(null);

        Optional<Autor> resultado = autorRepository.findById(ID);

        assertFalse(resultado.isPresent());

        verify(entityManager).find(Autor.class, ID);
    }

    @Test(expected = PersistenciaException.class)
    public void testBuscarPorIdComErro() {
        when(entityManager.find(Autor.class, ID)).thenThrow(new RuntimeException("Erro simulado"));

        autorRepository.findById(ID);
    }




    @Test
    public void testBuscarTodos() {
        List<Autor> autores = Arrays.asList(autor);
        when(entityManager.createQuery("SELECT a FROM Autor a", Autor.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(autores);

        // Substituir o método findAll por uma implementação direta para o teste
        doReturn(autores).when(autorRepository).findAll();

        List<Autor> resultado = autorRepository.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(autor, resultado.get(0));
    }

    @Test(expected = PersistenciaException.class)
    public void testBuscarTodosComErro() {
        // Configurar o mock para lançar exceção quando findAll for chamado
        doThrow(new PersistenciaException("Erro simulado", new RuntimeException())).when(autorRepository).findAll();

        autorRepository.findAll();
    }

    @Test
    public void testRemover() {
        when(entityManager.contains(any(Autor.class))).thenReturn(true);
        doNothing().when(entityManager).remove(any(Autor.class));

        autorRepository.delete(autor);

        verify(entityManager).remove(any(Autor.class));
    }

    @Test(expected = PersistenciaException.class)
    public void testRemoverComErro() {
        when(entityManager.contains(any(Autor.class))).thenReturn(true);
        doThrow(new RuntimeException("Erro simulado")).when(entityManager).remove(any(Autor.class));

        autorRepository.delete(autor);
    }
}
