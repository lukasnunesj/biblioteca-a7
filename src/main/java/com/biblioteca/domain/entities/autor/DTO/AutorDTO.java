package com.biblioteca.domain.entities.autor.DTO;

import com.biblioteca.domain.entities.autor.Autor;

/**
 * Classe de Transferência de Dados (DTO) para a entidade Autor.
 * <p>
 * Esta classe é utilizada para transferir dados de autores entre camadas da aplicação,
 * sem expor detalhes da implementação da entidade. Contém apenas os atributos
 * necessários para operações de criação e atualização de autores.
 * </p>
 * 
 * @author Biblioteca A7
 * @version 1.0
 */
public class AutorDTO {
    /**
     * Identificador único do autor.
     */
    private Long id;
    
    /**
     * Nome do autor.
     */
    private String nome;
    
    /**
     * CPF ou CNPJ do autor.
     */
    private String cpfcnpj;
    
    /**
     * Telefone de contato do autor.
     */
    private String telefone;
    
    /**
     * Email de contato do autor.
     */
    private String email;

    /**
     * Construtor para criar um novo AutorDTO com todos os atributos.
     *
     * @param id       o identificador do autor
     * @param nome     o nome do autor
     * @param cpfcnpj  o CPF ou CNPJ do autor
     * @param telefone o telefone de contato do autor
     * @param email    o email de contato do autor
     */
    public AutorDTO(Long id, String nome, String cpfcnpj, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.cpfcnpj = cpfcnpj;
        this.telefone = telefone;
        this.email = email;
    }

    /**
     * Converte este DTO para uma entidade Autor.
     *
     * @return uma nova instância de Autor com os dados deste DTO
     */
    public Autor toEntity() {
        Autor autor = new Autor();
        autor.setId(this.id);
        autor.setNome(this.nome);
        autor.setCpfcnpj(this.cpfcnpj);
        autor.setTelefone(this.telefone);
        autor.setEmail(this.email);
        return autor;
    }
}
