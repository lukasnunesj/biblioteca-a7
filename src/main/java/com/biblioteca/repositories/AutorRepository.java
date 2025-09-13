package com.biblioteca.repositories;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;

@Stateless
public class AutorRepository implements IAutorRepository {

    private static final Logger LOGGER = Logger.getLogger(AutorRepository.class.getName());

    @PersistenceContext(unitName = "bibliotecaPU")
    private EntityManager entityManager;

    @Override
    public Autor salvar(Autor autor) {
        try {
            LOGGER.info("Salvando autor: " + autor);
            Autor autorSalvo = entityManager.merge(autor);
            entityManager.flush();
            LOGGER.info("Autor salvo");
            return autorSalvo;
        } catch (Exception e) {
            LOGGER.severe("Erro ao salvar autor: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar autor: " + e.getMessage());
        }
    }

    @Override
    public Optional<Autor> buscarPorId(Long id) {
        try {
            LOGGER.info("Buscando autor por id: " + id);
            return Optional.ofNullable(entityManager.find(Autor.class, id));
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar autor por id: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar autor por id: " + e.getMessage());
        }
    }

    @Override
    public Optional<Autor> buscarPorCpfcnpj(String cpfcnpj) {
        try {
            LOGGER.info("Buscando autor por cpfcnpj: " + cpfcnpj);
            return Optional.ofNullable(entityManager.find(Autor.class, cpfcnpj));
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar autor por cpfcnpj: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar autor por cpfcnpj: " + e.getMessage());
        }
    }

    @Override
    public List<Autor> buscarTodos() {
        try {
            LOGGER.info("Buscando todos os autores");
            return entityManager.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar todos os autores: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar todos os autores: " + e.getMessage());
        }
    }

    @Override
    public void remover(Autor autor) {
        try {
            LOGGER.info("Removendo autor: " + autor);
            entityManager.remove(autor);
        } catch (Exception e) {
            LOGGER.severe("Erro ao remover autor: " + e.getMessage());
            throw new RuntimeException("Erro ao remover autor: " + e.getMessage());
        }
    }
}
