package com.biblioteca.application.livro.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

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
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.stream.Collectors;

@Stateless
public class ImportacaoLivroService implements IImportacaoLivroService {

    @Inject
    private ILivroService livroService;

    @Inject
    private IEditoraService editoraService;

    @Inject
    private IAutorService autorService;

    @Override
    public void importar(InputStream inputStream) {
        CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                .setHeader("titulo", "isbn", "data_publicacao", "editora", "autores")
                .setSkipHeaderRecord(true)
                .build();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {

            for (CSVRecord csvRecord : csvParser) {
                // Processar Editora
                String nomeEditora = csvRecord.get("editora");
                Editora editora = buscarOuCriarEditora(nomeEditora);

                // Processar Autores
                String[] nomesAutores = csvRecord.get("autores").split(",");

                List<Long> autoresIds = java.util.stream.Stream.of(nomesAutores)
                        .map(nome -> buscarOuCriarAutor(nome.trim()))
                        .map(Autor::getId)
                        .collect(Collectors.toList());

                // Criar LivroDTO
                LivroDTO livroDTO = new LivroDTO();
                livroDTO.setTitulo(csvRecord.get("titulo"));
                livroDTO.setIsbn(csvRecord.get("isbn"));
                livroDTO.setDataPublicacao(Integer.parseInt(csvRecord.get("data_publicacao")));
                livroDTO.setEditoraId(editora.getId());
                livroDTO.setAutoresIds(autoresIds);

                livroService.salvar(livroDTO);
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar o arquivo CSV: " + e.getMessage());
        }
    }

    private Editora buscarOuCriarEditora(String nomeEditora) {
        return editoraService.buscarPorNome(nomeEditora).orElseGet(() -> {
            EditoraDTO novaEditoraDTO = new EditoraDTO();
            novaEditoraDTO.setNome(nomeEditora);
            return editoraService.salvar(novaEditoraDTO);
        });
    }

    private Autor buscarOuCriarAutor(String nomeAutor) {
        return autorService.buscarPorNome(nomeAutor).orElseGet(() -> {
            AutorDTO novoAutorDTO = new AutorDTO();
            novoAutorDTO.setNome(nomeAutor);
            return autorService.salvar(novoAutorDTO);
        });
    }
}
