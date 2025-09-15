package com.biblioteca.application;

import java.util.List;
import java.util.Optional;


import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraRepository;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;

public class EditoraService implements IEditoraService {

    private final IEditoraRepository editoraRepository;

    public EditoraService(IEditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Editora salvar(EditoraDTO editoraDTO) {
        Editora editora = editoraDTO.toEntity();
        return editoraRepository.salvar(editora);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Editora> buscarPorId(Long id) {
        return editoraRepository.buscarPorId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Editora> buscarPorCnpj(String cnpj) {
        return editoraRepository.buscarPorCnpj(cnpj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Editora> buscarTodos() {
        return editoraRepository.buscarTodos();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remover(EditoraDTO editoraDTO) {
        Optional<Editora> editora = editoraRepository.buscarPorId(editoraDTO.getId());
        if (editora.isPresent()) {
            editoraRepository.remover(editora.get());
        }
    }
}
