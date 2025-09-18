package com.biblioteca.application.livro.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.DTO.OpenLibraryResponseDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;

public class CadastrarLivroPorIsbnServiceTest {

    @InjectMocks
    private CadastrarLivroPorIsbnService cadastrarLivroPorIsbnService;

    @Mock
    private ILivroRepository livroRepository;

    @Mock
    private IEditoraService editoraService;

    @Mock
    private IAutorService autorService;

    @Mock
    private OpenLibraryService openLibraryService;

    @Mock
    private ILivroService livroService;

    private Livro livro;
    private Editora editora;
    private Autor autor;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        editora = new Editora("Test Publisher", "12345678901234", "(11) 99999-9999", "contato@editora.com");
        editora.setId(1L);
        autor = new Autor("Test Author", "123.456.789-00", "(21) 99999-9999", "autor@exemplo.com");
        autor.setId(1L);
        livro = new Livro("Test Book", "9788574801414", 2023);
        livro.setId(1L);
    }

    @Test
    public void testExecute_LivroJaExiste() {
        String isbn = "9788574801414";
        when(livroRepository.buscarPorIsbn(isbn)).thenReturn(Optional.of(livro));

        Optional<Livro> resultado = cadastrarLivroPorIsbnService.execute(isbn);

        assertTrue(resultado.isPresent());
        assertEquals(livro, resultado.get());
        verify(livroRepository).buscarPorIsbn(isbn);
        verify(openLibraryService, never()).buscarLivroPorIsbn(any());
    }

    @Test
    public void testExecute_LivroNaoEncontradoNaAPI() {
        String isbn = "9788574801414";
        when(livroRepository.buscarPorIsbn(isbn)).thenReturn(Optional.empty());
        when(openLibraryService.buscarLivroPorIsbn(isbn)).thenReturn(Optional.empty());

        Optional<Livro> resultado = cadastrarLivroPorIsbnService.execute(isbn);

        assertFalse(resultado.isPresent());
        verify(livroRepository).buscarPorIsbn(isbn);
        verify(openLibraryService).buscarLivroPorIsbn(isbn);
    }

    @Test
    public void testExecute_SucessoComDadosCompletos() {
        String isbn = "9788574801414";
        String tituloLivro = "Test Book";
        String nomeEditora = "Test Publisher";
        String nomeAutor = "Test Author";

        OpenLibraryResponseDTO dadosOpenLibrary = mock(OpenLibraryResponseDTO.class);
        OpenLibraryResponseDTO.Publisher publisher = mock(OpenLibraryResponseDTO.Publisher.class);
        OpenLibraryResponseDTO.Author authorMock = mock(OpenLibraryResponseDTO.Author.class);

        when(dadosOpenLibrary.getTitle()).thenReturn(tituloLivro);
        when(dadosOpenLibrary.getPublishDate()).thenReturn("2023");
        when(dadosOpenLibrary.getPublishers()).thenReturn(Arrays.asList(publisher));
        when(publisher.getName()).thenReturn(nomeEditora);
        when(dadosOpenLibrary.getAuthors()).thenReturn(Arrays.asList(authorMock));
        when(authorMock.getName()).thenReturn(nomeAutor);

        when(livroRepository.buscarPorIsbn(isbn)).thenReturn(Optional.empty());
        when(openLibraryService.buscarLivroPorIsbn(isbn)).thenReturn(Optional.of(dadosOpenLibrary));
        when(editoraService.buscarPorNome(nomeEditora)).thenReturn(Optional.empty());
        when(autorService.buscarPorNome(nomeAutor)).thenReturn(Optional.empty());
        when(editoraService.salvar(any(EditoraDTO.class))).thenReturn(editora);
        when(autorService.salvar(any())).thenReturn(autor);
        when(livroService.salvar(any(LivroDTO.class))).thenReturn(livro);

        Optional<Livro> resultado = cadastrarLivroPorIsbnService.execute(isbn);

        assertTrue(resultado.isPresent());
        verify(livroRepository).buscarPorIsbn(isbn);
        verify(openLibraryService).buscarLivroPorIsbn(isbn);
        verify(editoraService).salvar(any(EditoraDTO.class));
        verify(autorService).salvar(any());
        verify(livroService).salvar(any(LivroDTO.class));
    }
}
