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

        // Verifica se já existe um autor com o mesmo CPF/CNPJ
        Optional<Autor> autorExistente = autorRepository.buscarPorCpfcnpj(removerFormatacaoCpfCnpj(autorDTO.getCpfcnpj()));
        if (autorExistente.isPresent()
                && (autorDTO.getId() == null || !autorDTO.getId().equals(autorExistente.get().getId()))) {
            throw new IllegalArgumentException("Já existe um autor cadastrado com este CPF/CNPJ");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Autor> buscarPorCpfcnpj(String cpfcnpj) {
        LOGGER.info("Buscando autor por CPF/CNPJ: " + cpfcnpj);
        return autorRepository.buscarPorCpfcnpj(removerFormatacaoCpfCnpj(cpfcnpj));
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

    private String removerFormatacaoCpfCnpj(String cpfcnpj) {
        if (cpfcnpj == null) {
            return null;
        }
        return cpfcnpj.replaceAll("[./-]", "");
    }
}
