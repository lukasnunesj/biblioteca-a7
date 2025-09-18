package com.biblioteca.application;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.biblioteca.application.livro.service.OpenLibraryService;
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
import com.biblioteca.infrastructure.exceptions.RecursoNaoEncontradoException;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

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

    @Inject
    private OpenLibraryService openLibraryService;

    /**
     * Construtor padrão para CDI
     */
    public LivroService() {
        // Construtor vazio para CDI
    }

    /**
     * Construtor para testes com injeção manual
     */
    public LivroService(ILivroRepository livroRepository, IEditoraService editoraService, IAutorService autorService, OpenLibraryService openLibraryService) {
        this.livroRepository = livroRepository;
        this.editoraService = editoraService;
        this.autorService = autorService;
        this.openLibraryService = openLibraryService;
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
        System.out.println("Buscando todos os livros.");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Livro> cadastrarPorIsbn(String isbn) {
        LOGGER.info("Cadastrando livro por ISBN: " + isbn);
        
        // Verifica se o livro já existe
        Optional<Livro> livroExistente = livroRepository.buscarPorIsbn(isbn);
        if (livroExistente.isPresent()) {
            LOGGER.info("Livro já existe no sistema com ISBN: " + isbn);
            return livroExistente;
        }

        // Busca informações na OpenLibrary
        Optional<OpenLibraryResponseDTO> dadosOpenLibrary = openLibraryService.buscarLivroPorIsbn(isbn);
        
        if (dadosOpenLibrary.isEmpty()) {
            LOGGER.warning("Livro não encontrado na OpenLibrary para ISBN: " + isbn);
            return Optional.empty();
        }

        OpenLibraryResponseDTO dadosLivro = dadosOpenLibrary.get();
        
        try {
            // Cria o livro com os dados básicos
            Livro livro = new Livro();
            livro.setTitulo(dadosLivro.getTitle() != null ? dadosLivro.getTitle() : "Título não informado");
            livro.setIsbn(isbn);
            
            // Tenta converter a data de publicação
            if (dadosLivro.getPublishDate() != null) {
                LocalDate dataPublicacao = parseDataPublicacao(dadosLivro.getPublishDate());
                livro.setDataPublicacao(dataPublicacao);
            }

            // Busca ou cria editora
            if (dadosLivro.getPublishers() != null && !dadosLivro.getPublishers().isEmpty()) {
                String nomeEditora = dadosLivro.getPublishers().get(0).getName();
                Editora editora = buscarOuCriarEditora(nomeEditora);
                livro.setEditora(editora);
            }

            // Busca ou cria autores
            Set<Autor> autores = buscarOuCriarAutores(dadosLivro);
            livro.setAutores(autores);

            // Salva o livro
            Livro livroSalvo = livroRepository.save(livro);
            LOGGER.info("Livro cadastrado com sucesso: " + livroSalvo.getTitulo());
            
            return Optional.of(livroSalvo);
            
        } catch (Exception e) {
            LOGGER.severe("Erro ao cadastrar livro por ISBN: " + e.getMessage());
            return Optional.empty();
        }
    }

    private LocalDate parseDataPublicacao(String dataString) {
        // Tenta diferentes formatos de data
        String[] formatos = {"yyyy", "MMM yyyy", "MMMM yyyy", "dd MMM yyyy", "yyyy-MM-dd", "MM/dd/yyyy"};
        
        for (String formato : formatos) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
                return LocalDate.parse(dataString, formatter);
            } catch (DateTimeParseException e) {
                // Continua tentando outros formatos
            }
        }
        
        // Se não conseguir parsear, retorna data atual
        LOGGER.warning("Não foi possível parsear a data: " + dataString + ". Usando data atual.");
        return LocalDate.now();
    }

    private Editora buscarOuCriarEditora(String nomeEditora) {
        // Busca editora existente pelo nome
        List<Editora> editoras = editoraService.buscarTodos();
        for (Editora editora : editoras) {
            if (editora.getNome().equalsIgnoreCase(nomeEditora)) {
                return editora;
            }
        }
        
        // Se não encontrar, cria uma nova editora com dados básicos
        EditoraDTO novaEditoraDTO = new EditoraDTO();
        novaEditoraDTO.setNome(nomeEditora);
        novaEditoraDTO.setCnpj("00000000000000"); // CNPJ padrão
        novaEditoraDTO.setTelefone("(00) 00000-0000"); // Telefone padrão
        novaEditoraDTO.setEmail("contato@" + nomeEditora.toLowerCase().replaceAll("[^a-z0-9]", "") + ".com");
        
        return editoraService.salvar(novaEditoraDTO);
    }

    private Set<Autor> buscarOuCriarAutores(OpenLibraryResponseDTO dadosLivro) {
        Set<Autor> autores = new HashSet<>();
        
        if (dadosLivro.getAuthors() != null) {
            for (OpenLibraryResponseDTO.Author author : dadosLivro.getAuthors()) {
                String nomeAutor = author.getName();
                if (nomeAutor != null && !nomeAutor.trim().isEmpty()) {
                    Autor autor = buscarOuCriarAutor(nomeAutor);
                    autores.add(autor);
                }
            }
        }
        
        return autores;
    }

    private Autor buscarOuCriarAutor(String nomeAutor) {
        // Busca autor existente pelo nome
        List<Autor> autoresExistentes = autorService.buscarTodos();
        for (Autor autor : autoresExistentes) {
            if (autor.getNome().equalsIgnoreCase(nomeAutor)) {
                return autor;
            }
        }
        
        // Se não encontrar, cria um novo autor com dados básicos
        AutorDTO novoAutorDTO = new AutorDTO();
        novoAutorDTO.setNome(nomeAutor);
        novoAutorDTO.setCpfcnpj("00000000000"); // CPF padrão
        novoAutorDTO.setTelefone("(00) 00000-0000"); // Telefone padrão
        novoAutorDTO.setEmail("contato@" + nomeAutor.toLowerCase().replaceAll("[^a-z0-9]", "") + ".com");
        
        return autorService.salvar(novoAutorDTO);
    }

}
