package com.biblioteca.domain.entities.autor.interfaces;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.common.interfaces.IBaseRepository;

import java.util.List;
import java.util.Optional;

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
     * Busca um autor pelo seu CPF/CNPJ.
     *
     * @param cpfcnpj o CPF/CNPJ do autor a ser buscado
     * @return um Optional contendo o autor, se encontrado, ou vazio caso contrário
     */
    Optional<Autor> buscarPorCpfcnpj(String cpfcnpj);

    /**
     * Busca autores por um termo de pesquisa em vários campos.
     *
     * @param termo o termo a ser pesquisado
     * @return uma lista de autores que correspondem ao termo de pesquisa
     */
    List<Autor> findByTermo(String termo);
}
