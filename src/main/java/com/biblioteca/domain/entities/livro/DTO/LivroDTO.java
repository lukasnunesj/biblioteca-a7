package com.biblioteca.domain.entities.livro.DTO;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.common.DTO.BaseDTO;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.infrastructure.exceptions.ValidacaoException;

/**
 * Data Transfer Object para a entidade Livro.
 * <p>
 * Esta classe é utilizada para transferir dados de livros entre as camadas da aplicação,
 * facilitando operações de criação, atualização e exibição de livros.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public class LivroDTO extends BaseDTO {
    private String titulo;
    private String isbn;
    private Integer dataPublicacao;
    private Long editoraId;
    private List<Long> autoresIds;
    private List<Long> livrosSemelhantesIds;

    /**
     * Construtor padrão.
     */
    public LivroDTO() {
        super();
    }

    /**
     * Construtor com todos os atributos.
     *
     * @param id                  o ID do livro
     * @param titulo              o título do livro
     * @param isbn                o ISBN do livro
     * @param dataPublicacao      o ano de publicação do livro
     * @param editoraId           o ID da editora do livro
     * @param autoresIds          a lista de IDs dos autores do livro
     * @param livrosSemelhantesIds a lista de IDs dos livros semelhantes
     */
    public LivroDTO(Long id, String titulo, String isbn, Integer dataPublicacao, Long editoraId,
            List<Long> autoresIds, List<Long> livrosSemelhantesIds) {
        super(id);
        this.titulo = titulo;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.editoraId = editoraId;
        this.autoresIds = autoresIds;
        this.livrosSemelhantesIds = livrosSemelhantesIds;
    }

    /**
     * Converte este DTO para uma entidade Livro.
     *
     * @param editora a editora do livro
     * @param autores o conjunto de autores do livro
     * @return a entidade Livro criada a partir deste DTO
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

    /**
     * Cria um DTO a partir de uma entidade Livro.
     *
     * @param livro a entidade Livro a ser convertida
     * @return um novo LivroDTO com os dados da entidade, ou null se a entidade for null
     */
    public static LivroDTO fromEntity(Livro livro) {
        if (livro == null) {
            return null;
        }

        List<Long> autoresIds = livro.getAutores() != null ? livro.getAutores().stream()
                .map(Autor::getId)
                .collect(Collectors.toList()) : Collections.emptyList();

        Long editoraId = (livro.getEditora() != null) ? livro.getEditora().getId() : null;

        List<Long> livrosSemelhantesIds = livro.getLivrosSemelhantes() != null ? livro.getLivrosSemelhantes().stream()
                .map(Livro::getId)
                .collect(Collectors.toList()) : Collections.emptyList();

        return new LivroDTO(
                livro.getId(),
                livro.getTitulo(),
                livro.getIsbn(),
                livro.getDataPublicacao(),
                editoraId,
                autoresIds,
                livrosSemelhantesIds);
    }

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
     * Retorna o ISBN do livro.
     *
     * @return o ISBN do livro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Define o ISBN do livro.
     *
     * @param isbn o novo ISBN do livro
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Retorna o ano de publicação do livro.
     *
     * @return o ano de publicação do livro
     */
    public Integer getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define o ano de publicação do livro.
     *
     * @param dataPublicacao o novo ano de publicação do livro
     */
    public void setDataPublicacao(Integer dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * Retorna o ID da editora do livro.
     *
     * @return o ID da editora
     */
    public Long getEditoraId() {
        return editoraId;
    }

    /**
     * Define o ID da editora do livro.
     *
     * @param editoraId o novo ID da editora
     */
    public void setEditoraId(Long editoraId) {
        this.editoraId = editoraId;
    }

    /**
     * Retorna a lista de IDs dos autores do livro.
     *
     * @return a lista de IDs dos autores
     */
    public List<Long> getAutoresIds() {
        return autoresIds;
    }

    /**
     * Define a lista de IDs dos autores do livro.
     *
     * @param autoresIds a nova lista de IDs dos autores
     */
    public void setAutoresIds(List<Long> autoresIds) {
        this.autoresIds = autoresIds;
    }

    /**
     * Retorna a lista de IDs dos livros semelhantes.
     *
     * @return a lista de IDs dos livros semelhantes
     */
    public List<Long> getLivrosSemelhantesIds() {
        return livrosSemelhantesIds;
    }

    /**
     * Define a lista de IDs dos livros semelhantes.
     *
     * @param livrosSemelhantesIds a nova lista de IDs dos livros semelhantes
     */
    public void setLivrosSemelhantesIds(List<Long> livrosSemelhantesIds) {
        this.livrosSemelhantesIds = livrosSemelhantesIds;
    }

    /**
     * Valida os dados do DTO.
     * Verifica se os campos obrigatórios foram preenchidos.
     *
     * @throws ValidacaoException se algum campo obrigatório não estiver preenchido
     */
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
