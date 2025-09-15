package com.biblioteca.domain.entities.editora.DTO;

import com.biblioteca.domain.entities.editora.Editora;

/**
 * Classe de Transferência de Dados (DTO) para a entidade Editora.
 * <p>
 * Esta classe é utilizada para transferir dados de editoras entre camadas da aplicação,
 * sem expor detalhes da implementação da entidade. Contém apenas os atributos
 * necessários para operações de criação e atualização de editoras.
 * </p>
 * 
 * @author Biblioteca A7
 * @version 1.0
 */
public class EditoraDTO {
    /**
     * Identificador único da editora.
     */
    private Long id;
    
    /**
     * Nome da editora.
     */
    private String nome;
    
    /**
     * CNPJ da editora.
     */
    private String cnpj;
    
    /**
     * Telefone de contato da editora.
     */
    private String telefone;
    
    /**
     * Email de contato da editora.
     */
    private String email;

    /**
     * Construtor para criar um novo EditoraDTO com todos os atributos.
     *
     * @param id       o identificador da editora
     * @param nome     o nome da editora
     * @param cnpj     o CNPJ da editora
     * @param telefone o telefone de contato da editora
     * @param email    o email de contato da editora
     */
    public EditoraDTO(Long id, String nome, String cnpj, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
    }

    /**
     * Converte este DTO para uma entidade Editora.
     *
     * @return uma nova instância de Editora com os dados deste DTO
     */
    public Editora toEntity() {
        Editora editora = new Editora();
        editora.setId(this.id);
        editora.setNome(this.nome);
        editora.setCnpj(this.cnpj);
        editora.setTelefone(this.telefone);
        editora.setEmail(this.email);
        return editora;
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
     * Define o identificador da editora.
     *
     * @param id o novo ID da editora
     */
    public void setId(Long id) {
        this.id = id;
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
}
