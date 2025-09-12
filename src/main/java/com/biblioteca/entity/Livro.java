package com.biblioteca.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Representa um livro no sistema de biblioteca.
 * <p>
 * Esta entidade armazena informações básicas sobre livros, como título,
 * ISBN e data de publicação. É mapeada para a tabela 'livros' no banco de
 * dados. Cada livro está associado a uma editora através de um relacionamento
 * muitos-para-um.
 * </p>
 * 
 * @author Biblioteca A7
 * @version 1.0
 */
@Entity
@Table(name = "livros")
public class Livro {
    /**
     * Identificador único do livro.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Título do livro.
     * Campo obrigatório com tamanho máximo de 500 caracteres.
     */
    @Column(name = "titulo", nullable = false, length = 500)
    private String titulo;

    /**
     * Código ISBN (International Standard Book Number) do livro.
     * Deve ser único no sistema e ter no máximo 20 caracteres.
     */
    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;

    /**
     * Data de publicação do livro.
     */
    @Column(name = "data_publicacao")
    private LocalDate dataPublicacao;

    /**
     * Editora responsável pelo livro.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editora_id")
    private Editora editora;

    @ManyToMany
    @JoinTable(name = "livro_autor", joinColumns = @JoinColumn(name = "livro_id"), inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private HashSet<Autor> autores = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "livros_semelhantes", joinColumns = @JoinColumn(name = "livro_id"), inverseJoinColumns = @JoinColumn(name = "semelhante_id"))
    private HashSet<Livro> livrosSemelhantes = new HashSet<>();

    /**
     * Construtor padrão necessário para JPA.
     */
    public Livro() {
    }

    /**
     * Construtor para criar um novo livro com todas as informações necessárias.
     * Realiza validações nos parâmetros antes de criar o objeto.
     *
     * @param titulo         o título do livro
     * @param isbn           o código ISBN do livro (deve ter 10 ou 13 dígitos)
     * @param dataPublicacao a data de publicação do livro (não pode ser futura)
     * @throws IllegalArgumentException se o título for vazio, o ISBN for inválido
     *                                  ou a data for futura
     */
    public Livro(String titulo, String isbn, LocalDate dataPublicacao) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título não pode ser vazio!");
        }

        if (!isISBNValid(isbn)) {
            throw new IllegalArgumentException("ISBN inválido!");
        }

        if (dataPublicacao.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de publicação não pode ser no futuro!");
        }

        this.titulo = titulo;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * Retorna o identificador único do livro.
     *
     * @return o ID do livro
     */
    public Long getId() {
        return id;
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
     * Valida o formato do ISBN antes de atribuí-lo.
     *
     * @param isbn o novo ISBN do livro
     * @throws IllegalArgumentException se o ISBN for inválido (não tiver 10 ou 13
     *                                  dígitos)
     */
    public void setIsbn(String isbn) {
        if (!isISBNValid(isbn)) {
            throw new IllegalArgumentException("ISBN inválido!");
        }
        this.isbn = isbn;
    }

    /**
     * Define a data de publicação do livro.
     *
     * @param dataPublicacao a nova data de publicação
     */
    public void setDataPublicacao(LocalDate dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    /**
     * Retorna a data de publicação do livro.
     *
     * @return a data de publicação
     */
    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Retorna a editora responsável pelo livro.
     * 
     * @return a editora do livro
     */
    public Editora getEditora() {
        return editora;
    }

    /**
     * Define a editora responsável pelo livro.
     *
     * @param editora a nova editora do livro
     */
    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public HashSet<Autor> getAutores() {
        return autores;
    }

    public void setAutores(HashSet<Autor> autores) {
        this.autores = autores;
    }

    public void addAutor(Autor autor) {
        this.autores.add(autor);
    }

    public void removeAutor(Autor autor) {
        this.autores.remove(autor);
    }

    public HashSet<Livro> getLivrosSemelhantes() {
        return livrosSemelhantes;
    }

    public void setLivrosSemelhantes(HashSet<Livro> livros) {
        this.livrosSemelhantes = livros;
    }

    public void addLivroSemelhante(Livro livro) {
        this.livrosSemelhantes.add(livro);
    }

    public void removeLivroSemelhante(Livro livro) {
        this.livrosSemelhantes.remove(livro);
    }

    /**
     * Verifica se o ISBN fornecido é válido.
     * Um ISBN válido deve ter 10 ou 13 dígitos numéricos.
     *
     * @param isbn o ISBN a ser validado
     * @return true se o ISBN for válido, false caso contrário
     */
    private boolean isISBNValid(String isbn) {
        return isbn != null && isbn.matches("\\d{10}|\\d{13}");
    }

    /**
     * Retorna uma representação em string do livro.
     * Útil para logging e depuração.
     *
     * @return uma string com os principais atributos do livro
     */
    @Override
    public String toString() {
        return "Livro [id=" + id + ", titulo=" + titulo + ", isbn=" + isbn + ", dataPublicacao=" + dataPublicacao + "]";
    }

    /**
     * Compara este livro com outro objeto para verificar igualdade.
     * Dois livros são considerados iguais se tiverem o mesmo ID e ISBN.
     *
     * @param obj o objeto a ser comparado com este livro
     * @return true se os objetos são iguais, false caso contrário
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Livro other = (Livro) obj;
        return Objects.equals(id, other.id) && Objects.equals(isbn, other.isbn);
    }
}
