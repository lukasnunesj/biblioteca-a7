package com.biblioteca.application.livro.usecases;

import java.io.InputStream;

/**
 * Interface que define o caso de uso para a importação de livros a partir de um arquivo.
 * <p>
 * Esta interface define o contrato para serviços que implementam a funcionalidade
 * de importação de livros a partir de arquivos externos, como CSV.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
public interface IImportacaoLivroService {

    /**
     * Importa livros de um arquivo, criando novos registros ou atualizando os
     * existentes.
     *
     * @param inputStream O stream de dados do arquivo a ser importado.
     */
    void importar(InputStream inputStream);
}
