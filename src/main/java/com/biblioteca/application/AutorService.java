package com.biblioteca.application;

import java.util.List;
import java.util.Optional;


import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;

public class AutorService implements IAutorService {

    private final IAutorRepository autorRepository;

    public AutorService(IAutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Override
    public Autor salvar(AutorDTO autorDTO) {
        Autor autor = autorDTO.toEntity();
        return autorRepository.salvar(autor);
    }

    @Override
    public Optional<Autor> buscarPorId(Long id) {
        return autorRepository.buscarPorId(id);
    }

    @Override
    public Optional<Autor> buscarPorCpfcnpj(String cpfcnpj) {
        return autorRepository.buscarPorCpfcnpj(cpfcnpj);
    }

    @Override
    public List<Autor> buscarTodos() {
        return autorRepository.buscarTodos();
    }

    @Override
    public void remover(AutorDTO autorDTO) {
        Autor autor = autorDTO.toEntity();
        autorRepository.remover(autor);
    }
}
