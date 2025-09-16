package com.biblioteca.infrastructure.repositories;

import com.biblioteca.domain.entities.common.interfaces.IBaseRepository;
import com.biblioteca.infrastructure.exceptions.PersistenciaException;
import com.biblioteca.infrastructure.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Implementação base para repositórios JPA.
 * 
 * @param <T> Tipo da entidade
 * @param <ID> Tipo do identificador da entidade
 */
public abstract class BaseRepository<T, ID> implements IBaseRepository<T, ID> {
    
    protected final Class<T> entityClass;
    
    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    /**
     * Executa uma operação dentro de uma transação.
     */
    protected <R> R executeInTransaction(Function<EntityManager, R> operation) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            R result = operation.apply(em);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new PersistenciaException("Erro ao executar operação no banco de dados", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public T save(T entity) {
        return executeInTransaction(em -> {
            return em.merge(entity);
        });
    }
    
    @Override
    public Optional<T> findById(ID id) {
        return executeInTransaction(em -> {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        });
    }
    
    @Override
    public List<T> findAll() {
        return executeInTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root);
            return em.createQuery(cq).getResultList();
        });
    }
    
    @Override
    public void delete(T entity) {
        executeInTransaction(em -> {
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            return null;
        });
    }
    
    @Override
    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }
    
    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }
    
    @Override
    public long count() {
        return executeInTransaction(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            cq.select(cb.count(cq.from(entityClass)));
            return em.createQuery(cq).getSingleResult();
        });
    }
}
