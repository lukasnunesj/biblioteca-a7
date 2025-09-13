package com.biblioteca.repositories;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraRepository;

@Stateless
public class EditoraRepository implements IEditoraRepository {

    private static final Logger LOGGER = Logger.getLogger(EditoraRepository.class.getName());

    @PersistenceContext(unitName = "bibliotecaPU")
    private EntityManager entityManager;

    @Override
    public Editora salvar(Editora editora) {
        try {
            LOGGER.info("Salvando editora: " + editora);
            Editora editoraSalva = entityManager.merge(editora);
            entityManager.flush();
            LOGGER.info("Editora salva");
            return editoraSalva;
        } catch (Exception e) {
            LOGGER.severe("Erro ao salvar editora: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar editora: " + e.getMessage());
        }
    }

    @Override
    public Optional<Editora> buscarPorId(Long id) {
        try {
            LOGGER.info("Buscando editora por id: " + id);
            return Optional.ofNullable(entityManager.find(Editora.class, id));
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar editora por id: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar editora por id: " + e.getMessage());
        }
    }

    @Override
    public Optional<Editora> buscarPorCnpj(String cnpj) {
        try {
            LOGGER.info("Buscando editora por cnpj: " + cnpj);
            return Optional.ofNullable(entityManager.find(Editora.class, cnpj));
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar editora por cnpj: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar editora por cnpj: " + e.getMessage());
        }
    }

    @Override
    public List<Editora> buscarTodos() {
        try {
            LOGGER.info("Buscando todas as editoras");
            return entityManager.createQuery("SELECT e FROM Editora e", Editora.class).getResultList();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar todas as editoras: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar todas as editoras: " + e.getMessage());
        }
    }

    @Override
    public void remover(Editora editora) {
        try {
            LOGGER.info("Removendo editora: " + editora);
            entityManager.remove(editora);
        } catch (Exception e) {
            LOGGER.severe("Erro ao remover editora: " + e.getMessage());
            throw new RuntimeException("Erro ao remover editora: " + e.getMessage());
        }
    }
}
