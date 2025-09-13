package com.biblioteca.domain.entities.editora;

import java.util.HashSet;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biblioteca.domain.entities.livro.Livro;

/**
 * Representa uma editora no sistema de biblioteca.
 * <p>
 * Esta entidade armazena informações sobre editoras, como nome, CNPJ,
 * telefone e email. É mapeada para a tabela 'editoras' no banco de dados.
 * Cada editora pode ter múltiplos livros associados através de um relacionamento
 * um-para-muitos.
 * </p>
 * 
 * @author Biblioteca A7
 * @version 1.0
 */
@Entity
@Table(name = "editoras")
public class Editora {
    /**
     * Identificador único da editora.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da editora.
     * Campo obrigatório com tamanho máximo de 100 caracteres.
     */
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /**
     * CNPJ da editora.
     * Campo obrigatório com tamanho máximo de 14 caracteres.
     */
    @Column(name = "cnpj", nullable = false, length = 14)
    private String cnpj;

    /**
     * Telefone de contato da editora.
     * Campo obrigatório com tamanho máximo de 15 caracteres.
     */
    @Column(name = "telefone", nullable = false, length = 15)
    private String telefone;

    /**
     * Email de contato da editora.
     * Campo obrigatório com tamanho máximo de 100 caracteres.
     */
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    /**
     * Conjunto de livros publicados por esta editora.
     * Relacionamento um-para-muitos mapeado pelo atributo 'editora' na entidade Livro.
     */
    @OneToMany(mappedBy = "editora", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private HashSet<Livro> livros = new HashSet<Livro>();

    /**
     * Construtor padrão necessário para JPA.
     */
    public Editora() {

    }

    /**
     * Construtor para criar uma nova editora com todas as informações necessárias.
     * Realiza validações nos parâmetros antes de criar o objeto.
     *
     * @param nome     o nome da editora
     * @param cnpj     o CNPJ da editora
     * @param telefone o telefone de contato da editora
     * @param email    o email de contato da editora
     * @throws IllegalArgumentException se algum dos parâmetros for nulo ou vazio
     */
    public Editora(String nome, String cnpj, String telefone, String email) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ não pode ser vazio!");
        }
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone não pode ser vazio!");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser vazio!");
        }

        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
    }

    /**
     * Define o identificador único da editora.
     *
     * @param id o novo ID da editora
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o identificador único da editora.
     *
     * @return o ID da editora
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna o nome da editora.
     *
     * @return o nome da editora
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome da editora.
     *
     * @param nome o novo nome da editora
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o CNPJ da editora.
     *
     * @return o CNPJ da editora
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * Define o CNPJ da editora.
     *
     * @param cnpj o novo CNPJ da editora
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * Retorna o telefone de contato da editora.
     *
     * @return o telefone da editora
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o telefone de contato da editora.
     *
     * @param telefone o novo telefone da editora
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Retorna o email de contato da editora.
     *
     * @return o email da editora
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email de contato da editora.
     *
     * @param email o novo email da editora
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna o conjunto de livros publicados por esta editora.
     *
     * @return conjunto de livros da editora
     */
    public HashSet<Livro> getLivros() {
        return livros;
    }

    /**
     * Define o conjunto de livros publicados por esta editora.
     *
     * @param livros o novo conjunto de livros da editora
     */
    public void setLivros(HashSet<Livro> livros) {
        this.livros = livros;
    }

    /**
     * Adiciona um livro ao conjunto de livros da editora.
     *
     * @param livro o livro a ser adicionado
     */
    public void addLivro(Livro livro) {
        livros.add(livro);
    }

    /**
     * Remove um livro do conjunto de livros da editora.
     *
     * @param livro o livro a ser removido
     */
    public void removeLivro(Livro livro) {
        livros.remove(livro);
    }

    /**
     * Retorna uma representação em string da editora.
     * Útil para logging e depuração.
     *
     * @return uma string com os principais atributos da editora
     */
    @Override
    public String toString() {
        return "Editora [id=" + id + ", nome=" + nome + ", cnpj=" + cnpj + ", telefone=" + telefone + ", email=" + email
                + "]";
    }

    /**
     * Compara esta editora com outro objeto para verificar igualdade.
     * Duas editoras são consideradas iguais se tiverem o mesmo ID e CNPJ.
     *
     * @param obj o objeto a ser comparado com esta editora
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
        Editora other = (Editora) obj;
        return Objects.equals(id, other.id) && Objects.equals(cnpj, other.cnpj);
    }
}
