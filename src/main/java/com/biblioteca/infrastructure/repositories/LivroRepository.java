package com.biblioteca.infrastructure.repositories;

import javax.persistence.EntityManager;

import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementação da interface ILivroRepository.
 * <p>
 * Esta classe fornece a implementação concreta das operações de persistência
 * para a entidade Livro, utilizando JPA/Hibernate.
 * </p>
 * 
 * @author Biblioteca A7
 * @version 1.0
 */
public class LivroRepository implements ILivroRepository {

    /**
     * Logger para registrar informações, avisos e erros.
     */
    private static final Logger LOGGER = Logger.getLogger(LivroRepository.class.getName());

    /**
     * Gerenciador de entidades JPA.
     * Responsável por realizar as operações de persistência.
     */
    private final EntityManager entityManager;

    public LivroRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que utiliza o método merge do EntityManager para
     * persistir ou atualizar um livro no banco de dados.
     * </p>
     */
    @Override
    public Livro salvar(Livro livro) {
        try {
            LOGGER.info("Salvando livro: " + livro);
            Livro livroSalvo = entityManager.merge(livro);
            entityManager.flush();
            LOGGER.info("Livro salvo");
            return livroSalvo;
        } catch (Exception e) {
            LOGGER.severe("Erro ao salvar livro: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar livro: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que utiliza o método find do EntityManager para
     * buscar um livro pelo seu ID no banco de dados.
     * </p>
     */
    @Override
    public Optional<Livro> buscarPorId(Long id) {
        try {
            LOGGER.info("Buscando livro por id: " + id);
            return Optional.ofNullable(entityManager.find(Livro.class, id));
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar livro por id: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar livro por id: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que utiliza o método find do EntityManager para
     * buscar um livro pelo seu ISBN no banco de dados.
     * </p>
     */
    @Override
    public Optional<Livro> buscarPorIsbn(String isbn) {
        try {
            LOGGER.info("Buscando livro por isbn: " + isbn);
            return Optional.ofNullable(entityManager.find(Livro.class, isbn));
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar livro por isbn: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar livro por isbn: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que utiliza uma consulta JPQL para
     * buscar todos os livros no banco de dados.
     * </p>
     */
    @Override
    public List<Livro> buscarTodos() {
        try {
            LOGGER.info("Buscando todos os livros");
            return entityManager.createQuery("SELECT l FROM Livro l", Livro.class).getResultList();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar todos os livros: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar todos os livros: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que utiliza o método remove do EntityManager para
     * remover um livro do banco de dados.
     * </p>
     */
    @Override
    public void remover(Livro livro) {
        try {
            LOGGER.info("Removendo livro: " + livro);
            entityManager.remove(livro);
        } catch (Exception e) {
            LOGGER.severe("Erro ao remover livro: " + e.getMessage());
            throw new RuntimeException("Erro ao remover livro: " + e.getMessage());
        }
    }

}
