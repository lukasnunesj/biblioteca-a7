package com.biblioteca.application;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.infrastructure.exceptions.RecursoNaoEncontradoException;

@Stateless
public class AutorService implements IAutorService {

    private static final Logger LOGGER = Logger.getLogger(AutorService.class.getName());

    @Inject
    private IAutorRepository autorRepository;
    
    /**
     * Construtor padrão para CDI
     */
    public AutorService() {
        // Construtor vazio para CDI
    }
    
    /**
     * Construtor para testes com injeção manual
     */
    public AutorService(IAutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Autor salvar(AutorDTO autorDTO) {
        LOGGER.info("Salvando autor: " + autorDTO.getNome());

        // Valida os dados do DTO antes de prosseguir
        autorDTO.validar();

        // Valida duplicidade por nome
        Optional<Autor> autorPorNome = autorRepository.buscarPorNome(autorDTO.getNome());
        if (autorPorNome.isPresent() && (autorDTO.getId() == null || !autorDTO.getId().equals(autorPorNome.get().getId()))) {
            throw new IllegalArgumentException("Já existe um autor cadastrado com este nome.");
        }


        Autor autor = autorDTO.toEntity();
        return autorRepository.save(autor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Autor> buscarPorId(Long id) {
        LOGGER.info("Buscando autor por ID: " + id);
        return autorRepository.findById(id);
    }


    @Override
    public Optional<Autor> buscarPorNome(String nome) {
        LOGGER.info("Buscando autor por nome: " + nome);
        return autorRepository.buscarPorNome(nome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Autor> buscarTodos() {
        LOGGER.info("Buscando todos os autores");
        return autorRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remover(AutorDTO autorDTO) {
        LOGGER.info("Removendo autor com ID: " + autorDTO.getId());

        if (autorDTO.getId() == null) {
            throw new IllegalArgumentException("ID do autor não pode ser nulo para remoção");
        }

        Optional<Autor> autor = autorRepository.findById(autorDTO.getId());
        if (autor.isPresent()) {
            autorRepository.delete(autor.get());
            LOGGER.info("Autor removido com sucesso");
        } else {
            throw new RecursoNaoEncontradoException("Autor com ID " + autorDTO.getId() + " não encontrado");
        }
    }

    @Override
    public List<Autor> findByTermo(String termo) {
        LOGGER.info("Buscando autores por termo: " + termo);
        return autorRepository.findByTermo(termo);
    }

}
