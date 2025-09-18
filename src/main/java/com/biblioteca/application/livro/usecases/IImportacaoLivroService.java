package com.biblioteca.application.livro.usecases;

import java.io.InputStream;

/**
 * Caso de uso para a importação de livros a partir de um arquivo.
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
