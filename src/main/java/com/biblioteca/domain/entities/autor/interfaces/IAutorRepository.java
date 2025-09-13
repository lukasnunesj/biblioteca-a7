package com.biblioteca.domain.entities.autor.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.autor.Autor;

public interface IAutorRepository {

    Autor salvar(Autor autor);

    Optional<Autor> buscarPorId(Long id);

    Optional<Autor> buscarPorCpfcnpj(String cpfcnpj);

    List<Autor> buscarTodos();

    void remover(Autor autor);
}
