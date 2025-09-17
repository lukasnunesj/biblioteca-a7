package com.biblioteca.infrastructure.repositories;

import java.util.Optional;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraRepository;

@Stateless
public class EditoraRepository extends BaseRepository<Editora, Long> implements IEditoraRepository {

    private static final Logger LOGGER = Logger.getLogger(EditoraRepository.class.getName());

    public EditoraRepository() {
        super(Editora.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Editora> buscarPorCnpj(String cnpj) {
        try {
            LOGGER.info("Buscando editora por cnpj: " + cnpj);
            TypedQuery<Editora> query = entityManager.createQuery("SELECT e FROM Editora e WHERE e.cnpj = :cnpj", Editora.class);
            query.setParameter("cnpj", cnpj);
            return query.getResultStream().findFirst();
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar editora por cnpj: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar editora por cnpj: " + e.getMessage());
        }
    }

}
