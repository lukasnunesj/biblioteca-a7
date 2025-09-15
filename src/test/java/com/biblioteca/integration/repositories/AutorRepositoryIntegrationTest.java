package com.biblioteca.integration.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.infrastructure.repositories.AutorRepository;
import com.biblioteca.integration.IntegrationTestBase;

public class AutorRepositoryIntegrationTest extends IntegrationTestBase {

    private AutorRepository autorRepository;

    @Override
    protected void beforeEachTest() {
        autorRepository = new AutorRepository(entityManager);
    }

    @Test
    public void testSalvarAutor() {
        // Arrange
        Autor autor = new Autor("Carlos Drummond", "123.456.789-00", "(31) 99999-9999", "carlos@exemplo.com");

        // Act
        Autor autorSalvo = autorRepository.salvar(autor);
        
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
        Autor autor = new Autor("Carlos Drummond", "123.456.789-00", "(31) 99999-9999", "carlos@exemplo.com");
        Autor autorSalvo = autorRepository.salvar(autor);
        
        // Act
        Optional<Autor> resultado = autorRepository.buscarPorId(autorSalvo.getId());
        
        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Carlos Drummond");
    }

    @Test
    public void testBuscarPorIdNaoEncontrado() {
        // Act
        Optional<Autor> resultado = autorRepository.buscarPorId(999L);
        
        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    public void testBuscarTodos() {
        // Arrange
        Autor autor1 = new Autor("Carlos Drummond", "123.456.789-00", "(31) 99999-9999", "carlos@exemplo.com");
        Autor autor2 = new Autor("Machado de Assis", "987.654.321-00", "(21) 88888-8888", "machado@exemplo.com");
        
        autorRepository.salvar(autor1);
        autorRepository.salvar(autor2);
        
        // Act
        List<Autor> autores = autorRepository.buscarTodos();
        
        // Assert
        assertThat(autores).hasSize(2);
        assertThat(autores).extracting("nome").contains("Carlos Drummond", "Machado de Assis");
    }

    @Test
    public void testRemover() {
        // Arrange
        Autor autor = new Autor("Carlos Drummond", "123.456.789-00", "(31) 99999-9999", "carlos@exemplo.com");
        Autor autorSalvo = autorRepository.salvar(autor);
        
        // Act
        autorRepository.remover(autorSalvo);
        Optional<Autor> resultado = autorRepository.buscarPorId(autorSalvo.getId());
        
        // Assert
        assertThat(resultado).isEmpty();
    }
}
