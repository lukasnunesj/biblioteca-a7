package com.biblioteca.domain.entities.autor.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;

/**
 * Interface para o serviço de autores.
 * Define operações de negócio relacionadas à entidade Autor.
 */
public interface IAutorService {

    /**
     * Salva um autor a partir dos dados do DTO.
     * 
     * @param autorDTO o DTO contendo os dados do autor a ser salvo
     * @return o autor salvo com possíveis atualizações (como ID gerado)
     */
    Autor salvar(AutorDTO autorDTO);

    /**
     * Busca um autor pelo seu identificador único.
     * 
     * @param id o ID do autor a ser buscado
     * @return um Optional contendo o autor, se encontrado, ou vazio caso contrário
     */
    Optional<Autor> buscarPorId(Long id);

    /**
     * Busca um autor pelo seu CPF/CNPJ.
     * 
     * @param cpfcnpj o CPF/CNPJ do autor a ser buscado
     * @return um Optional contendo o autor, se encontrado, ou vazio caso contrário
     */
    Optional<Autor> buscarPorCpfcnpj(String cpfcnpj);

    /**
     * Retorna todos os autores cadastrados no sistema.
     * 
     * @return uma lista contendo todos os autores
     */
    List<Autor> buscarTodos();

    /**
     * Remove um autor do sistema a partir dos dados do DTO.
     * 
     * @param autorDTO o DTO contendo os dados do autor a ser removido
     */
    void remover(AutorDTO autorDTO);
}
