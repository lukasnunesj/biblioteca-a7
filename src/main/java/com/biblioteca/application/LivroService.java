package com.biblioteca.application;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;

/**
 * Implementação da interface ILivroService.
 * <p>
 * Esta classe fornece a implementação concreta das operações de negócio
 * relacionadas à entidade Livro, coordenando a interação entre os repositórios
 * de Livro, Editora e Autor.
 * </p>
 * 
 * @author Biblioteca A7
 * @version 1.0
 */
public class LivroService implements ILivroService {

    private final ILivroRepository livroRepository;
    private final IEditoraService editoraService;
    private final IAutorService autorService;

    public LivroService(ILivroRepository livroRepository, IEditoraService editoraService,
            IAutorService autorService) {
        this.livroRepository = livroRepository;
        this.editoraService = editoraService;
        this.autorService = autorService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Livro salvar(LivroDTO livroDTO) {
        Editora editora = editoraService.buscarPorId(livroDTO.getEditoraId())
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada!"));

        HashSet<Autor> autores = new HashSet<>();
        for (Long autorId : livroDTO.getAutoresIds()) {
            autores.add(autorService.buscarPorId(autorId)
                    .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado!")));
        }

        Livro livro = livroDTO.toEntity(editora, autores);
        return livroRepository.salvar(livro);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.buscarPorId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Livro> buscarPorIsbn(String isbn) {
        return livroRepository.buscarPorIsbn(isbn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Livro> buscarTodos() {
        return livroRepository.buscarTodos();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remover(LivroDTO livroDTO) {
        Optional<Livro> livro = livroRepository.buscarPorId(livroDTO.getId());
        if (livro.isPresent()) {
            livroRepository.remover(livro.get());
        }
    }

}
