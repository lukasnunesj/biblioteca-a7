package com.biblioteca.domain.entities.autor.DTO;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.common.DTO.BaseDTO;
import com.biblioteca.infrastructure.exceptions.ValidacaoException;

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
public class AutorDTO extends BaseDTO {
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
     * Construtor padrão.
     */
    public AutorDTO() {
        super();
    }
    
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
        super(id);
        this.nome = nome;
        setCpfcnpj(cpfcnpj);
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
        autor.setId(this.getId());
        autor.setNome(this.nome);
        autor.setCpfcnpj(this.cpfcnpj);
        autor.setTelefone(this.telefone);
        autor.setEmail(this.email);
        return autor;
    }
    
    /**
     * Cria um DTO a partir de uma entidade Autor.
     *
     * @param autor a entidade Autor
     * @return um novo AutorDTO com os dados da entidade
     */
    public static AutorDTO fromEntity(Autor autor) {
        if (autor == null) {
            return null;
        }
        return new AutorDTO(
            autor.getId(),
            autor.getNome(),
            autor.getCpfcnpj(),
            autor.getTelefone(),
            autor.getEmail()
        );
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
     * Define o CPF/CNPJ do autor.
     *
     * @param cpfcnpj o novo CPF/CNPJ do autor
     */
    public void setCpfcnpj(String cpfcnpj) {
        if (cpfcnpj != null) {
            this.cpfcnpj = cpfcnpj.replaceAll("[^0-9]", "");
        } else {
            this.cpfcnpj = null;
        }
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
     * Valida os dados do DTO.
     * 
     * @throws ValidacaoException se os dados forem inválidos
     */
    @Override
    public void validar() {
        if (nome == null || nome.isBlank()) {
            throw new ValidacaoException("O nome do autor é obrigatório");
        }
        
        
        if (email != null && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new ValidacaoException("O email do autor é inválido");
        }
    }
    
    @Override
    public String toString() {
        return this.nome;
    }
}
