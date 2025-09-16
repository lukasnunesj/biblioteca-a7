package com.biblioteca.infrastructure.repositories;

import java.util.Optional;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;
import com.biblioteca.infrastructure.exceptions.PersistenciaException;

public class AutorRepository extends BaseRepository<Autor, Long> implements IAutorRepository {

    private static final Logger LOGGER = Logger.getLogger(AutorRepository.class.getName());

    public AutorRepository() {
        super(Autor.class);
    }
    
    public AutorRepository(EntityManager entityManager) {
        super(Autor.class);
        // Este construtor é mantido para compatibilidade com o código existente
        // O EntityManager agora é gerenciado pelo BaseRepository
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Autor> buscarPorCpfcnpj(String cpfcnpj) {
        try {
            LOGGER.info("Buscando autor por cpfcnpj: " + cpfcnpj);
            return executeInTransaction(em -> {
                return em.createQuery("SELECT a FROM Autor a WHERE a.cpfcnpj = :cpfcnpj", Autor.class)
                        .setParameter("cpfcnpj", cpfcnpj)
                        .getResultStream()
                        .findFirst();
            });
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar autor por cpfcnpj: " + e.getMessage());
            throw new PersistenciaException("Erro ao buscar autor por cpfcnpj", e);
        }
    }

}
