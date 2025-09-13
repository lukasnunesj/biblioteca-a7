package com.biblioteca.domain.entities.editora.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.editora.Editora;

public interface IEditoraRepository {

    Editora salvar(Editora editora);

    Optional<Editora> buscarPorId(Long id);

    Optional<Editora> buscarPorCnpj(String cnpj);

    List<Editora> buscarTodos();

    void remover(Editora editora);
}
