package com.biblioteca.infrastructure.repositories;

import java.util.Optional;
import java.util.logging.Logger;

import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraRepository;

/**
 * Implementação do repositório de editoras.
 * <p>
 * Esta classe fornece a implementação concreta das operações de persistência
 * para a entidade Editora, estendendo o repositório base e implementando a interface
 * IEditoraRepository.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
@Stateless
public class EditoraRepository extends BaseRepository<Editora, Long> implements IEditoraRepository {

    private static final Logger LOGGER = Logger.getLogger(EditoraRepository.class.getName());

    /**
     * Construtor padrão.
     * Inicializa o repositório com a classe da entidade Editora.
     */
    public EditoraRepository() {
        super(Editora.class);
    }

    /**
     * Construtor com injeção manual do EntityManager.
     * Útil para testes e casos especiais.
     *
     * @param entityManager o EntityManager a ser utilizado pelo repositório
     */
    public EditoraRepository(jakarta.persistence.EntityManager entityManager) {
        super(Editora.class);
        this.entityManager = entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Editora> buscarPorCnpj(String cnpj) {
        try {
            LOGGER.info("Buscando editora por cnpj: " + cnpj);
            TypedQuery<Editora> query = entityManager.createQuery("SELECT e FROM Editora e WHERE e.cnpj = :cnpj",
                    Editora.class);
            query.setParameter("cnpj", cnpj);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar editora por cnpj: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar editora por cnpj: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Editora> buscarPorNome(String nome) {
        try {
            LOGGER.info("Buscando editora por nome: " + nome);
            TypedQuery<Editora> query = entityManager.createQuery("SELECT e FROM Editora e WHERE e.nome = :nome",
                    Editora.class);
            query.setParameter("nome", nome);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar editora por nome: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar editora por nome: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Editora> findByTermo(String termo) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Editora> cq = cb.createQuery(Editora.class);
            Root<Editora> editora = cq.from(Editora.class);

            String termoLike = "%" + termo.toLowerCase() + "%";

            Predicate predicado = cb.or(
                cb.like(cb.lower(editora.get("nome")), termoLike),
                cb.like(cb.lower(editora.get("cnpj")), termoLike),
                cb.like(cb.lower(editora.get("telefone")), termoLike),
                cb.like(cb.lower(editora.get("email")), termoLike)
            );

            cq.select(editora).where(predicado).distinct(true);

            return entityManager.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar editoras por termo: " + e.getMessage());
        }
    }
}
