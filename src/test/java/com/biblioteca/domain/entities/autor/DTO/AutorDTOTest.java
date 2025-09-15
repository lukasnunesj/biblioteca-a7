package com.biblioteca.domain.entities.autor.DTO;

import static org.junit.Assert.*;

import org.junit.Test;

import com.biblioteca.domain.entities.autor.Autor;

public class AutorDTOTest {
    
    private final Long ID = 1L;
    private final String NOME = "Carlos Drummond";
    private final String CPFCNPJ = "123.456.789-00";
    private final String TELEFONE = "(31) 99999-9999";
    private final String EMAIL = "carlos@exemplo.com";
    
    @Test
    public void testConstrutor() {
        AutorDTO autorDTO = new AutorDTO(ID, NOME, CPFCNPJ, TELEFONE, EMAIL);
        
        assertEquals(ID, autorDTO.getId());
        assertEquals(NOME, autorDTO.getNome());
        assertEquals(CPFCNPJ, autorDTO.getCpfcnpj());
        assertEquals(TELEFONE, autorDTO.getTelefone());
        assertEquals(EMAIL, autorDTO.getEmail());
    }
    
    @Test
    public void testToEntity() {
        AutorDTO autorDTO = new AutorDTO(ID, NOME, CPFCNPJ, TELEFONE, EMAIL);
        
        Autor autor = autorDTO.toEntity();
        
        assertEquals(ID, autor.getId());
        assertEquals(NOME, autor.getNome());
        assertEquals(CPFCNPJ, autor.getCpfcnpj());
        assertEquals(TELEFONE, autor.getTelefone());
        assertEquals(EMAIL, autor.getEmail());
    }

    @Test
    public void testSettersAndGetters() {
        AutorDTO autorDTO = new AutorDTO(ID, NOME, CPFCNPJ, TELEFONE, EMAIL);

        Long novoId = 2L;
        String novoNome = "Outro Autor";
        String novoCpfcnpj = "987.654.321-00";
        String novoTelefone = "(11) 88888-8888";
        String novoEmail = "outro@exemplo.com";

        autorDTO.setId(novoId);
        autorDTO.setNome(novoNome);
        autorDTO.setCpfcnpj(novoCpfcnpj);
        autorDTO.setTelefone(novoTelefone);
        autorDTO.setEmail(novoEmail);

        assertEquals(novoId, autorDTO.getId());
        assertEquals(novoNome, autorDTO.getNome());
        assertEquals(novoCpfcnpj, autorDTO.getCpfcnpj());
        assertEquals(novoTelefone, autorDTO.getTelefone());
        assertEquals(novoEmail, autorDTO.getEmail());
    }
}
