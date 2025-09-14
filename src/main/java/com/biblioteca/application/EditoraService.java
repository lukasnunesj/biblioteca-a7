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

    @Override
    public Editora salvar(EditoraDTO editoraDTO) {
        Editora editora = editoraDTO.toEntity();
        return editoraRepository.salvar(editora);
    }

    @Override
    public Optional<Editora> buscarPorId(Long id) {
        return editoraRepository.buscarPorId(id);
    }

    @Override
    public Optional<Editora> buscarPorCnpj(String cnpj) {
        return editoraRepository.buscarPorCnpj(cnpj);
    }

    @Override
    public List<Editora> buscarTodos() {
        return editoraRepository.buscarTodos();
    }

    @Override
    public void remover(EditoraDTO editoraDTO) {
        Editora editora = editoraDTO.toEntity();
        editoraRepository.remover(editora);
    }
}
