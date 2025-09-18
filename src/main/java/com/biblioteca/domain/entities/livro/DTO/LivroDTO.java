package com.biblioteca.domain.entities.livro.DTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.common.DTO.BaseDTO;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.infrastructure.exceptions.ValidacaoException;

/**
 * Classe de Transferência de Dados (DTO) para a entidade Livro.
 * <p>
 * Esta classe é utilizada para transferir dados de livros entre camadas da
 * aplicação,
 * sem expor detalhes da implementação da entidade. Contém apenas os atributos
 * necessários para operações de criação e atualização de livros.
 * </p>
 */
public class LivroDTO extends BaseDTO {
    /**
     * Identificador único do livro.
     */

    /**
     * Título do livro.
     */
    private String titulo;

    /**
     * Código ISBN do livro.
     */
    private String isbn;

    /**
     * Data de publicação do livro.
     */
    private LocalDate dataPublicacao;

    /**
     * Identificador da editora do livro.
     */
    private Long editoraId;

    /**
     * Lista de identificadores dos autores do livro.
     */
    private List<Long> autoresIds;

    /**
     * Construtor para criar um novo LivroDTO com todos os atributos.
     *
     * @param id             o identificador do livro
     * @param titulo         o título do livro
     * @param isbn           o código ISBN do livro
     * @param dataPublicacao a data de publicação do livro
     * @param editoraId      o identificador da editora
     * @param autoresIds     a lista de identificadores dos autores
     */
    public LivroDTO() {
        super();
    }

    public LivroDTO(Long id, String titulo, String isbn, LocalDate dataPublicacao, Long editoraId,
            List<Long> autoresIds) {
        super(id);
        this.titulo = titulo;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.editoraId = editoraId;
        this.autoresIds = autoresIds;
    }

    /**
     * Converte este DTO para uma entidade Livro.
     *
     * @param editora a entidade Editora associada ao livro
     * @param autores o conjunto de entidades Autor associadas ao livro
     * @return uma nova instância de Livro com os dados deste DTO
     */
    public Livro toEntity(Editora editora, Set<Autor> autores) {
        Livro livro = new Livro();
        livro.setId(this.getId());
        livro.setTitulo(this.titulo);
        livro.setIsbn(this.isbn);
        livro.setDataPublicacao(this.dataPublicacao);
        livro.setEditora(editora);
        livro.setAutores(autores);
        return livro;
    }

    public static LivroDTO fromEntity(Livro livro) {
        System.out.println("Convertendo entidade Livro para DTO");
        if (livro == null) {
            return null;
        }
        System.out.println("Livro: " + livro);
        List<Long> autoresIds = livro.getAutores().stream()
                .map(Autor::getId)
                .collect(Collectors.toList());
        System.out.println("Autores IDs: " + autoresIds);
        // Safely get editora ID, handling null case
        Long editoraId = null;
        if (livro.getEditora() != null) {
            editoraId = livro.getEditora().getId();
        }
        System.out.println("Editora ID: " + editoraId);

        return new LivroDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getDataPublicacao(),
                editoraId,
                autoresIds);
    }

    /**
     * Retorna o identificador do livro.
     *
     * @return o ID do livro
     */

    /**
     * Retorna o título do livro.
     *
     * @return o título do livro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título do livro.
     *
     * @param titulo o novo título do livro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Retorna o código ISBN do livro.
     *
     * @return o ISBN do livro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Define o código ISBN do livro.
     *
     * @param isbn o novo ISBN do livro
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Retorna a data de publicação do livro.
     *
     * @return a data de publicação do livro
     */
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define a data de publicação do livro.
     *
     * @param dataPublicacao a nova data de publicação do livro
     */
    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * Retorna o identificador da editora do livro.
     *
     * @return o ID da editora
     */
    public Long getEditoraId() {
        return editoraId;
    }

    /**
     * Define o identificador da editora do livro.
     *
     * @param editoraId o novo ID da editora
     */
    public void setEditoraId(Long editoraId) {
        this.editoraId = editoraId;
    }

    /**
     * Retorna a lista de identificadores dos autores do livro.
     *
     * @return a lista de IDs dos autores
     */
    public List<Long> getAutoresIds() {
        return autoresIds;
    }

    /**
     * Define a lista de identificadores dos autores do livro.
     *
     * @param autoresIds a nova lista de IDs dos autores
     */
    public void setAutoresIds(List<Long> autoresIds) {
        this.autoresIds = autoresIds;
    }

    @Override
    public void validar() {
        if (titulo == null || titulo.isBlank()) {
            throw new ValidacaoException("O título do livro é obrigatório.");
        }
        if (isbn == null || isbn.isBlank()) {
            throw new ValidacaoException("O ISBN do livro é obrigatório.");
        }
        if (dataPublicacao == null) {
            throw new ValidacaoException("A data de publicação do livro é obrigatória.");
        }
        if (editoraId == null) {
            throw new ValidacaoException("A editora do livro é obrigatória.");
        }
        if (autoresIds == null || autoresIds.isEmpty()) {
            throw new ValidacaoException("O livro deve ter pelo menos um autor.");
        }
    }

}
