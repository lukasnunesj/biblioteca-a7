package com.biblioteca.domain.entities.autor.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;

public interface IAutorService {

    Autor salvar(AutorDTO autorDTO);

    Optional<Autor> buscarPorId(Long id);

    Optional<Autor> buscarPorCpfcnpj(String cpfcnpj);

    List<Autor> buscarTodos();

    void remover(AutorDTO autorDTO);
}
