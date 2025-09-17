package com.biblioteca.integration.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.biblioteca.application.AutorService;
import com.biblioteca.application.EditoraService;
import com.biblioteca.application.LivroService;
import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.infrastructure.repositories.AutorRepository;
import com.biblioteca.infrastructure.repositories.EditoraRepository;
import com.biblioteca.infrastructure.repositories.LivroRepository;
import com.biblioteca.integration.IntegrationTestBase;

public class LivroServiceIntegrationTest extends IntegrationTestBase {

    private LivroService livroService;
    private LivroRepository livroRepository;
    private EditoraRepository editoraRepository;
    private AutorRepository autorRepository;
    private AutorService autorService;
    private EditoraService editoraService;

    @Override
    protected void beforeEachTest() {
        livroRepository = new LivroRepository(entityManager);
        editoraRepository = new EditoraRepository(entityManager);
        autorRepository = new AutorRepository(entityManager);

        autorService = new AutorService(autorRepository);
        editoraService = new EditoraService(editoraRepository);

        livroService = new LivroService(livroRepository, editoraService, autorService);
    }

    @Test
    public void testSalvar() {
        // Arrange
        Editora editora = criarEditora();
        Autor autor = criarAutor();

        // Usar um ISBN único para evitar conflitos
        String isbnUnico = String.format("978%010d", Math.abs(System.nanoTime() % 10000000000L));

        LivroDTO livroDTO = new LivroDTO(
                null,
                "Dom Casmurro",
                isbnUnico,
                LocalDate.of(1899, 1, 1),
                editora.getId(),
                Arrays.asList(autor.getId()));

        // Act
        Livro livroSalvo = livroService.salvar(livroDTO);

        // Assert
        assertThat(livroSalvo.getId()).isNotNull();
        assertThat(livroSalvo.getTitulo()).isEqualTo("Dom Casmurro");
        assertThat(livroSalvo.getIsbn()).isEqualTo(isbnUnico);
        assertThat(livroSalvo.getDataPublicacao()).isEqualTo(LocalDate.of(1899, 1, 1));
        assertThat(livroSalvo.getEditora().getId()).isEqualTo(editora.getId());
        assertThat(livroSalvo.getAutores()).hasSize(1);
        assertThat(livroSalvo.getAutores().iterator().next().getId()).isEqualTo(autor.getId());
    }

    @Test
    public void testBuscarPorId() {
        // Arrange
        Livro livro = criarLivroCompleto();

        // Act
        Optional<Livro> resultado = livroService.buscarPorId(livro.getId());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Dom Casmurro");
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        // Act
        Optional<Livro> resultado = livroService.buscarPorId(999L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPorIsbn() {
        // Arrange
        Livro livro = criarLivroCompleto();

        // Garantir que a entidade esteja persistida
        transaction.commit();
        transaction.begin();

        // Act
        Optional<Livro> resultado = livroService.buscarPorIsbn(livro.getIsbn());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Dom Casmurro");
    }

    @Test
    public void testBuscarTodos() {
        // Arrange
        criarLivroCompleto();
        criarOutroLivro();

        // Act
        List<Livro> livros = livroService.buscarTodos();

        // Assert
        assertThat(livros).hasSize(2);
        assertThat(livros).extracting("titulo").contains("Dom Casmurro", "Memórias Póstumas de Brás Cubas");
    }

    @Test
    public void testRemover() {
        // Arrange
        Livro livro = criarLivroCompleto();
        Long livroId = livro.getId();

        // Garantir que a entidade esteja persistida
        transaction.commit();
        entityManager.clear();
        transaction = entityManager.getTransaction();
        transaction.begin();

        // Buscar a entidade novamente para garantir que está gerenciada
        Livro livroGerenciado = entityManager.find(Livro.class, livroId);
        assertThat(livroGerenciado).isNotNull();

        LivroDTO livroDTO = new LivroDTO(
                livroGerenciado.getId(),
                livroGerenciado.getTitulo(),
                livroGerenciado.getIsbn(),
                livroGerenciado.getDataPublicacao(),
                livroGerenciado.getEditora().getId(),
                Arrays.asList(livroGerenciado.getAutores().iterator().next().getId()));

        // Act
        livroService.remover(livroDTO);

        // Commit para confirmar a remoção
        transaction.commit();
        transaction = entityManager.getTransaction();
        transaction.begin();

        // Verificar se foi removido
        Optional<Livro> resultado = livroService.buscarPorId(livroId);

        // Assert
        assertThat(resultado).isEmpty();
    }

    // Métodos auxiliares

    private Editora criarEditora() {
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhiadasletras.com");
        return editoraRepository.save(editora);
    }

    private Autor criarAutor() {
        Autor autor = new Autor("Machado de Assis", "123.456.789-00", "(21) 99999-9999", "machado@exemplo.com");
        return autorRepository.save(autor);
    }

    private Livro criarLivroCompleto() {
        Editora editora = criarEditora();
        Autor autor = criarAutor();

        // Usar um ISBN único para evitar conflitos
        String isbnUnico = String.format("978%010d", Math.abs(System.nanoTime() % 10000000000L));

        Livro livro = new Livro("Dom Casmurro", isbnUnico, LocalDate.of(1899, 1, 1));
        livro.setEditora(editora);
        livro.addAutor(autor);

        return livroRepository.save(livro);
    }

    private Livro criarOutroLivro() {
        Editora editora = entityManager.find(Editora.class, 1L);
        if (editora == null) {
            editora = criarEditora();
        }

        Autor autor = entityManager.find(Autor.class, 1L);
        if (autor == null) {
            autor = criarAutor();
        }

        // Usar um ISBN único para evitar conflitos
        String isbnUnico = String.format("978%010d", Math.abs((System.nanoTime() + 1) % 10000000000L));

        Livro livro = new Livro("Memórias Póstumas de Brás Cubas", isbnUnico, LocalDate.of(1881, 1, 1));
        livro.setEditora(editora);
        livro.addAutor(autor);

        return livroRepository.save(livro);
    }
}
