package com.biblioteca.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;

public class AutorServiceTest {

    private AutorService autorService;
    
    @Mock
    private IAutorRepository autorRepository;
    
    private Autor autor;
    private AutorDTO autorDTO;
    private final Long ID = 1L;
    private final String NOME = "Carlos Drummond";
    private final String CPFCNPJ = "123.456.789-00";
    private final String TELEFONE = "(31) 99999-9999";
    private final String EMAIL = "carlos@exemplo.com";
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        autorService = new AutorService(autorRepository);
        
        autor = new Autor(NOME, CPFCNPJ, TELEFONE, EMAIL);
        autor.setId(ID);
        
        autorDTO = new AutorDTO(ID, NOME, CPFCNPJ, TELEFONE, EMAIL);
    }
    
    @Test
    public void testSalvar() {
        when(autorRepository.salvar(any(Autor.class))).thenReturn(autor);
        
        Autor resultado = autorService.salvar(autorDTO);
        
        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        assertEquals(NOME, resultado.getNome());
        assertEquals(CPFCNPJ, resultado.getCpfcnpj());
        assertEquals(TELEFONE, resultado.getTelefone());
        assertEquals(EMAIL, resultado.getEmail());
        
        verify(autorRepository).salvar(any(Autor.class));
    }
    
    @Test
    public void testBuscarPorId() {
        when(autorRepository.buscarPorId(ID)).thenReturn(Optional.of(autor));
        
        Optional<Autor> resultado = autorService.buscarPorId(ID);
        
        assertTrue(resultado.isPresent());
        assertEquals(autor, resultado.get());
        
        verify(autorRepository).buscarPorId(ID);
    }
    
    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(autorRepository.buscarPorId(ID)).thenReturn(Optional.empty());
        
        Optional<Autor> resultado = autorService.buscarPorId(ID);
        
        assertFalse(resultado.isPresent());
        
        verify(autorRepository).buscarPorId(ID);
    }
    
    @Test
    public void testBuscarPorCpfcnpj() {
        when(autorRepository.buscarPorCpfcnpj(CPFCNPJ)).thenReturn(Optional.of(autor));
        
        Optional<Autor> resultado = autorService.buscarPorCpfcnpj(CPFCNPJ);
        
        assertTrue(resultado.isPresent());
        assertEquals(autor, resultado.get());
        
        verify(autorRepository).buscarPorCpfcnpj(CPFCNPJ);
    }
    
    @Test
    public void testBuscarPorCpfcnpjNaoEncontrado() {
        when(autorRepository.buscarPorCpfcnpj(CPFCNPJ)).thenReturn(Optional.empty());
        
        Optional<Autor> resultado = autorService.buscarPorCpfcnpj(CPFCNPJ);
        
        assertFalse(resultado.isPresent());
        
        verify(autorRepository).buscarPorCpfcnpj(CPFCNPJ);
    }
    
    @Test
    public void testBuscarTodos() {
        List<Autor> autores = Arrays.asList(autor);
        when(autorRepository.buscarTodos()).thenReturn(autores);
        
        List<Autor> resultado = autorService.buscarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(autor, resultado.get(0));
        
        verify(autorRepository).buscarTodos();
    }
    
    @Test
    public void testRemover() {
        AutorDTO autorDTO = new AutorDTO(ID, NOME, CPFCNPJ, TELEFONE, EMAIL);
        Autor autor = autorDTO.toEntity();
        
        when(autorRepository.buscarPorId(ID)).thenReturn(Optional.of(autor));
        
        autorService.remover(autorDTO);
        
        verify(autorRepository).buscarPorId(ID);
        verify(autorRepository).remover(autor);
    }
}
