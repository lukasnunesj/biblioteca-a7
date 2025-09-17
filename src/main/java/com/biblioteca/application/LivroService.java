package com.biblioteca.application;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;
import com.biblioteca.infrastructure.exceptions.RecursoNaoEncontradoException;

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
@Stateless
public class LivroService implements ILivroService {

    private static final Logger LOGGER = Logger.getLogger(LivroService.class.getName());

    @Inject
    private ILivroRepository livroRepository;

    @Inject
    private IEditoraService editoraService;

    @Inject
    private IAutorService autorService;
    
    /**
     * Construtor padrão para CDI
     */
    public LivroService() {
        // Construtor vazio para CDI
    }
    
    /**
     * Construtor para testes com injeção manual
     */
    public LivroService(ILivroRepository livroRepository, IEditoraService editoraService, IAutorService autorService) {
        this.livroRepository = livroRepository;
        this.editoraService = editoraService;
        this.autorService = autorService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Livro salvar(LivroDTO livroDTO) {
        LOGGER.info("Salvando livro: " + livroDTO.getTitulo());
        livroDTO.validar();

        Editora editora = editoraService.buscarPorId(livroDTO.getEditoraId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Editora com ID " + livroDTO.getEditoraId() + " não encontrada."));

        Set<Autor> autores = new HashSet<>();
        for (Long autorId : livroDTO.getAutoresIds()) {
            Autor autor = autorService.buscarPorId(autorId)
                    .orElseThrow(
                            () -> new RecursoNaoEncontradoException("Autor com ID " + autorId + " não encontrado."));
            autores.add(autor);
        }

        Livro livro = livroDTO.toEntity(editora, autores);
        return livroRepository.save(livro);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Livro> buscarPorId(Long id) {
        LOGGER.info("Buscando livro por ID: " + id);
        return livroRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Livro> buscarPorIsbn(String isbn) {
        LOGGER.info("Buscando livro por ISBN: " + isbn);
        return livroRepository.buscarPorIsbn(isbn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Livro> buscarTodos() {
        LOGGER.info("Buscando todos os livros.");
        return livroRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remover(LivroDTO livroDTO) {
        LOGGER.info("Removendo livro com ID: " + livroDTO.getId());
        if (livroDTO.getId() == null) {
            throw new IllegalArgumentException("ID do livro não pode ser nulo para remoção.");
        }

        Livro livro = livroRepository.findById(livroDTO.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Livro com ID " + livroDTO.getId() + " não encontrado."));

        livroRepository.delete(livro);
        LOGGER.info("Livro removido com sucesso.");
    }

}
