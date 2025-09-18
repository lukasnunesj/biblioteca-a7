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

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;
import com.biblioteca.infrastructure.exceptions.PersistenciaException;

@Stateless
public class AutorRepository extends BaseRepository<Autor, Long> implements IAutorRepository {

    private static final Logger LOGGER = Logger.getLogger(AutorRepository.class.getName());

    public AutorRepository() {
        super(Autor.class);
    }

    public AutorRepository(jakarta.persistence.EntityManager entityManager) {
        super(Autor.class);
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Autor> buscarPorNome(String nome) {
        try {
            LOGGER.info("Buscando autor por nome: " + nome);
            TypedQuery<Autor> query = entityManager.createQuery("SELECT a FROM Autor a WHERE a.nome = :nome",
                    Autor.class);
            query.setParameter("nome", nome);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar autor por nome: " + e.getMessage());
            throw new PersistenciaException("Erro ao buscar autor por nome", e);
        }
    }

    @Override
    public Optional<Autor> buscarPorCpfcnpj(String cpfcnpj) {
        try {
            LOGGER.info("Buscando autor por CPF/CNPJ: " + cpfcnpj);
            TypedQuery<Autor> query = entityManager.createQuery("SELECT a FROM Autor a WHERE a.cpfcnpj = :cpfcnpj",
                    Autor.class);
            query.setParameter("cpfcnpj", cpfcnpj);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar autor por CPF/CNPJ: " + e.getMessage());
            throw new PersistenciaException("Erro ao buscar autor por CPF/CNPJ", e);
        }
    }

    @Override
    public List<Autor> findByTermo(String termo) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Autor> cq = cb.createQuery(Autor.class);
            Root<Autor> autor = cq.from(Autor.class);

            String termoLike = "%" + termo.toLowerCase() + "%";

            Predicate predicado = cb.or(
                cb.like(cb.lower(autor.get("nome")), termoLike),
                cb.like(cb.lower(autor.get("cpfcnpj")), termoLike),
                cb.like(cb.lower(autor.get("telefone")), termoLike),
                cb.like(cb.lower(autor.get("email")), termoLike)
            );

            cq.select(autor).where(predicado).distinct(true);

            return entityManager.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar autores por termo: " + e.getMessage());
        }
    }
}
