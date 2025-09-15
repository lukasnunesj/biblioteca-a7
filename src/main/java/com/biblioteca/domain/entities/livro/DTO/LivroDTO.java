package com.biblioteca.domain.entities.livro.DTO;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.livro.Livro;

/**
 * Classe de Transferência de Dados (DTO) para a entidade Livro.
 * <p>
 * Esta classe é utilizada para transferir dados de livros entre camadas da
 * aplicação,
 * sem expor detalhes da implementação da entidade. Contém apenas os atributos
 * necessários para operações de criação e atualização de livros.
 * </p>
 */
public class LivroDTO {
    /**
     * Identificador único do livro.
     */
    private Long id;

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
    public LivroDTO(Long id, String titulo, String isbn, LocalDate dataPublicacao, Long editoraId,
            List<Long> autoresIds) {
        this.id = id;
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
    public Livro toEntity(Editora editora, HashSet<Autor> autores) {
        Livro livro = new Livro();
        livro.setId(this.id);
        livro.setTitulo(this.titulo);
        livro.setIsbn(this.isbn);
        livro.setDataPublicacao(this.dataPublicacao);
        livro.setEditora(editora);
        livro.setAutores(autores);
        return livro;
    }

    /**
     * Retorna o identificador do livro.
     *
     * @return o ID do livro
     */
    public Long getId() {
        return id;
    }

    /**
     * Define o identificador do livro.
     *
     * @param id o novo ID do livro
     */
    public void setId(Long id) {
        this.id = id;
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

}
