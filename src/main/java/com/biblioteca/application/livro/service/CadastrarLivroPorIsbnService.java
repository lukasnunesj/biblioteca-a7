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

/**
 * Serviço responsável por cadastrar livros a partir de um ISBN.
 * <p>
 * Esta classe utiliza o serviço OpenLibrary para buscar informações de livros
 * pelo ISBN e cadastrá-los no sistema, incluindo seus autores e editora.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
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

    /**
     * Executa o caso de uso para cadastrar um livro a partir do ISBN.
     * <p>
     * Verifica se o livro já existe no sistema. Se não existir, busca as informações
     * na OpenLibrary e cadastra o livro, incluindo seus autores e editora.
     * </p>
     *
     * @param isbn o ISBN do livro a ser cadastrado
     * @return um Optional contendo o livro cadastrado, ou vazio se não foi possível cadastrar
     */
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

    /**
     * Converte uma string de data para o ano de publicação.
     * <p>
     * Tenta converter a string de data em vários formatos diferentes.
     * Se não conseguir, tenta interpretar a string como um número inteiro.
     * </p>
     *
     * @param dataString a string contendo a data de publicação
     * @return o ano de publicação como Integer, ou null se não for possível converter
     */
    private Integer parseDataPublicacao(String dataString) {
        String[] formatos = {"MMM yyyy", "MMMM yyyy", "dd MMM yyyy", "yyyy-MM-dd", "MM/dd/yyyy"};
        for (String formato : formatos) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
                return java.time.LocalDate.parse(dataString, formatter).getYear();
            } catch (DateTimeParseException e) {
            }
        }
        try {
            return Integer.parseInt(dataString);
        } catch (NumberFormatException e) {
            LOGGER.warning("Não foi possível parsear a data: " + dataString + ".");
        }
        return null;
    }

    /**
     * Busca uma editora pelo nome ou cria uma nova se não existir.
     *
     * @param nomeEditora o nome da editora a ser buscada ou criada
     * @return a editora encontrada ou criada
     */
    private Editora buscarOuCriarEditora(String nomeEditora) {
        return editoraService.buscarPorNome(nomeEditora).orElseGet(() -> {
            EditoraDTO novaEditoraDTO = new EditoraDTO();
            novaEditoraDTO.setNome(nomeEditora);
            return editoraService.salvar(novaEditoraDTO);
        });
    }

    /**
     * Busca um autor pelo nome ou cria um novo se não existir.
     *
     * @param nomeAutor o nome do autor a ser buscado ou criado
     * @return o autor encontrado ou criado
     */
    private Autor buscarOuCriarAutor(String nomeAutor) {
        return autorService.buscarPorNome(nomeAutor).orElseGet(() -> {
            AutorDTO novoAutorDTO = new AutorDTO();
            novoAutorDTO.setNome(nomeAutor);
            return autorService.salvar(novoAutorDTO);
        });
    }
}
