package com.biblioteca.infrastructure.repositories;

import java.util.Optional;
import java.util.logging.Logger;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

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

}
