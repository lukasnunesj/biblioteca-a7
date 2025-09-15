package com.biblioteca.infrastructure.repositories;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.autor.Autor;

public class AutorRepositoryTest {

    private AutorRepository autorRepository;
    
    @Mock
    private EntityManager entityManager;
    
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
        autorRepository = new AutorRepository(entityManager);
        
        autor = new Autor(NOME, CPFCNPJ, TELEFONE, EMAIL);
        autor.setId(ID);
    }
    
    @Test
    public void testSalvar() {
        when(entityManager.merge(any(Autor.class))).thenReturn(autor);
        
        Autor resultado = autorRepository.salvar(autor);
        
        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        assertEquals(NOME, resultado.getNome());
        assertEquals(CPFCNPJ, resultado.getCpfcnpj());
        assertEquals(TELEFONE, resultado.getTelefone());
        assertEquals(EMAIL, resultado.getEmail());
        
        verify(entityManager).merge(any(Autor.class));
        verify(entityManager).flush();
    }
    
    @Test(expected = RuntimeException.class)
    public void testSalvarComErro() {
        when(entityManager.merge(any(Autor.class))).thenThrow(new RuntimeException("Erro simulado"));
        
        autorRepository.salvar(autor);
    }
    
    @Test
    public void testBuscarPorId() {
        when(entityManager.find(Autor.class, ID)).thenReturn(autor);
        
        Optional<Autor> resultado = autorRepository.buscarPorId(ID);
        
        assertTrue(resultado.isPresent());
        assertEquals(autor, resultado.get());
        
        verify(entityManager).find(Autor.class, ID);
    }
    
    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(entityManager.find(Autor.class, ID)).thenReturn(null);
        
        Optional<Autor> resultado = autorRepository.buscarPorId(ID);
        
        assertFalse(resultado.isPresent());
        
        verify(entityManager).find(Autor.class, ID);
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuscarPorIdComErro() {
        when(entityManager.find(Autor.class, ID)).thenThrow(new RuntimeException("Erro simulado"));
        
        autorRepository.buscarPorId(ID);
    }
    
    @Test
    public void testBuscarPorCpfcnpj() {
        when(entityManager.createQuery("SELECT a FROM Autor a WHERE a.cpfcnpj = :cpfcnpj", Autor.class)).thenReturn(query);
        when(query.setParameter("cpfcnpj", CPFCNPJ)).thenReturn(query);
        when(query.getResultStream()).thenReturn(java.util.stream.Stream.of(autor));
        
        Optional<Autor> resultado = autorRepository.buscarPorCpfcnpj(CPFCNPJ);
        
        assertTrue(resultado.isPresent());
        assertEquals(autor, resultado.get());
        
        verify(entityManager).createQuery("SELECT a FROM Autor a WHERE a.cpfcnpj = :cpfcnpj", Autor.class);
        verify(query).setParameter("cpfcnpj", CPFCNPJ);
        verify(query).getResultStream();
    }
    
    @Test
    public void testBuscarPorCpfcnpjNaoEncontrado() {
        when(entityManager.createQuery("SELECT a FROM Autor a WHERE a.cpfcnpj = :cpfcnpj", Autor.class)).thenReturn(query);
        when(query.setParameter("cpfcnpj", CPFCNPJ)).thenReturn(query);
        when(query.getResultStream()).thenReturn(java.util.stream.Stream.empty());
        
        Optional<Autor> resultado = autorRepository.buscarPorCpfcnpj(CPFCNPJ);
        
        assertFalse(resultado.isPresent());
        
        verify(entityManager).createQuery("SELECT a FROM Autor a WHERE a.cpfcnpj = :cpfcnpj", Autor.class);
        verify(query).setParameter("cpfcnpj", CPFCNPJ);
        verify(query).getResultStream();
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuscarPorCpfcnpjComErro() {
        when(entityManager.createQuery("SELECT a FROM Autor a WHERE a.cpfcnpj = :cpfcnpj", Autor.class))
            .thenThrow(new RuntimeException("Erro simulado"));
        
        autorRepository.buscarPorCpfcnpj(CPFCNPJ);
    }
    
    @Test
    public void testBuscarTodos() {
        List<Autor> autores = Arrays.asList(autor);
        when(entityManager.createQuery("SELECT a FROM Autor a", Autor.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(autores);
        
        List<Autor> resultado = autorRepository.buscarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(autor, resultado.get(0));
        
        verify(entityManager).createQuery("SELECT a FROM Autor a", Autor.class);
        verify(query).getResultList();
    }
    
    @Test(expected = RuntimeException.class)
    public void testBuscarTodosComErro() {
        when(entityManager.createQuery("SELECT a FROM Autor a", Autor.class)).thenThrow(new RuntimeException("Erro simulado"));
        
        autorRepository.buscarTodos();
    }
    
    @Test
    public void testRemover() {
        doNothing().when(entityManager).remove(any(Autor.class));
        
        autorRepository.remover(autor);
        
        verify(entityManager).remove(any(Autor.class));
    }
    
    @Test(expected = RuntimeException.class)
    public void testRemoverComErro() {
        doThrow(new RuntimeException("Erro simulado")).when(entityManager).remove(any(Autor.class));
        
        autorRepository.remover(autor);
    }
}
