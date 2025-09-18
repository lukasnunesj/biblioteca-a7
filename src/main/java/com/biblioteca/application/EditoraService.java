package com.biblioteca.application;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.infrastructure.exceptions.RecursoNaoEncontradoException;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraRepository;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;

@Stateless
public class EditoraService implements IEditoraService {

    private static final Logger LOGGER = Logger.getLogger(EditoraService.class.getName());

    @Inject
    private IEditoraRepository editoraRepository;

    /**
     * Construtor para testes
     */
    public EditoraService() {
        // Construtor vazio para testes
    }
    
    /**
     * Construtor para testes com injeção manual
     */
    public EditoraService(IEditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Editora salvar(EditoraDTO editoraDTO) {
        LOGGER.info("Salvando editora: " + editoraDTO.getNome());
        editoraDTO.validar();

        // Valida duplicidade por nome
        Optional<Editora> editoraPorNome = editoraRepository.buscarPorNome(editoraDTO.getNome());
        if (editoraPorNome.isPresent() && (editoraDTO.getId() == null || !editoraDTO.getId().equals(editoraPorNome.get().getId()))) {
            throw new IllegalArgumentException("Já existe uma editora cadastrada com este nome.");
        }


        Editora editora = editoraDTO.toEntity();
        return editoraRepository.save(editora);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Editora> buscarPorId(Long id) {
        LOGGER.info("Buscando editora por ID: " + id);
        return editoraRepository.findById(id);
    }


    @Override
    public Optional<Editora> buscarPorNome(String nome) {
        LOGGER.info("Buscando editora por nome: " + nome);
        return editoraRepository.buscarPorNome(nome);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Editora> buscarTodos() {
        LOGGER.info("Buscando todas as editoras.");
        return editoraRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remover(EditoraDTO editoraDTO) {
        LOGGER.info("Removendo editora com ID: " + editoraDTO.getId());
        if (editoraDTO.getId() == null) {
            throw new IllegalArgumentException("ID da editora não pode ser nulo para remoção.");
        }

        Editora editora = editoraRepository.findById(editoraDTO.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Editora com ID " + editoraDTO.getId() + " não encontrada."));

        editoraRepository.delete(editora);
        LOGGER.info("Editora removida com sucesso.");
    }

    @Override
    public List<Editora> findByTermo(String termo) {
        LOGGER.info("Buscando editoras por termo: " + termo);
        return editoraRepository.findByTermo(termo);
    }

}
