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

        Optional<Editora> editoraExistente = editoraRepository.buscarPorCnpj(removerFormatacaoCnpj(editoraDTO.getCnpj()));
        if (editoraExistente.isPresent()
                && (editoraDTO.getId() == null || !editoraDTO.getId().equals(editoraExistente.get().getId()))) {
            throw new IllegalArgumentException("Já existe uma editora cadastrada com este CNPJ.");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Editora> buscarPorCnpj(String cnpj) {
        LOGGER.info("Buscando editora por CNPJ: " + cnpj);
        return editoraRepository.buscarPorCnpj(removerFormatacaoCnpj(cnpj));
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

    private String removerFormatacaoCnpj(String cnpj) {
        if (cnpj == null) {
            return null;
        }
        return cnpj.replaceAll("[./-]", "");
    }
}
