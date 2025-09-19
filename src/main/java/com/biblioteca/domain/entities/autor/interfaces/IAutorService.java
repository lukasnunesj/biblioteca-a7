package com.biblioteca.domain.entities.autor.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;

/**
 * Interface para o serviço de autores.
 * <p>
 * Define operações de negócio relacionadas à entidade Autor,
 * incluindo criação, busca, atualização e remoção de autores.
 * Serve como contrato para implementações concretas do serviço.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
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
     * Busca um autor pelo seu nome.
     * 
     * @param nome o nome do autor a ser buscado
     * @return um Optional contendo o autor, se encontrado, ou vazio caso contrário
     */
    Optional<Autor> buscarPorNome(String nome);

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

    /**
     * Busca autores por um termo de pesquisa em vários campos.
     * 
     * @param termo o termo a ser pesquisado
     * @return uma lista de autores que correspondem ao termo de pesquisa
     */
    List<Autor> findByTermo(String termo);
}
