package com.biblioteca.domain.entities.editora.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;

public interface IEditoraService {
    Editora salvar(EditoraDTO editoraDTO);

    Optional<Editora> buscarPorId(Long id);

    Optional<Editora> buscarPorCnpj(String cnpj);

    List<Editora> buscarTodos();

    void remover(EditoraDTO editoraDTO);
}
