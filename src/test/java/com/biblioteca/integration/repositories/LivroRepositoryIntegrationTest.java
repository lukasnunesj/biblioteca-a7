package com.biblioteca.integration.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.infrastructure.repositories.AutorRepository;
import com.biblioteca.infrastructure.repositories.EditoraRepository;
import com.biblioteca.infrastructure.repositories.LivroRepository;
import com.biblioteca.integration.IntegrationTestBase;

public class LivroRepositoryIntegrationTest extends IntegrationTestBase {

    private LivroRepository livroRepository;
    private EditoraRepository editoraRepository;
    private AutorRepository autorRepository;

    @Override
    protected void beforeEachTest() {
        livroRepository = new LivroRepository(entityManager);
        editoraRepository = new EditoraRepository(entityManager);
        autorRepository = new AutorRepository(entityManager);
    }

    @Test
    public void testSalvarLivro() {
        // Arrange
        Editora editora = criarEditora();
        Autor autor = criarAutor();

        Livro livro = new Livro("Dom Casmurro", "9788574801414", 1899);
        livro.setEditora(editora);
        livro.addAutor(autor);

        // Act
        Livro livroSalvo = livroRepository.save(livro);

        // Assert
        assertThat(livroSalvo.getId()).isNotNull();
        assertThat(livroSalvo.getTitulo()).isEqualTo("Dom Casmurro");
        assertThat(livroSalvo.getIsbn()).isEqualTo("9788574801414");
        assertThat(livroSalvo.getDataPublicacao()).isEqualTo(1899);
        assertThat(livroSalvo.getEditora().getNome()).isEqualTo("Companhia das Letras");
        assertThat(livroSalvo.getAutores()).hasSize(1);
        assertThat(livroSalvo.getAutores().iterator().next().getNome()).isEqualTo("Machado de Assis");
    }

    @Test
    public void testBuscarPorId() {
        // Arrange
        Livro livro = criarLivroCompleto();

        // Act
        Optional<Livro> resultado = livroRepository.findById(livro.getId());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Dom Casmurro");
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        // Act
        Optional<Livro> resultado = livroRepository.findById(999L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarTodos() {
        // Arrange
        Livro livro1 = criarLivroCompleto();

        Editora editora = entityManager.find(Editora.class, livro1.getEditora().getId());
        Autor autor = entityManager.find(Autor.class, livro1.getAutores().iterator().next().getId());

        Livro livro2 = new Livro("Memórias Póstumas de Brás Cubas", "9788535921182", 1881);
        livro2.setEditora(editora);
        livro2.addAutor(autor);
        livroRepository.save(livro2);

        // Act
        List<Livro> livros = livroRepository.findAll();

        // Assert
        assertThat(livros).hasSize(2);
        assertThat(livros).extracting("titulo").contains("Dom Casmurro", "Memórias Póstumas de Brás Cubas");
    }

    @Test
    public void testRemover() {
        // Arrange
        Livro livro = criarLivroCompleto();

        // Act
        livroRepository.delete(livro);
        Optional<Livro> resultado = livroRepository.findById(livro.getId());

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

        Livro livro = new Livro("Dom Casmurro", "9788574801414", 1899);
        livro.setEditora(editora);
        livro.addAutor(autor);

        return livroRepository.save(livro);
    }
}
