package com.biblioteca.domain.entities.autor.interfaces;

import java.util.List;
import java.util.Optional;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.common.interfaces.IBaseRepository;

/**
 * Interface para o repositório de autores.
 * Define operações básicas de persistência para a entidade Autor.
 */
public interface IAutorRepository extends IBaseRepository<Autor, Long> {


    /**
     * Busca um autor pelo seu nome.
     * 
     * @param nome o nome do autor a ser buscado
     * @return um Optional contendo o autor, se encontrado, ou vazio caso contrário
     */
    Optional<Autor> buscarPorNome(String nome);

    /**
     * Método de compatibilidade: Salva um autor no repositório.
     * 
     * @param autor o autor a ser salvo
     * @return o autor salvo com possíveis atualizações (como ID gerado)
     */
    default Autor salvar(Autor autor) {
        return save(autor);
    }

    /**
     * Método de compatibilidade: Busca um autor pelo seu identificador único.
     * 
     * @param id o ID do autor a ser buscado
     * @return um Optional contendo o autor, se encontrado, ou vazio caso contrário
     */
    default Optional<Autor> buscarPorId(Long id) {
        return findById(id);
    }

    /**
     * Método de compatibilidade: Retorna todos os autores cadastrados no
     * repositório.
     * 
     * @return uma lista contendo todos os autores
     */
    default List<Autor> buscarTodos() {
        return findAll();
    }

    /**
     * Método de compatibilidade: Remove um autor do repositório.
     * 
     * @param autor o autor a ser removido
     */
    default void remover(Autor autor) {
        delete(autor);
    }
}
