package com.biblioteca.domain.entities.livro;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.editora.Editora;

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

    /**
     * Conjunto de autores associados ao livro.
     * Relacionamento muitos-para-muitos mapeado pela tabela 'livro_autor'.
     */
    @ManyToMany
    @JoinTable(name = "livro_autor", joinColumns = @JoinColumn(name = "livro_id"), inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private Set<Autor> autores = new HashSet<>();

    /**
     * Conjunto de livros considerados semelhantes a este livro.
     * Relacionamento muitos-para-muitos mapeado pela tabela 'livros_semelhantes'.
     */
    @ManyToMany
    @JoinTable(name = "livros_semelhantes", joinColumns = @JoinColumn(name = "livro_id"), inverseJoinColumns = @JoinColumn(name = "semelhante_id"))
    private Set<Livro> livrosSemelhantes = new HashSet<>();

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
     * Define o identificador único do livro.
     *
     * @param id o novo ID do livro
     */
    public void setId(Long id) {
        this.id = id;
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

    /**
     * Retorna o conjunto de autores associados ao livro.
     *
     * @return conjunto de autores do livro
     */
    public Set<Autor> getAutores() {
        return autores;
    }

    /**
     * Define o conjunto de autores associados ao livro.
     *
     * @param autores o novo conjunto de autores do livro
     */
    public void setAutores(Set<Autor> autores) {
        this.autores = autores;
    }

    /**
     * Adiciona um autor ao conjunto de autores do livro.
     *
     * @param autor o autor a ser adicionado
     */
    public void addAutor(Autor autor) {
        this.autores.add(autor);
    }

    /**
     * Remove um autor do conjunto de autores do livro.
     *
     * @param autor o autor a ser removido
     */
    public void removeAutor(Autor autor) {
        this.autores.remove(autor);
    }

    /**
     * Retorna o conjunto de livros semelhantes a este livro.
     *
     * @return conjunto de livros semelhantes
     */
    public Set<Livro> getLivrosSemelhantes() {
        return livrosSemelhantes;
    }

    /**
     * Define o conjunto de livros semelhantes a este livro.
     *
     * @param livros o novo conjunto de livros semelhantes
     */
    public void setLivrosSemelhantes(Set<Livro> livros) {
        this.livrosSemelhantes = livros;
    }

    /**
     * Adiciona um livro ao conjunto de livros semelhantes.
     *
     * @param livro o livro a ser adicionado como semelhante
     */
    public void addLivroSemelhante(Livro livro) {
        this.livrosSemelhantes.add(livro);
    }

    /**
     * Remove um livro do conjunto de livros semelhantes.
     *
     * @param livro o livro a ser removido dos semelhantes
     */
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
