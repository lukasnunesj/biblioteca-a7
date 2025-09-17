package com.biblioteca.infrastructure.repositories;

import java.util.Optional;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;
import com.biblioteca.infrastructure.exceptions.PersistenciaException;

@Stateless
public class AutorRepository extends BaseRepository<Autor, Long> implements IAutorRepository {

    private static final Logger LOGGER = Logger.getLogger(AutorRepository.class.getName());

    public AutorRepository() {
        super(Autor.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Autor> buscarPorCpfcnpj(String cpfcnpj) {
        try {
            LOGGER.info("Buscando autor por cpfcnpj: " + cpfcnpj);
            TypedQuery<Autor> query = entityManager.createQuery("SELECT a FROM Autor a WHERE a.cpfcnpj = :cpfcnpj", Autor.class);
            query.setParameter("cpfcnpj", cpfcnpj);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar autor por cpfcnpj: " + e.getMessage());
            throw new PersistenciaException("Erro ao buscar autor por cpfcnpj", e);
        }
    }

}
