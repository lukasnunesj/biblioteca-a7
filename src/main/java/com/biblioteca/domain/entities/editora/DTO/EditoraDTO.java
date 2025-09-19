package com.biblioteca.domain.entities.editora.DTO;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.common.DTO.BaseDTO;
import com.biblioteca.infrastructure.exceptions.ValidacaoException;

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
public class EditoraDTO extends BaseDTO {

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
     * Construtor padrão.
     */
    public EditoraDTO() {
        super();
    }
    
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
        super(id);
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
        editora.setId(this.getId());
        editora.setNome(this.nome);
        editora.setCnpj(this.cnpj);
        editora.setTelefone(this.telefone);
        editora.setEmail(this.email);
        return editora;
    }
    
    /**
     * Cria um DTO a partir de uma entidade Editora.
     *
     * @param editora a entidade Editora
     * @return um novo EditoraDTO com os dados da entidade
     */
    public static EditoraDTO fromEntity(Editora editora) {
        if (editora == null) {
            return null;
        }
        return new EditoraDTO(
            editora.getId(),
            editora.getNome(),
            editora.getCnpj(),
            editora.getTelefone(),
            editora.getEmail()
        );
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
        if (cnpj != null) {
            this.cnpj = cnpj.replaceAll("[^0-9]", "");
        } else {
            this.cnpj = null;
        }
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
     * Valida os dados do DTO.
     * 
     * @throws ValidacaoException se os dados forem inválidos
     */
    @Override
    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("O nome da editora é obrigatório");
        }
        
        
        if (email != null && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new ValidacaoException("O email da editora é inválido");
        }
        
        if (cnpj != null && !cnpj.matches("^\\d{14}$")) {
            throw new ValidacaoException("O CNPJ deve conter exatamente 14 dígitos numéricos");
        }
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
