package com.biblioteca.integration.services;

import static org.assertj.core.api.Assertions.assertThat;

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

/**
 * Testes de integração para o serviço LivroService.
 * <p>
 * Esta classe testa a integração entre o serviço de livros e o banco de dados,
 * verificando operações como salvar, buscar, atualizar e remover livros.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class LivroServiceIntegrationTest extends IntegrationTestBase {

    private LivroService livroService;
    private LivroRepository livroRepository;
    private EditoraRepository editoraRepository;
    private AutorRepository autorRepository;
    private AutorService autorService;
    private EditoraService editoraService;

    /**
     * Configura o ambiente de teste antes de cada teste.
     * Inicializa os repositórios e serviços necessários para os testes.
     */
    @Override
    protected void beforeEachTest() {
        livroRepository = new LivroRepository(entityManager);
        editoraRepository = new EditoraRepository(entityManager);
        autorRepository = new AutorRepository(entityManager);

        autorService = new AutorService(autorRepository);
        editoraService = new EditoraService(editoraRepository);

        livroService = new LivroService(livroRepository, editoraService, autorService, new com.biblioteca.application.livro.service.OpenLibraryService());
    }

    /**
     * Testa o método salvar do LivroService.
     * Verifica se um livro é corretamente salvo no banco de dados com todos os seus relacionamentos.
     */
    @Test
    public void testSalvar() {
        Editora editora = criarEditora();
        Autor autor = criarAutor();

        // Usar um ISBN único para evitar conflitos
        String isbnUnico = String.format("978%010d", Math.abs(System.nanoTime() % 10000000000L));

        LivroDTO livroDTO = new LivroDTO(
                null,
                "Dom Casmurro",
                isbnUnico,
                1899,
                editora.getId(),
                Arrays.asList(autor.getId()),
                new java.util.ArrayList<>());

        // Act
        Livro livroSalvo = livroService.salvar(livroDTO);

        // Assert
        assertThat(livroSalvo.getId()).isNotNull();
        assertThat(livroSalvo.getTitulo()).isEqualTo("Dom Casmurro");
        assertThat(livroSalvo.getIsbn()).isEqualTo(isbnUnico);
        assertThat(livroSalvo.getDataPublicacao()).isEqualTo(1899);
        assertThat(livroSalvo.getEditora().getId()).isEqualTo(editora.getId());
        assertThat(livroSalvo.getAutores()).hasSize(1);
        assertThat(livroSalvo.getAutores().iterator().next().getId()).isEqualTo(autor.getId());
    }
    
    /**
     * Testa o método salvar do LivroService com múltiplos autores.
     * Verifica se um livro é corretamente salvo com vários autores associados.
     */
    @Test
    public void testSalvarComMultiplosAutores() {
        Editora editora = criarEditora();
        Autor autor1 = criarAutor();
        Autor autor2 = criarOutroAutor();

        // Usar um ISBN único para evitar conflitos
        String isbnUnico = String.format("978%010d", Math.abs(System.nanoTime() % 10000000000L));

        LivroDTO livroDTO = new LivroDTO(
                null,
                "Grande Sertão: Veredas",
                isbnUnico,
                1956,
                editora.getId(),
                Arrays.asList(autor1.getId(), autor2.getId()),
                new java.util.ArrayList<>());

        // Act
        Livro livroSalvo = livroService.salvar(livroDTO);

        // Assert
        assertThat(livroSalvo.getId()).isNotNull();
        assertThat(livroSalvo.getTitulo()).isEqualTo("Grande Sertão: Veredas");
        assertThat(livroSalvo.getIsbn()).isEqualTo(isbnUnico);
        assertThat(livroSalvo.getDataPublicacao()).isEqualTo(1956);
        assertThat(livroSalvo.getEditora().getId()).isEqualTo(editora.getId());
        assertThat(livroSalvo.getAutores()).hasSize(2);
        
        boolean autor1Encontrado = false;
        boolean autor2Encontrado = false;
        
        for (Autor autor : livroSalvo.getAutores()) {
            if (autor.getId().equals(autor1.getId())) {
                autor1Encontrado = true;
            } else if (autor.getId().equals(autor2.getId())) {
                autor2Encontrado = true;
            }
        }
        
        assertThat(autor1Encontrado).isTrue();
        assertThat(autor2Encontrado).isTrue();
    }

    /**
     * Testa o método buscarPorId do LivroService.
     * Verifica se um livro é corretamente recuperado pelo seu ID.
     */
    @Test
    public void testBuscarPorId() {
        // Arrange
        Livro livro = criarLivroCompleto();

        Optional<Livro> resultado = livroService.buscarPorId(livro.getId());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Dom Casmurro");
    }

    /**
     * Testa o método buscarPorId do LivroService quando o ID não existe.
     * Verifica se o método retorna um Optional vazio quando o livro não é encontrado.
     */
    @Test
    public void testBuscarPorIdNaoEncontrado() {
        Optional<Livro> resultado = livroService.buscarPorId(999L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    /**
     * Testa o método buscarPorIsbn do LivroService.
     * Verifica se um livro é corretamente recuperado pelo seu ISBN.
     */
    @Test
    public void testBuscarPorIsbn() {
        // Arrange
        Livro livro = criarLivroCompleto();

        transaction.commit();
        transaction.begin();

        Optional<Livro> resultado = livroService.buscarPorIsbn(livro.getIsbn());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getTitulo()).isEqualTo("Dom Casmurro");
    }

    /**
     * Testa o método buscarTodos do LivroService.
     * Verifica se todos os livros são corretamente recuperados do banco de dados.
     */
    @Test
    public void testBuscarTodos() {
        criarLivroCompleto();
        criarOutroLivro();

        List<Livro> livros = livroService.buscarTodos();

        assertThat(livros).hasSize(2);
        assertThat(livros).extracting("titulo").contains("Dom Casmurro", "Memórias Póstumas de Brás Cubas");
    }

    /**
     * Testa o método remover do LivroService.
     * Verifica se um livro é corretamente removido do banco de dados.
     */
    @Test
    public void testRemover() {
        Livro livro = criarLivroCompleto();
        Long livroId = livro.getId();

        transaction.commit();
        entityManager.clear();
        transaction = entityManager.getTransaction();
        transaction.begin();

        Livro livroGerenciado = entityManager.find(Livro.class, livroId);
        assertThat(livroGerenciado).isNotNull();

        LivroDTO livroDTO = new LivroDTO(
                livroGerenciado.getId(),
                livroGerenciado.getTitulo(),
                livroGerenciado.getIsbn(),
                livroGerenciado.getDataPublicacao(),
                livroGerenciado.getEditora().getId(),
                Arrays.asList(livroGerenciado.getAutores().iterator().next().getId()),
                new java.util.ArrayList<>());

        livroService.remover(livroDTO);

        transaction.commit();
        transaction = entityManager.getTransaction();
        transaction.begin();

        Optional<Livro> resultado = livroService.buscarPorId(livroId);

        // Assert
        assertThat(resultado).isEmpty();
    }


    /**
     * Método auxiliar para criar uma editora para os testes.
     *
     * @return uma instância de Editora salva no banco de dados
     */
    private Editora criarEditora() {
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhiadasletras.com");
        return editoraRepository.save(editora);
    }

    /**
     * Método auxiliar para criar um autor para os testes.
     *
     * @return uma instância de Autor salva no banco de dados
     */
    private Autor criarAutor() {
        Autor autor = new Autor("Machado de Assis", "123.456.789-00", "(21) 99999-9999", "machado@exemplo.com");
        return autorRepository.save(autor);
    }
    
    /**
     * Método auxiliar para criar um autor alternativo para os testes.
     *
     * @return uma instância de Autor salva no banco de dados
     */
    private Autor criarOutroAutor() {
        Autor autor = new Autor("Guimarães Rosa", "987.654.321-00", "(31) 88888-8888", "rosa@exemplo.com");
        return autorRepository.save(autor);
    }

    /**
     * Método auxiliar para criar um livro completo para os testes.
     * Cria um livro com editora e autor associados.
     *
     * @return uma instância de Livro salva no banco de dados
     */
    private Livro criarLivroCompleto() {
        Editora editora = criarEditora();
        Autor autor = criarAutor();

        // Usar um ISBN único para evitar conflitos
        String isbnUnico = String.format("978%010d", Math.abs(System.nanoTime() % 10000000000L));

        Livro livro = new Livro("Dom Casmurro", isbnUnico, 1899);
        livro.setEditora(editora);
        livro.addAutor(autor);

        return livroRepository.save(livro);
    }

    /**
     * Método auxiliar para criar um livro alternativo para os testes.
     * Reutiliza editora e autor existentes se possível.
     *
     * @return uma instância de Livro salva no banco de dados
     */
    private Livro criarOutroLivro() {
        Editora editora = entityManager.find(Editora.class, 1L);
        if (editora == null) {
            editora = criarEditora();
        }

        Autor autor = entityManager.find(Autor.class, 1L);
        if (autor == null) {
            autor = criarAutor();
        }

        String isbnUnico = String.format("978%010d", Math.abs((System.nanoTime() + 1) % 10000000000L));

        Livro livro = new Livro("Memórias Póstumas de Brás Cubas", isbnUnico, 1881);
        livro.setEditora(editora);
        livro.addAutor(autor);

        return livroRepository.save(livro);
    }
}
