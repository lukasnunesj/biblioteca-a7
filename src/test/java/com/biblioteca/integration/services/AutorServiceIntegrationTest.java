package com.biblioteca.integration.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.biblioteca.application.AutorService;
import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.infrastructure.repositories.AutorRepository;
import com.biblioteca.integration.IntegrationTestBase;

public class AutorServiceIntegrationTest extends IntegrationTestBase {

    private AutorService autorService;
    private AutorRepository autorRepository;

    @Override
    protected void beforeEachTest() {
        autorRepository = new AutorRepository(entityManager);
        autorService = new AutorService(autorRepository);
    }

    @Test
    public void testSalvar() {
        // Arrange
        AutorDTO autorDTO = new AutorDTO(null, "Carlos Drummond", "123.456.789-00", "(31) 99999-9999",
                "carlos@exemplo.com");

        // Act
        Autor autorSalvo = autorService.salvar(autorDTO);

        // Assert
        assertThat(autorSalvo.getId()).isNotNull();
        assertThat(autorSalvo.getNome()).isEqualTo("Carlos Drummond");
        assertThat(autorSalvo.getCpfcnpj()).isEqualTo("123.456.789-00");
        assertThat(autorSalvo.getTelefone()).isEqualTo("(31) 99999-9999");
        assertThat(autorSalvo.getEmail()).isEqualTo("carlos@exemplo.com");
    }

    @Test
    public void testBuscarPorId() {
        // Arrange
        Autor autor = criarAutor();

        // Act
        Optional<Autor> resultado = autorService.buscarPorId(autor.getId());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Carlos Drummond");
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        // Act
        Optional<Autor> resultado = autorService.buscarPorId(999L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarPorCpfcnpj() {
        // Arrange
        Autor autor = criarAutor();

        // Garantir que a entidade esteja persistida
        transaction.commit();
        transaction.begin();

        // Act
        Optional<Autor> resultado = autorService.buscarPorCpfcnpj(autor.getCpfcnpj());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Carlos Drummond");
    }

    @Test
    public void testBuscarTodos() {
        // Arrange
        criarAutor();
        criarOutroAutor();

        // Act
        List<Autor> autores = autorService.buscarTodos();

        // Assert
        assertThat(autores).hasSize(2);
        assertThat(autores).extracting("nome").contains("Carlos Drummond", "Machado de Assis");
    }

    @Test
    public void testRemover() {
        // Arrange
        Autor autor = criarAutor();
        Long autorId = autor.getId();

        // Garantir que a entidade esteja persistida
        transaction.commit();
        entityManager.clear();
        transaction = entityManager.getTransaction();
        transaction.begin();

        // Buscar a entidade novamente para garantir que está gerenciada
        Autor autorGerenciado = entityManager.find(Autor.class, autorId);
        assertThat(autorGerenciado).isNotNull();

        AutorDTO autorDTO = new AutorDTO(
                autorGerenciado.getId(),
                autorGerenciado.getNome(),
                autorGerenciado.getCpfcnpj(),
                autorGerenciado.getTelefone(),
                autorGerenciado.getEmail());

        // Act
        autorService.remover(autorDTO);

        // Commit para confirmar a remoção
        transaction.commit();
        transaction = entityManager.getTransaction();
        transaction.begin();

        // Verificar se foi removido
        Optional<Autor> resultado = autorService.buscarPorId(autorId);

        // Assert
        assertThat(resultado).isEmpty();
    }

    // Métodos auxiliares

    private Autor criarAutor() {
        Autor autor = new Autor("Carlos Drummond", "123.456.789-00", "(31) 99999-9999", "carlos@exemplo.com");
        return autorRepository.salvar(autor);
    }

    private Autor criarOutroAutor() {
        Autor autor = new Autor("Machado de Assis", "987.654.321-00", "(21) 88888-8888", "machado@exemplo.com");
        return autorRepository.salvar(autor);
    }
}
