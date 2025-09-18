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
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;

public class ImportacaoLivroServiceTest {

    @Mock
    private ILivroRepository livroRepository;

    @InjectMocks
    private ImportacaoLivroService importacaoLivroService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveImportarNovosLivrosECriarRegistros() {
        String csvContent = "isbn,titulo,dataPublicacao\n" +
                            "978-3-16-148410-0,Livro A,2023-01-15\n" +
                            "978-1-23-456789-7,Livro B,2023-03-20";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        when(livroRepository.buscarPorIsbn(anyString())).thenReturn(Optional.empty());

        importacaoLivroService.importar(inputStream);

        verify(livroRepository, times(2)).save(any(Livro.class));
    }

    @Test
    public void deveImportarLivrosExistentesEAtualizarRegistros() {
        String csvContent = "isbn,titulo,dataPublicacao\n" +
                            "978-3-16-148410-0,Livro A Atualizado,2023-01-20";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        Livro livroExistente = new Livro("Livro A Original", "978-3-16-148410-0", LocalDate.of(2023, 1, 15));
        when(livroRepository.buscarPorIsbn("978-3-16-148410-0")).thenReturn(Optional.of(livroExistente));

        importacaoLivroService.importar(inputStream);

        verify(livroRepository, times(1)).save(livroExistente);
        assert(livroExistente.getTitulo().equals("Livro A Atualizado"));
        assert(livroExistente.getDataPublicacao().equals(LocalDate.of(2023, 1, 20)));
    }
}
