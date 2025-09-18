package com.biblioteca.domain.entities.editora.interfaces;

import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;

import java.util.List;
import java.util.Optional;

/**
 * Interface para o serviço de editoras.
 * Define operações de negócio relacionadas à entidade Editora.
 */
public interface IEditoraService {

    /**
     * Salva uma editora a partir dos dados do DTO.
     * 
     * @param editoraDTO o DTO contendo os dados da editora a ser salva
     * @return a editora salva com possíveis atualizações (como ID gerado)
     */
    Editora salvar(EditoraDTO editoraDTO);

    /**
     * Busca uma editora pelo seu identificador único.
     * 
     * @param id o ID da editora a ser buscada
     * @return um Optional contendo a editora, se encontrada, ou vazio caso
     *         contrário
     */
    Optional<Editora> buscarPorId(Long id);

    /**
     * Busca uma editora pelo seu nome.
     * 
     * @param nome o nome da editora a ser buscada
     * @return um Optional contendo a editora, se encontrada, ou vazio caso
     *         contrário
     */
    Optional<Editora> buscarPorNome(String nome);

    /**
     * Retorna todas as editoras cadastradas no sistema.
     * 
     * @return uma lista contendo todas as editoras
     */
    List<Editora> buscarTodos();

    /**
     * Remove uma editora do sistema a partir dos dados do DTO.
     * 
     * @param editoraDTO o DTO contendo os dados da editora a ser removida
     */
    void remover(EditoraDTO editoraDTO);

    /**
     * Busca editoras por um termo de pesquisa em vários campos.
     * 
     * @param termo o termo a ser pesquisado
     * @return uma lista de editoras que correspondem ao termo de pesquisa
     */
    List<Editora> findByTermo(String termo);

}
