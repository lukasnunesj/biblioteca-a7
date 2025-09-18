package com.biblioteca.integration.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.biblioteca.application.EditoraService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.infrastructure.repositories.EditoraRepository;
import com.biblioteca.integration.IntegrationTestBase;

public class EditoraServiceIntegrationTest extends IntegrationTestBase {

    private EditoraService editoraService;
    private EditoraRepository editoraRepository;

    @Override
    protected void beforeEachTest() {
        editoraRepository = new EditoraRepository(entityManager);
        editoraService = new EditoraService(editoraRepository);
    }

    @Test
    public void testSalvar() {
        // Arrange
        EditoraDTO editoraDTO = new EditoraDTO(null, "Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhiadasletras.com");

        // Act
        Editora editoraSalva = editoraService.salvar(editoraDTO);

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
        Editora editora = criarEditora();

        // Act
        Optional<Editora> resultado = editoraService.buscarPorId(editora.getId());

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Companhia das Letras");
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        // Act
        Optional<Editora> resultado = editoraService.buscarPorId(999L);

        // Assert
        assertThat(resultado).isEmpty();
    }


    @Test
    public void testBuscarTodos() {
        // Arrange
        criarEditora();
        criarOutraEditora();

        // Act
        List<Editora> editoras = editoraService.buscarTodos();

        // Assert
        assertThat(editoras).hasSize(2);
        assertThat(editoras).extracting("nome").contains("Companhia das Letras", "Rocco");
    }

    @Test
    public void testRemover() {
        // Arrange
        Editora editora = criarEditora();
        Long editoraId = editora.getId();

        // Garantir que a entidade esteja persistida
        transaction.commit();
        entityManager.clear();
        transaction = entityManager.getTransaction();
        transaction.begin();

        // Buscar a entidade novamente para garantir que está gerenciada
        Editora editoraGerenciada = entityManager.find(Editora.class, editoraId);
        assertThat(editoraGerenciada).isNotNull();

        EditoraDTO editoraDTO = new EditoraDTO(
                editoraGerenciada.getId(),
                editoraGerenciada.getNome(),
                editoraGerenciada.getCnpj(),
                editoraGerenciada.getTelefone(),
                editoraGerenciada.getEmail());

        // Act
        editoraService.remover(editoraDTO);

        // Commit para confirmar a remoção
        transaction.commit();
        transaction = entityManager.getTransaction();
        transaction.begin();

        // Verificar se foi removido
        Optional<Editora> resultado = editoraService.buscarPorId(editoraId);

        // Assert
        assertThat(resultado).isEmpty();
    }

    // Métodos auxiliares

    private Editora criarEditora() {
        Editora editora = new Editora("Companhia das Letras", "12345678901234", "(11) 99999-9999",
                "contato@companhiadasletras.com");
        return editoraRepository.save(editora);
    }

    private Editora criarOutraEditora() {
        Editora editora = new Editora("Rocco", "98765432109876", "(21) 88888-8888", "contato@rocco.com.br");
        return editoraRepository.save(editora);
    }
}
