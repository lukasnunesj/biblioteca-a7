package com.biblioteca.infrastructure.repositories;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;

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
@Stateless
public class LivroRepository extends BaseRepository<Livro, Long> implements ILivroRepository {

    /**
     * Logger para registrar informações, avisos e erros.
     */
    private static final Logger LOGGER = Logger.getLogger(LivroRepository.class.getName());

    public LivroRepository() {
        super(Livro.class);
    }

    public LivroRepository(jakarta.persistence.EntityManager entityManager) {
        super(Livro.class);
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Livro> buscarPorIsbn(String isbn) {
        try {
            LOGGER.info("Buscando livro por isbn: " + isbn);
            TypedQuery<Livro> query = entityManager.createQuery("SELECT l FROM Livro l WHERE l.isbn = :isbn",
                    Livro.class);
            query.setParameter("isbn", isbn);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar livro por isbn: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar livro por isbn: " + e.getMessage());
        }
    }

    /**
     * Busca todos os livros com carregamento antecipado (eager loading) de autores
     * e editora.
     * Isso evita o problema de LazyInitializationException quando a sessão do
     * Hibernate é fechada.
     *
     * @return Lista de livros com autores e editora carregados
     */
    @Override
    public Optional<Livro> findById(Long id) {
        try {
            LOGGER.info("Buscando livro por id com carregamento antecipado de autores e editora: " + id);
            TypedQuery<Livro> query = entityManager.createQuery(
                    "SELECT l FROM Livro l LEFT JOIN FETCH l.autores LEFT JOIN FETCH l.editora LEFT JOIN FETCH l.livrosSemelhantes WHERE l.id = :id",
                    Livro.class);
            query.setParameter("id", id);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar livro por id: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar livro por id: " + e.getMessage());
        }
    }

    @Override
    public List<Livro> findAll() {
        try {
            LOGGER.info("Buscando todos os livros com carregamento antecipado de autores e editora");
            System.out.println("Buscando todos os livros com carregamento antecipado de autores e editora");
            TypedQuery<Livro> query = entityManager.createQuery(
                    "SELECT DISTINCT l FROM Livro l LEFT JOIN FETCH l.autores LEFT JOIN FETCH l.editora LEFT JOIN FETCH l.livrosSemelhantes",
                    Livro.class);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar todos os livros: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar todos os livros: " + e.getMessage());
        }
    }

    @Override
    public List<Livro> findByTermo(String termo) {
        try {
            LOGGER.info("Buscando livros por termo: " + termo);
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Livro> cq = cb.createQuery(Livro.class);
            Root<Livro> livro = cq.from(Livro.class);
            livro.fetch("autores", jakarta.persistence.criteria.JoinType.LEFT);
            livro.fetch("editora", jakarta.persistence.criteria.JoinType.LEFT);
            livro.fetch("livrosSemelhantes", jakarta.persistence.criteria.JoinType.LEFT);

            String termoLike = "%" + termo.toLowerCase() + "%";

            Predicate predicado = cb.or(
                cb.like(cb.lower(livro.get("titulo")), termoLike),
                cb.like(cb.lower(livro.get("isbn")), termoLike),
                cb.like(cb.lower(livro.get("editora").get("nome")), termoLike),
                cb.like(cb.lower(livro.join("autores").get("nome")), termoLike)
            );

            cq.select(livro).where(predicado).distinct(true);

            return entityManager.createQuery(cq).getResultList();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar livros por termo: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar livros por termo: " + e.getMessage());
        }
    }

}
