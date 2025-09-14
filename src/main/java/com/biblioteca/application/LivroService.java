package com.biblioteca.application;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorRepository;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraRepository;
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
    private final IEditoraRepository editoraRepository;
    private final IAutorRepository autorRepository;

    public LivroService(ILivroRepository livroRepository, IEditoraRepository editoraRepository,
            IAutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.editoraRepository = editoraRepository;
        this.autorRepository = autorRepository;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que busca a editora e os autores associados ao livro
     * antes de salvá-lo no repositório.
     * </p>
     */
    @Override
    public Livro salvar(LivroDTO livroDTO) {
        Optional<Editora> editora = editoraRepository.buscarPorId(livroDTO.getEditoraId());

        HashSet<Autor> autores = new HashSet<>();
        for (Long autorId : livroDTO.getAutoresIds()) {
            autores.add(autorRepository.buscarPorId(autorId).get());
        }

        Livro livro = livroDTO.toEntity(editora.get(), autores);
        return livroRepository.salvar(livro);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que delega a busca ao repositório de livros.
     * </p>
     */
    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.buscarPorId(id);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que delega a busca ao repositório de livros.
     * </p>
     */
    @Override
    public Optional<Livro> buscarPorIsbn(String isbn) {
        return livroRepository.buscarPorIsbn(isbn);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que delega a busca ao repositório de livros.
     * </p>
     */
    @Override
    public List<Livro> buscarTodos() {
        return livroRepository.buscarTodos();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementação que converte o DTO para entidade e delega
     * a remoção ao repositório de livros.
     * </p>
     */
    @Override
    public void remover(LivroDTO livroDTO) {
        Livro livro = livroDTO.toEntity(editoraRepository.buscarPorId(livroDTO.getEditoraId()).get(), new HashSet<>());
        livroRepository.remover(livro);
    }

}
