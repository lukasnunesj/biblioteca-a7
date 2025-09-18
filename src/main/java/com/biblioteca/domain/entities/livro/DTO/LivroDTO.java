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

public class LivroDTO extends BaseDTO {
    private String titulo;
    private String isbn;
    private Integer dataPublicacao;
    private Long editoraId;
    private List<Long> autoresIds;
    private List<Long> livrosSemelhantesIds;

    public LivroDTO() {
        super();
    }

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

    // Getters and Setters

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(Integer dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Long getEditoraId() {
        return editoraId;
    }

    public void setEditoraId(Long editoraId) {
        this.editoraId = editoraId;
    }

    public List<Long> getAutoresIds() {
        return autoresIds;
    }

    public void setAutoresIds(List<Long> autoresIds) {
        this.autoresIds = autoresIds;
    }

    public List<Long> getLivrosSemelhantesIds() {
        return livrosSemelhantesIds;
    }

    public void setLivrosSemelhantesIds(List<Long> livrosSemelhantesIds) {
        this.livrosSemelhantesIds = livrosSemelhantesIds;
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
