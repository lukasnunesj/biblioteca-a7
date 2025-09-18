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

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraRepository;

public class EditoraServiceTest {

    @InjectMocks
    private EditoraService editoraService;
    
    @Mock
    private IEditoraRepository editoraRepository;
    
    private Editora editora;
    private EditoraDTO editoraDTO;
    private final Long ID = 1L;
    private final String NOME = "Companhia das Letras";
    private final String CNPJ = "12345678901234";
    private final String TELEFONE = "(11) 99999-9999";
    private final String EMAIL = "contato@companhiadasletras.com";
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        editora = new Editora(NOME, CNPJ, TELEFONE, EMAIL);
        editora.setId(ID);
        
        editoraDTO = new EditoraDTO(ID, NOME, CNPJ, TELEFONE, EMAIL);
    }
    
    @Test
    public void testSalvar() {
        when(editoraRepository.save(any(Editora.class))).thenReturn(editora);
        
        Editora resultado = editoraService.salvar(editoraDTO);
        
        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        
        verify(editoraRepository).save(any(Editora.class));
    }
    
    @Test
    public void testBuscarPorId() {
        when(editoraRepository.findById(ID)).thenReturn(Optional.of(editora));
        
        Optional<Editora> resultado = editoraService.buscarPorId(ID);
        
        assertTrue(resultado.isPresent());
        assertEquals(editora, resultado.get());
        
        verify(editoraRepository).findById(ID);
    }
    
    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(editoraRepository.findById(ID)).thenReturn(Optional.empty());
        
        Optional<Editora> resultado = editoraService.buscarPorId(ID);
        
        assertFalse(resultado.isPresent());
        
        verify(editoraRepository).findById(ID);
    }
    
    
    
    @Test
    public void testBuscarTodos() {
        List<Editora> editoras = Arrays.asList(editora);
        when(editoraRepository.findAll()).thenReturn(editoras);
        
        List<Editora> resultado = editoraService.buscarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        verify(editoraRepository).findAll();
    }
    
    @Test
    public void testRemover() {
        when(editoraRepository.findById(ID)).thenReturn(Optional.of(editora));
        
        editoraService.remover(editoraDTO);
        
        verify(editoraRepository).findById(ID);
        verify(editoraRepository).delete(editora);
    }
}
