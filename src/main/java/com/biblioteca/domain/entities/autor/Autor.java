package com.biblioteca.domain.entities.autor;

import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.biblioteca.domain.entities.livro.Livro;

/**
 * Representa um autor no sistema de biblioteca.
 * <p>
 * Esta entidade armazena informações sobre autores, como nome, CPF/CNPJ,
 * telefone e email. É mapeada para a tabela 'autores' no banco de dados.
 * Cada autor pode estar associado a múltiplos livros através de um relacionamento
 * muitos-para-muitos.
 * </p>
 * 
 * @author Biblioteca A7
 * @version 1.0
 */
@Entity
@Table(name = "autores")
public class Autor {
    /**
     * Identificador único do autor.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do autor.
     * Campo obrigatório com tamanho máximo de 100 caracteres.
     */
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /**
     * CPF ou CNPJ do autor.
     * Campo obrigatório com tamanho máximo de 18 caracteres.
     */
    @Column(name = "cpfcnpj", nullable = false, length = 18)
    private String cpfcnpj;

    /**
     * Telefone de contato do autor.
     * Campo obrigatório com tamanho máximo de 15 caracteres.
     */
    @Column(name = "telefone", nullable = false, length = 15)
    private String telefone;

    /**
     * Email de contato do autor.
     * Campo obrigatório com tamanho máximo de 100 caracteres.
     */
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    /**
     * Conjunto de livros associados a este autor.
     * Relacionamento muitos-para-muitos mapeado pelo atributo 'autor' na entidade Livro.
     */
    @ManyToMany(mappedBy = "autores", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Livro> livros = new HashSet<>();

    /**
     * Construtor padrão necessário para JPA.
     */
    public Autor() {
    }

    /**
     * Construtor para criar um novo autor com todas as informações necessárias.
     * Realiza validações nos parâmetros antes de criar o objeto.
     *
     * @param nome     o nome do autor
     * @param cpfcnpj o CPF ou CNPJ do autor
     * @param telefone o telefone de contato do autor
     * @param email   o email de contato do autor
     * @throws IllegalArgumentException se algum dos parâmetros for nulo ou vazio
     */
    public Autor(String nome, String cpfcnpj, String telefone, String email) {
                if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        if (cpfcnpj == null || cpfcnpj.isBlank()) {
            throw new IllegalArgumentException("CPF/CNPJ inválido");
        }
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone inválido");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email inválido");
        }

        this.nome = nome;
        this.cpfcnpj = cpfcnpj;
        this.telefone = telefone;
        this.email = email;
    }

    /**
     * Define o identificador único do autor.
     *
     * @param id o novo ID do autor
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retorna o identificador único do autor.
     *
     * @return o ID do autor
     */
    public Long getId() {
        return id;
    }

    /**
     * Retorna o nome do autor.
     *
     * @return o nome do autor
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o nome do autor.
     *
     * @param nome o novo nome do autor
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o CPF ou CNPJ do autor.
     *
     * @return o CPF ou CNPJ do autor
     */
    public String getCpfcnpj() {
        return cpfcnpj;
    }

    /**
     * Define o CPF ou CNPJ do autor.
     *
     * @param cpfcnpj o novo CPF ou CNPJ do autor
     */
    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    /**
     * Retorna o telefone de contato do autor.
     *
     * @return o telefone do autor
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * Define o telefone de contato do autor.
     *
     * @param telefone o novo telefone do autor
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * Retorna o email de contato do autor.
     *
     * @return o email do autor
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o email de contato do autor.
     *
     * @param email o novo email do autor
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna o conjunto de livros associados a este autor.
     *
     * @return conjunto de livros do autor
     */
    public Set<Livro> getLivros() {
        return livros;
    }

    /**
     * Define o conjunto de livros associados a este autor.
     *
     * @param livros o novo conjunto de livros do autor
     */
    public void setLivros(Set<Livro> livros) {
        this.livros = livros;
    }

    /**
     * Adiciona um livro ao conjunto de livros do autor.
     *
     * @param livro o livro a ser adicionado
     */
    public void addLivro(Livro livro) {
        this.livros.add(livro);
    }

    /**
     * Remove um livro do conjunto de livros do autor.
     *
     * @param livro o livro a ser removido
     */
    public void removeLivro(Livro livro) {
        this.livros.remove(livro);
    }

    /**
     * Retorna uma representação em string do autor.
     * Útil para logging e depuração.
     *
     * @return uma string com os principais atributos do autor
     */
    @Override
    public String toString() {
        return "Autor [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + "]";
    }

    /**
     * Compara este autor com outro objeto para verificar igualdade.
     * Dois autores são considerados iguais se tiverem o mesmo ID e CPF/CNPJ.
     *
     * @param obj o objeto a ser comparado com este autor
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
        Autor other = (Autor) obj;
        return Objects.equals(id, other.id) && Objects.equals(cpfcnpj, other.cpfcnpj);
    }

}
