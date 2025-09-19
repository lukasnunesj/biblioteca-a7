package com.biblioteca.application.livro.service;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.Livro;
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
        String csvContent = "titulo,isbn,data_publicacao,editora,autores\n" +
                            "Livro Teste,978-3-16-148410-0,2023,Editora Teste,Autor Teste";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        when(editoraService.buscarPorNome(anyString())).thenReturn(Optional.empty());
        when(autorService.buscarPorNome(anyString())).thenReturn(Optional.empty());

        when(editoraService.salvar(any())).thenReturn(new Editora("Editora Teste", null, null, null));
        when(autorService.salvar(any())).thenReturn(new Autor("Autor Teste", null, null, null));

        importacaoLivroService.importar(inputStream);

        verify(editoraService, times(1)).salvar(any());
        verify(autorService, times(1)).salvar(any());
        verify(livroService, times(1)).salvar(any(LivroDTO.class));
    }

    @Test
    public void deveReutilizarEditoraSeNomeExistir() {
        String csvContent = "titulo,isbn,data_publicacao,editora,autores\n" +
                            "Livro Teste,978-3-16-148410-0,2023,Editora Existente,Autor Teste";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        Editora editoraExistente = new Editora("Editora Existente", null, null, null);

        when(editoraService.buscarPorNome("Editora Existente")).thenReturn(Optional.of(editoraExistente));
        when(autorService.buscarPorNome(anyString())).thenReturn(Optional.empty());
        when(autorService.salvar(any())).thenReturn(new Autor("Autor Teste", null, null, null));

        importacaoLivroService.importar(inputStream);

        verify(editoraService, never()).salvar(any());
        verify(livroService, times(1)).salvar(any(LivroDTO.class));
    }

    @Test
    public void deveReutilizarAutorSeNomeExistir() {
        String csvContent = "titulo,isbn,data_publicacao,editora,autores\n" +
                            "Livro Teste,978-3-16-148410-0,2023,Editora Teste,Autor Existente";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        Autor autorExistente = new Autor("Autor Existente", null, null, null);

        when(editoraService.buscarPorNome(anyString())).thenReturn(Optional.empty());
        when(autorService.buscarPorNome("Autor Existente")).thenReturn(Optional.of(autorExistente));
        when(editoraService.salvar(any())).thenReturn(new Editora("Editora Teste", null, null, null));

        importacaoLivroService.importar(inputStream);

        verify(autorService, never()).salvar(any());
        verify(livroService, times(1)).salvar(any(LivroDTO.class));
    }
    
    @Test
    public void deveAtualizarLivroSeIsbnJaExistir() {
        // Preparar dados de teste
        String isbn = "978-3-16-148410-0";
        String csvContent = "titulo,isbn,data_publicacao,editora,autores\n" +
                            "Livro Atualizado," + isbn + ",2023,Editora Nova,Autor Novo";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        
        // Criar livro existente
        Livro livroExistente = new Livro();
        livroExistente.setId(123L);
        livroExistente.setTitulo("Livro Original");
        livroExistente.setIsbn(isbn);
        livroExistente.setDataPublicacao(2020);
        
        // Configurar mocks
        Editora editoraNova = new Editora();
        editoraNova.setId(456L);
        editoraNova.setNome("Editora Nova");
        
        Autor autorNovo = new Autor();
        autorNovo.setId(789L);
        autorNovo.setNome("Autor Novo");
        
        when(livroService.buscarPorIsbn(isbn)).thenReturn(Optional.of(livroExistente));
        when(editoraService.buscarPorNome("Editora Nova")).thenReturn(Optional.of(editoraNova));
        when(autorService.buscarPorNome("Autor Novo")).thenReturn(Optional.of(autorNovo));
        when(autorService.buscarPorId(789L)).thenReturn(Optional.of(autorNovo));
        
        // Executar o método a ser testado
        importacaoLivroService.importar(inputStream);
        
        // Verificar que o método fromEntity e salvar foram chamados
        verify(livroService).salvar(any(LivroDTO.class));
        
        // Verificar que o livro existente foi atualizado com os novos valores
        verify(livroService).buscarPorIsbn(isbn);
        verify(editoraService).buscarPorNome("Editora Nova");
        verify(autorService).buscarPorNome("Autor Novo");
        verify(autorService).buscarPorId(789L);
    }
}
