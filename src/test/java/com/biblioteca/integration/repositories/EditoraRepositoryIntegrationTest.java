package com.biblioteca.integration.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.infrastructure.repositories.EditoraRepository;
import com.biblioteca.integration.IntegrationTestBase;

public class EditoraRepositoryIntegrationTest extends IntegrationTestBase {

    private EditoraRepository editoraRepository;

    @Override
    protected void beforeEachTest() {
        editoraRepository = new EditoraRepository(entityManager);
    }

    @Test
    public void testSalvarEditora() {
        // Arrange
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhiadasletras.com");

        // Act
        Editora editoraSalva = editoraRepository.save(editora);

        // Assert
        assertThat(editoraSalva.getId()).isNotNull();
        assertThat(editoraSalva.getNome()).isEqualTo("Companhia das Letras");
        assertThat(editoraSalva.getCnpj()).isEqualTo("12345678901234");
        assertThat(editoraSalva.getTelefone()).isEqualTo("(11) 99999-9999");
        assertThat(editoraSalva.getEmail()).isEqualTo("contato@companhiadasletras.com");
    }

    @Test
    public void testBuscarPorId() {
        // Arrange
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhiadasletras.com");
        Editora editoraSalva = editoraRepository.save(editora);

        // Act
        Optional<Editora> resultado = editoraRepository.findById(editoraSalva.getId());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Companhia das Letras");
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        // Act
        Optional<Editora> resultado = editoraRepository.findById(999L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarTodos() {
        // Arrange
        Editora editora1 = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhiadasletras.com");
        Editora editora2 = new Editora("Rocco", "98765432109876", "(21) 88888-8888", "contato@rocco.com.br");

        editoraRepository.save(editora1);
        editoraRepository.save(editora2);

        // Act
        List<Editora> editoras = editoraRepository.findAll();

        // Assert
        assertThat(editoras).hasSize(2);
        assertThat(editoras).extracting("nome").contains("Companhia das Letras", "Rocco");
    }

    @Test
    public void testRemover() {
        // Arrange
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhiadasletras.com");
        Editora editoraSalva = editoraRepository.save(editora);

        // Act
        editoraRepository.delete(editoraSalva);
        Optional<Editora> resultado = editoraRepository.findById(editoraSalva.getId());

        // Assert
        assertThat(resultado).isEmpty();
    }
}
