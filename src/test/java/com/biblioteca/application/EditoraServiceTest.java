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

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraRepository;

public class EditoraServiceTest {

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
        editoraService = new EditoraService(editoraRepository);
        
        editora = new Editora(NOME, CNPJ, TELEFONE, EMAIL);
        editora.setId(ID);
        
        editoraDTO = new EditoraDTO(ID, NOME, CNPJ, TELEFONE, EMAIL);
    }
    
    @Test
    public void testSalvar() {
        when(editoraRepository.salvar(any(Editora.class))).thenReturn(editora);
        
        Editora resultado = editoraService.salvar(editoraDTO);
        
        assertNotNull(resultado);
        assertEquals(ID, resultado.getId());
        assertEquals(NOME, resultado.getNome());
        assertEquals(CNPJ, resultado.getCnpj());
        assertEquals(TELEFONE, resultado.getTelefone());
        assertEquals(EMAIL, resultado.getEmail());
        
        verify(editoraRepository).salvar(any(Editora.class));
    }
    
    @Test
    public void testBuscarPorId() {
        when(editoraRepository.buscarPorId(ID)).thenReturn(Optional.of(editora));
        
        Optional<Editora> resultado = editoraService.buscarPorId(ID);
        
        assertTrue(resultado.isPresent());
        assertEquals(editora, resultado.get());
        
        verify(editoraRepository).buscarPorId(ID);
    }
    
    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(editoraRepository.buscarPorId(ID)).thenReturn(Optional.empty());
        
        Optional<Editora> resultado = editoraService.buscarPorId(ID);
        
        assertFalse(resultado.isPresent());
        
        verify(editoraRepository).buscarPorId(ID);
    }
    
    @Test
    public void testBuscarPorCnpj() {
        when(editoraRepository.buscarPorCnpj(CNPJ)).thenReturn(Optional.of(editora));
        
        Optional<Editora> resultado = editoraService.buscarPorCnpj(CNPJ);
        
        assertTrue(resultado.isPresent());
        assertEquals(editora, resultado.get());
        
        verify(editoraRepository).buscarPorCnpj(CNPJ);
    }
    
    @Test
    public void testBuscarPorCnpjNaoEncontrado() {
        when(editoraRepository.buscarPorCnpj(CNPJ)).thenReturn(Optional.empty());
        
        Optional<Editora> resultado = editoraService.buscarPorCnpj(CNPJ);
        
        assertFalse(resultado.isPresent());
        
        verify(editoraRepository).buscarPorCnpj(CNPJ);
    }
    
    @Test
    public void testBuscarTodos() {
        List<Editora> editoras = Arrays.asList(editora);
        when(editoraRepository.buscarTodos()).thenReturn(editoras);
        
        List<Editora> resultado = editoraService.buscarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(editora, resultado.get(0));
        
        verify(editoraRepository).buscarTodos();
    }
    
    @Test
    public void testRemover() {
        EditoraDTO editoraDTO = new EditoraDTO(ID, NOME, CNPJ, TELEFONE, EMAIL);
        Editora editora = editoraDTO.toEntity();
        
        when(editoraRepository.buscarPorId(ID)).thenReturn(Optional.of(editora));
        
        editoraService.remover(editoraDTO);
        
        verify(editoraRepository).buscarPorId(ID);
        verify(editoraRepository).remover(editora);
    }
}
