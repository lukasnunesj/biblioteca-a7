package com.biblioteca.application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.biblioteca.application.livro.service.OpenLibraryService;
import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.DTO.OpenLibraryResponseDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;

public class LivroServiceTest {

    @InjectMocks
    private LivroService livroService;
    
    @Mock
    private ILivroRepository livroRepository;
    
    @Mock
    private IEditoraService editoraService;
    
    @Mock
    private IAutorService autorService;
    
    @Mock
    private OpenLibraryService openLibraryService;
    
    private Livro livro;
    private LivroDTO livroDTO;
    private Editora editora;
    private Autor autor;
    
    private final Long LIVRO_ID = 1L;
    private final String TITULO = "Dom Casmurro";
    private final String ISBN = "9788574801414";
    private final LocalDate DATA_PUBLICACAO = LocalDate.of(1899, 1, 1);
    
    private final Long EDITORA_ID = 1L;
    private final String EDITORA_NOME = "Companhia das Letras";
    private final String EDITORA_CNPJ = "12345678901234";
    private final String EDITORA_TELEFONE = "(11) 99999-9999";
    private final String EDITORA_EMAIL = "contato@companhiadasletras.com";
    
    private final Long AUTOR_ID = 1L;
    private final String AUTOR_NOME = "Machado de Assis";
    private final String AUTOR_CPFCNPJ = "123.456.789-00";
    private final String AUTOR_TELEFONE = "(21) 99999-9999";
    private final String AUTOR_EMAIL = "machado@exemplo.com";
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        editora = new Editora(EDITORA_NOME, EDITORA_CNPJ, EDITORA_TELEFONE, EDITORA_EMAIL);
        editora.setId(EDITORA_ID);
        
        autor = new Autor(AUTOR_NOME, AUTOR_CPFCNPJ, AUTOR_TELEFONE, AUTOR_EMAIL);
        autor.setId(AUTOR_ID);
        
        livro = new Livro(TITULO, ISBN, DATA_PUBLICACAO);
        livro.setId(LIVRO_ID);
        livro.setEditora(editora);
        livro.setAutores(new HashSet<>(Arrays.asList(autor)));
        
        livroDTO = new LivroDTO(LIVRO_ID, TITULO, ISBN, DATA_PUBLICACAO, EDITORA_ID, Arrays.asList(AUTOR_ID));
    }
    
    @Test
    public void testSalvar() {
        when(editoraService.buscarPorId(EDITORA_ID)).thenReturn(Optional.of(editora));
        when(autorService.buscarPorId(AUTOR_ID)).thenReturn(Optional.of(autor));
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);
        
        Livro resultado = livroService.salvar(livroDTO);
        
        assertNotNull(resultado);
        assertEquals(LIVRO_ID, resultado.getId());
        
        verify(editoraService).buscarPorId(EDITORA_ID);
        verify(autorService).buscarPorId(AUTOR_ID);
        verify(livroRepository).save(any(Livro.class));
    }
    
    @Test
    public void testBuscarPorId() {
        when(livroRepository.findById(LIVRO_ID)).thenReturn(Optional.of(livro));
        
        Optional<Livro> resultado = livroService.buscarPorId(LIVRO_ID);
        
        assertTrue(resultado.isPresent());
        assertEquals(livro, resultado.get());
        
        verify(livroRepository).findById(LIVRO_ID);
    }
    
    @Test
    public void testBuscarPorIdNaoEncontrado() {
        when(livroRepository.findById(LIVRO_ID)).thenReturn(Optional.empty());
        
        Optional<Livro> resultado = livroService.buscarPorId(LIVRO_ID);
        
        assertFalse(resultado.isPresent());
        
        verify(livroRepository).findById(LIVRO_ID);
    }
    
    @Test
    public void testBuscarPorIsbn() {
        when(livroRepository.buscarPorIsbn(ISBN)).thenReturn(Optional.of(livro));
        
        Optional<Livro> resultado = livroService.buscarPorIsbn(ISBN);
        
        assertTrue(resultado.isPresent());
        assertEquals(livro, resultado.get());
        
        verify(livroRepository).buscarPorIsbn(ISBN);
    }
    
    @Test
    public void testBuscarPorIsbnNaoEncontrado() {
        when(livroRepository.buscarPorIsbn(ISBN)).thenReturn(Optional.empty());
        
        Optional<Livro> resultado = livroService.buscarPorIsbn(ISBN);
        
        assertFalse(resultado.isPresent());
        
        verify(livroRepository).buscarPorIsbn(ISBN);
    }
    
    @Test
    public void testBuscarTodos() {
        List<Livro> livros = Arrays.asList(livro);
        when(livroRepository.findAll()).thenReturn(livros);
        
        List<Livro> resultado = livroService.buscarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        verify(livroRepository).findAll();
    }
    
    @Test
    public void testRemover() {
        when(livroRepository.findById(LIVRO_ID)).thenReturn(Optional.of(livro));
        
        livroService.remover(livroDTO);
        
        verify(livroRepository).findById(LIVRO_ID);
        verify(livroRepository).delete(livro);
    }
    
    @Test
    public void testCadastrarPorIsbn_LivroJaExiste() {
        // Given
        String isbn = "9788574801414";
        when(livroRepository.buscarPorIsbn(isbn)).thenReturn(Optional.of(livro));
        
        // When
        Optional<Livro> resultado = livroService.cadastrarPorIsbn(isbn);
        
        // Then
        assertTrue(resultado.isPresent());
        assertEquals(livro, resultado.get());
        verify(livroRepository).buscarPorIsbn(isbn);
        verify(openLibraryService, never()).buscarLivroPorIsbn(any());
    }
    
    @Test
    public void testCadastrarPorIsbn_LivroNaoEncontradoNaAPI() {
        // Given
        String isbn = "9788574801414";
        when(livroRepository.buscarPorIsbn(isbn)).thenReturn(Optional.empty());
        when(openLibraryService.buscarLivroPorIsbn(isbn)).thenReturn(Optional.empty());
        
        // When
        Optional<Livro> resultado = livroService.cadastrarPorIsbn(isbn);
        
        // Then
        assertFalse(resultado.isPresent());
        verify(livroRepository).buscarPorIsbn(isbn);
        verify(openLibraryService).buscarLivroPorIsbn(isbn);
    }
    
    @Test
    public void testCadastrarPorIsbn_SucessoComDadosCompletos() {
        // Given
        String isbn = "9788574801414";
        OpenLibraryResponseDTO dadosOpenLibrary = new OpenLibraryResponseDTO();
        dadosOpenLibrary.setTitle("Test Book");
        dadosOpenLibrary.setPublishDate("2023");
        dadosOpenLibrary.setPublishers(Arrays.asList("Test Publisher"));
        
        when(livroRepository.buscarPorIsbn(isbn)).thenReturn(Optional.empty());
        when(openLibraryService.buscarLivroPorIsbn(isbn)).thenReturn(Optional.of(dadosOpenLibrary));
        when(editoraService.buscarTodos()).thenReturn(Arrays.asList());
        when(editoraService.salvar(any(EditoraDTO.class))).thenReturn(editora);
        when(autorService.buscarTodos()).thenReturn(Arrays.asList());
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);
        
        // When
        Optional<Livro> resultado = livroService.cadastrarPorIsbn(isbn);
        
        // Then
        assertTrue(resultado.isPresent());
        verify(livroRepository).buscarPorIsbn(isbn);
        verify(openLibraryService).buscarLivroPorIsbn(isbn);
        verify(livroRepository).save(any(Livro.class));
    }
}
