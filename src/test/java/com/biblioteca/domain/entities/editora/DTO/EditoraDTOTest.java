package com.biblioteca.domain.entities.editora.DTO;

import static org.junit.Assert.*;

import org.junit.Test;

import com.biblioteca.domain.entities.editora.Editora;

public class EditoraDTOTest {
    
    private final Long ID = 1L;
    private final String NOME = "Companhia das Letras";
    private final String CNPJ = "12345678901234";
    private final String TELEFONE = "(11) 99999-9999";
    private final String EMAIL = "contato@companhiadasletras.com";
    
    @Test
    public void testConstrutor() {
        EditoraDTO editoraDTO = new EditoraDTO(ID, NOME, CNPJ, TELEFONE, EMAIL);
        
        assertEquals(ID, editoraDTO.getId());
        assertEquals(NOME, editoraDTO.getNome());
        assertEquals(CNPJ, editoraDTO.getCnpj());
        assertEquals(TELEFONE, editoraDTO.getTelefone());
        assertEquals(EMAIL, editoraDTO.getEmail());
    }
    
    @Test
    public void testToEntity() {
        EditoraDTO editoraDTO = new EditoraDTO(ID, NOME, CNPJ, TELEFONE, EMAIL);
        
        Editora editora = editoraDTO.toEntity();
        
        assertEquals(ID, editora.getId());
        assertEquals(NOME, editora.getNome());
        assertEquals(CNPJ, editora.getCnpj());
        assertEquals(TELEFONE, editora.getTelefone());
        assertEquals(EMAIL, editora.getEmail());
    }

    @Test
    public void testSettersAndGetters() {
        EditoraDTO editoraDTO = new EditoraDTO(ID, NOME, CNPJ, TELEFONE, EMAIL);

        Long novoId = 2L;
        String novoNome = "Outra Editora";
        String novoCnpj = "98765432109876";
        String novoTelefone = "(11) 88888-8888";
        String novoEmail = "outro@editora.com";

        editoraDTO.setId(novoId);
        editoraDTO.setNome(novoNome);
        editoraDTO.setCnpj(novoCnpj);
        editoraDTO.setTelefone(novoTelefone);
        editoraDTO.setEmail(novoEmail);

        assertEquals(novoId, editoraDTO.getId());
        assertEquals(novoNome, editoraDTO.getNome());
        assertEquals(novoCnpj, editoraDTO.getCnpj());
        assertEquals(novoTelefone, editoraDTO.getTelefone());
        assertEquals(novoEmail, editoraDTO.getEmail());
    }
}
