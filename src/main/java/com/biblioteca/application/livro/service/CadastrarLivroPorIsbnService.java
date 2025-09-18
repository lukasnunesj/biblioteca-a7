package com.biblioteca.application.livro.service;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.DTO.OpenLibraryResponseDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
public class CadastrarLivroPorIsbnService {

    private static final Logger LOGGER = Logger.getLogger(CadastrarLivroPorIsbnService.class.getName());

    @Inject
    private ILivroRepository livroRepository;

    @Inject
    private IEditoraService editoraService;

    @Inject
    private IAutorService autorService;

    @Inject
    private OpenLibraryService openLibraryService;

    @Inject
    private ILivroService livroService;

    public Optional<Livro> execute(String isbn) {
        LOGGER.info("Executando caso de uso para cadastrar livro por ISBN: " + isbn);

        Optional<Livro> livroExistente = livroRepository.buscarPorIsbn(isbn);
        if (livroExistente.isPresent()) {
            LOGGER.info("Livro já existe no sistema com ISBN: " + isbn);
            return livroExistente;
        }

        return openLibraryService.buscarLivroPorIsbn(isbn).map(dadosLivro -> {
            try {
                LivroDTO livroDTO = new LivroDTO();
                livroDTO.setTitulo(dadosLivro.getTitle() != null ? dadosLivro.getTitle() : "Título não informado");
                livroDTO.setIsbn(isbn);

                if (dadosLivro.getPublishDate() != null) {
                    livroDTO.setDataPublicacao(parseDataPublicacao(dadosLivro.getPublishDate()));
                }

                if (dadosLivro.getPublishers() != null && !dadosLivro.getPublishers().isEmpty()) {
                    String nomeEditora = dadosLivro.getPublishers().get(0).getName();
                    Editora editora = buscarOuCriarEditora(nomeEditora);
                    livroDTO.setEditoraId(editora.getId());
                }

                Set<Long> autoresIds = new HashSet<>();
                if (dadosLivro.getAuthors() != null) {
                    for (OpenLibraryResponseDTO.Author author : dadosLivro.getAuthors()) {
                        String nomeAutor = author.getName();
                        if (nomeAutor != null && !nomeAutor.trim().isEmpty()) {
                            Autor autor = buscarOuCriarAutor(nomeAutor);
                            autoresIds.add(autor.getId());
                        }
                    }
                }
                livroDTO.setAutoresIds(new java.util.ArrayList<>(autoresIds));

                Livro livroSalvo = livroService.salvar(livroDTO);
                LOGGER.info("Livro cadastrado com sucesso: " + livroSalvo.getTitulo());
                return livroSalvo;

            } catch (Exception e) {
                LOGGER.severe("Erro ao processar e salvar livro do ISBN " + isbn + ": " + e.getMessage());
                return null;
            }
        });
    }

    private Integer parseDataPublicacao(String dataString) {
        String[] formatos = {"MMM yyyy", "MMMM yyyy", "dd MMM yyyy", "yyyy-MM-dd", "MM/dd/yyyy"};
        for (String formato : formatos) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
                return java.time.LocalDate.parse(dataString, formatter).getYear();
            } catch (DateTimeParseException e) {
                // Continua
            }
        }
        try {
            return Integer.parseInt(dataString);
        } catch (NumberFormatException e) {
            LOGGER.warning("Não foi possível parsear a data: " + dataString + ".");
        }
        return null;
    }

    private Editora buscarOuCriarEditora(String nomeEditora) {
        return editoraService.buscarPorNome(nomeEditora).orElseGet(() -> {
            EditoraDTO novaEditoraDTO = new EditoraDTO();
            novaEditoraDTO.setNome(nomeEditora);
            return editoraService.salvar(novaEditoraDTO);
        });
    }

    private Autor buscarOuCriarAutor(String nomeAutor) {
        return autorService.buscarPorNome(nomeAutor).orElseGet(() -> {
            AutorDTO novoAutorDTO = new AutorDTO();
            novoAutorDTO.setNome(nomeAutor);
            return autorService.salvar(novoAutorDTO);
        });
    }
}
