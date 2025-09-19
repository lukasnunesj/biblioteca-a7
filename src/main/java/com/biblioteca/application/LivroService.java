package com.biblioteca.application;

import com.biblioteca.application.livro.service.OpenLibraryService;
import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;
import com.biblioteca.infrastructure.exceptions.RecursoNaoEncontradoException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
public class LivroService implements ILivroService {

    private static final Logger LOGGER = Logger.getLogger(LivroService.class.getName());

    @Inject
    private ILivroRepository livroRepository;

    @Inject
    private IEditoraService editoraService;

    @Inject
    private IAutorService autorService;

    @Inject
    private OpenLibraryService openLibraryService;

    public LivroService() {
    }

    public LivroService(ILivroRepository livroRepository, IEditoraService editoraService, IAutorService autorService, OpenLibraryService openLibraryService) {
        this.livroRepository = livroRepository;
        this.editoraService = editoraService;
        this.autorService = autorService;
        this.openLibraryService = openLibraryService;
    }

    @Override
    public Livro salvar(LivroDTO livroDTO) {
        LOGGER.info("Salvando livro: " + livroDTO.getTitulo());
        livroDTO.validar();

        Editora editora = editoraService.buscarPorId(livroDTO.getEditoraId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Editora com ID " + livroDTO.getEditoraId() + " não encontrada."));

        Set<Autor> autores = new HashSet<>();
        if (livroDTO.getAutoresIds() != null) {
            for (Long autorId : livroDTO.getAutoresIds()) {
                Autor autor = autorService.buscarPorId(autorId)
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Autor com ID " + autorId + " não encontrado."));
                autores.add(autor);
            }
        }

        Livro livro;
        if (livroDTO.getId() != null) {
            livro = livroRepository.findById(livroDTO.getId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Livro com ID " + livroDTO.getId() + " não encontrado."));
            livro.setTitulo(livroDTO.getTitulo());
            livro.setIsbn(livroDTO.getIsbn());
            livro.setDataPublicacao(livroDTO.getDataPublicacao());
            livro.setEditora(editora);
            livro.setAutores(autores);
        } else {
            livro = livroDTO.toEntity(editora, autores);
        }

        Set<Livro> livrosSemelhantes = new HashSet<>();
        if (livroDTO.getLivrosSemelhantesIds() != null) {
            for (Long semelhanteId : livroDTO.getLivrosSemelhantesIds()) {
                Livro semelhante = livroRepository.findById(semelhanteId)
                        .orElseThrow(() -> new RecursoNaoEncontradoException("Livro semelhante com ID " + semelhanteId + " não encontrado."));
                livrosSemelhantes.add(semelhante);
            }
        }
        livro.setLivrosSemelhantes(livrosSemelhantes);
        System.err.println("Livro salvo: " + livro);
        return livroRepository.save(livro);
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        LOGGER.info("Buscando livro por ID: " + id);
        return livroRepository.findById(id);
    }

    @Override
    public Optional<Livro> buscarPorIsbn(String isbn) {
        LOGGER.info("Buscando livro por ISBN: " + isbn);
        // Normalizar o ISBN removendo os hífens antes de chamar o repositório
        String isbnNormalizado = isbn != null ? isbn.replace("-", "") : isbn;
        return livroRepository.buscarPorIsbn(isbnNormalizado);
    }

    @Override
    public List<Livro> buscarTodos() {
        LOGGER.info("Buscando todos os livros.");
        return livroRepository.findAll();
    }

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

    @Override
    public List<Livro> findByTermo(String termo) {
        LOGGER.info("Buscando livros por termo: " + termo);
        return livroRepository.findByTermo(termo);
    }
}
