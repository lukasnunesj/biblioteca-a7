package com.biblioteca.infrastructure.repositories;

import com.biblioteca.domain.entities.common.interfaces.IBaseRepository;
import com.biblioteca.infrastructure.exceptions.PersistenciaException;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Implementação base para repositórios JPA.
 * 
 * @param <T>  Tipo da entidade
 * @param <ID> Tipo do identificador da entidade
 */
@Stateless
public abstract class BaseRepository<T, ID> implements IBaseRepository<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    protected final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    @Transactional
    public T save(T entity) {
        try {
            return entityManager.merge(entity);
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao salvar entidade: " + entity.getClass().getSimpleName(), e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            return Optional.ofNullable(entityManager.find(entityClass, id));
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao buscar entidade por ID: " + id, e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root);
            return entityManager.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao buscar todas as entidades: " + entityClass.getSimpleName(), e);
        }
    }

    @Override
    @Transactional
    public void delete(T entity) {
        try {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao deletar entidade: " + entity.getClass().getSimpleName(), e);
        }
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        findById(id).ifPresent(this::delete);
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            cq.select(cb.count(cq.from(entityClass)));
            return entityManager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao contar entidades: " + entityClass.getSimpleName(), e);
        }
    }
}
