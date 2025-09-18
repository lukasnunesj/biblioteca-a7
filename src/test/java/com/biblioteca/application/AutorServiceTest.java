package com.biblioteca.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;

public class AutorServiceTest {

    @InjectMocks
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
        
        autor = new Autor(NOME, CPFCNPJ, TELEFONE, EMAIL);
        autor.setId(ID);
        
        autorDTO = new AutorDTO(ID, NOME, CPFCNPJ, TELEFONE, EMAIL);
    }
    
    @Test
    public void testSalvar() {
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);
        
        Autor resultado = autorService.salvar(autorDTO);
        
        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        
        verify(autorRepository).save(any(Autor.class));
    }
    
    @Test
    public void testBuscarPorId() {
        when(autorRepository.findById(ID)).thenReturn(Optional.of(autor));
        
        Optional<Autor> resultado = autorService.buscarPorId(ID);
        
        assertTrue(resultado.isPresent());
        assertEquals(autor, resultado.get());
        
        verify(autorRepository).findById(ID);
    }
    
    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(autorRepository.findById(ID)).thenReturn(Optional.empty());
        
        Optional<Autor> resultado = autorService.buscarPorId(ID);
        
        assertFalse(resultado.isPresent());
        
        verify(autorRepository).findById(ID);
    }
    
    
    
    @Test
    public void testBuscarTodos() {
        List<Autor> autores = Arrays.asList(autor);
        when(autorRepository.findAll()).thenReturn(autores);
        
        List<Autor> resultado = autorService.buscarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        verify(autorRepository).findAll();
    }
    
    @Test
    public void testRemover() {
        when(autorRepository.findById(ID)).thenReturn(Optional.of(autor));
        
        autorService.remover(autorDTO);
        
        verify(autorRepository).findById(ID);
        verify(autorRepository).delete(autor);
    }
}
