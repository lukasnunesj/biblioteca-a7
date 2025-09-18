package com.biblioteca.application.livro.service;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;

public class ImportacaoLivroServiceTest {

    @Mock
    private ILivroService livroService;

    @Mock
    private IEditoraService editoraService;

    @Mock
    private IAutorService autorService;

    @InjectMocks
    private ImportacaoLivroService importacaoLivroService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveImportarNovosLivrosECriarRegistros() {
        String csvContent = "titulo,isbn,data_publicacao,editora,cnpj_editora,autores,cpf_cnpj_autores\n" +
                            "Livro Teste,978-3-16-148410-0,2023-01-15,Editora Teste,12345678000195,Autor Teste,12345678900";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        when(editoraService.buscarPorCnpj(anyString())).thenReturn(Optional.empty());
        when(autorService.buscarPorCpfcnpj(anyString())).thenReturn(Optional.empty());

        when(editoraService.salvar(any())).thenReturn(new Editora("Editora Teste", "12345678000195", "(00) 00000-0000", "contato@editorateste.com"));
        when(autorService.salvar(any())).thenReturn(new Autor("Autor Teste", "12345678900", "(00) 00000-0000", "contato@autorteste.com"));

        importacaoLivroService.importar(inputStream);

        verify(editoraService, times(1)).salvar(any());
        verify(autorService, times(1)).salvar(any());
        verify(livroService, times(1)).salvar(any(LivroDTO.class));
    }

    @Test
    public void deveAtualizarNomeDaEditoraSeCnpjExistir() {
        String csvContent = "titulo,isbn,data_publicacao,editora,cnpj_editora,autores,cpf_cnpj_autores\n" +
                            "Livro Teste,978-3-16-148410-0,2023-01-15,Novo Nome Editora,12345678000195,Autor Teste,12345678900";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        Editora editoraExistente = new Editora("Nome Antigo", "12345678000195", "(00) 00000-0000", "contato@editora.com");

        when(editoraService.buscarPorCnpj(anyString())).thenReturn(Optional.of(editoraExistente));
        when(autorService.buscarPorCpfcnpj(anyString())).thenReturn(Optional.empty());
        when(autorService.salvar(any())).thenReturn(new Autor("Autor Teste", "12345678900", "(00) 00000-0000", "contato@autorteste.com"));
        when(editoraService.salvar(any(com.biblioteca.domain.entities.editora.DTO.EditoraDTO.class))).thenReturn(editoraExistente);

        importacaoLivroService.importar(inputStream);

        verify(editoraService, times(1)).salvar(any(com.biblioteca.domain.entities.editora.DTO.EditoraDTO.class));
        verify(livroService, times(1)).salvar(any(LivroDTO.class));
    }

    @Test
    public void deveAtualizarNomeDoAutorSeCpfCnpjExistir() {
        String csvContent = "titulo,isbn,data_publicacao,editora,cnpj_editora,autores,cpf_cnpj_autores\n" +
                            "Livro Teste,978-3-16-148410-0,2023-01-15,Editora Teste,12345678000195,Novo Nome Autor,12345678900";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        Autor autorExistente = new Autor("Nome Antigo", "12345678900", "(00) 00000-0000", "contato@autor.com");

        when(editoraService.buscarPorCnpj(anyString())).thenReturn(Optional.empty());
        when(autorService.buscarPorCpfcnpj(anyString())).thenReturn(Optional.of(autorExistente));
        when(editoraService.salvar(any())).thenReturn(new Editora("Editora Teste", "12345678000195", "(00) 00000-0000", "contato@editorateste.com"));
        when(autorService.salvar(any(com.biblioteca.domain.entities.autor.DTO.AutorDTO.class))).thenReturn(autorExistente);

        importacaoLivroService.importar(inputStream);

        verify(autorService, times(1)).salvar(any(com.biblioteca.domain.entities.autor.DTO.AutorDTO.class));
        verify(livroService, times(1)).salvar(any(LivroDTO.class));
    }
}
