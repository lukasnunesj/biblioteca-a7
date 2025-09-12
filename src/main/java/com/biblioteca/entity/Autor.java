package com.biblioteca.entity;

import java.util.HashSet;
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

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "cpfcnpj", nullable = false, length = 18)
    private String cpfcnpj;

    @Column(name = "telefone", nullable = false, length = 15)
    private String telefone;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @ManyToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private HashSet<Livro> livros = new HashSet<Livro>();

    public Autor() {
    }

    public Autor(String nome, String cpfcnpj, String telefone, String email) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome inválido");
        }
        if (cpfcnpj == null || cpfcnpj.isEmpty()) {
            throw new IllegalArgumentException("CPF/CNPJ inválido");
        }
        if (telefone == null || telefone.isEmpty()) {
            throw new IllegalArgumentException("Telefone inválido");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email inválido");
        }

        this.nome = nome;
        this.cpfcnpj = cpfcnpj;
        this.telefone = telefone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpfcnpj() {
        return cpfcnpj;
    }

    public void setCpfcnpj(String cpfcnpj) {
        this.cpfcnpj = cpfcnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashSet<Livro> getLivros() {
        return livros;
    }

    public void setLivros(HashSet<Livro> livros) {
        this.livros = livros;
    }

    public void addLivro(Livro livro) {
        this.livros.add(livro);
    }

    public void removeLivro(Livro livro) {
        this.livros.remove(livro);
    }

    @Override
    public String toString() {
        return "Autor [id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + "]";
    }

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
