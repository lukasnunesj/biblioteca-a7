package com.biblioteca.application.livro.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.biblioteca.application.livro.usecases.IImportacaoLivroService;
import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela importação de livros a partir de arquivos CSV.
 * <p>
 * Esta classe implementa a interface IImportacaoLivroService e fornece
 * funcionalidades para importar livros, autores e editoras a partir de um arquivo CSV.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
@Stateless
public class ImportacaoLivroService implements IImportacaoLivroService {

    @Inject
    private ILivroService livroService;

    @Inject
    private IEditoraService editoraService;

    @Inject
    private IAutorService autorService;

    /**
     * Importa livros a partir de um arquivo CSV.
     * <p>
     * O arquivo CSV deve conter as colunas: titulo, isbn, data_publicacao, editora, autores.
     * Os autores devem ser separados por vírgula.
     * </p>
     *
     * @param inputStream o stream de entrada contendo o arquivo CSV
     * @throws RuntimeException se ocorrer um erro durante a importação
     */
    @Override
    public void importar(InputStream inputStream) {
        CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader("titulo", "isbn", "data_publicacao", "editora", "autores")
                .setSkipHeaderRecord(true)
                .build();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {

            for (CSVRecord csvRecord : csvParser) {
                String isbn = csvRecord.get("isbn");
                String titulo = csvRecord.get("titulo");
                int dataPublicacao = Integer.parseInt(csvRecord.get("data_publicacao"));

                String nomeEditora = csvRecord.get("editora");
                Editora editora = buscarOuCriarEditora(nomeEditora);

                String[] nomesAutores = csvRecord.get("autores").split(",");

                List<Long> autoresIds = java.util.stream.Stream.of(nomesAutores)
                        .map(nome -> buscarOuCriarAutor(nome.trim()))
                        .map(Autor::getId)
                        .collect(Collectors.toList());

                Optional<Livro> livroExistenteOpt = livroService.buscarPorIsbn(isbn);

                if (livroExistenteOpt.isPresent()) {
                    Livro livroExistente = livroExistenteOpt.get();

                    livroExistente.setTitulo(titulo);
                    livroExistente.setDataPublicacao(dataPublicacao);
                    livroExistente.setEditora(editora);
                    Set<Autor> autores = new HashSet<>();
                    for (Long autorId : autoresIds) {
                        Autor autor = autorService.buscarPorId(autorId)
                                .orElseThrow(
                                        () -> new RuntimeException("Autor com ID " + autorId + " não encontrado."));
                        autores.add(autor);
                    }
                    livroExistente.setAutores(autores);

                    livroService.salvar(LivroDTO.fromEntity(livroExistente));
                } else {
                    LivroDTO livroDTO = new LivroDTO();
                    livroDTO.setTitulo(titulo);
                    livroDTO.setIsbn(isbn);
                    livroDTO.setDataPublicacao(dataPublicacao);
                    livroDTO.setEditoraId(editora.getId());
                    livroDTO.setAutoresIds(autoresIds);

                    livroService.salvar(livroDTO);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar o arquivo CSV: " + e.getMessage());
        }
    }

    /**
     * Busca uma editora pelo nome ou cria uma nova se não existir.
     *
     * @param nomeEditora o nome da editora a ser buscada ou criada
     * @return a editora encontrada ou criada
     */
    private Editora buscarOuCriarEditora(String nomeEditora) {
        return editoraService.buscarPorNome(nomeEditora).orElseGet(() -> {
            EditoraDTO novaEditoraDTO = new EditoraDTO();
            novaEditoraDTO.setNome(nomeEditora);
            return editoraService.salvar(novaEditoraDTO);
        });
    }

    /**
     * Busca um autor pelo nome ou cria um novo se não existir.
     *
     * @param nomeAutor o nome do autor a ser buscado ou criado
     * @return o autor encontrado ou criado
     */
    private Autor buscarOuCriarAutor(String nomeAutor) {
        return autorService.buscarPorNome(nomeAutor).orElseGet(() -> {
            AutorDTO novoAutorDTO = new AutorDTO();
            novoAutorDTO.setNome(nomeAutor);
            return autorService.salvar(novoAutorDTO);
        });
    }
}
