package com.biblioteca.application.livro.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.biblioteca.application.livro.usecases.IImportacaoLivroService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;

public class ImportacaoLivroService implements IImportacaoLivroService {

    private final ILivroRepository livroRepository;

    public ImportacaoLivroService(ILivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    @Override
    public void importar(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.Builder.create().setHeader().setIgnoreHeaderCase(true).setTrim(true).build())) {

            for (CSVRecord csvRecord : csvParser) {
                String isbn = csvRecord.get("isbn");
                String titulo = csvRecord.get("titulo");
                LocalDate dataPublicacao = LocalDate.parse(csvRecord.get("dataPublicacao"));

                Optional<Livro> livroExistente = livroRepository.buscarPorIsbn(isbn);

                Livro livro;
                if (livroExistente.isPresent()) {
                    livro = livroExistente.get();
                    livro.setTitulo(titulo);
                    livro.setDataPublicacao(dataPublicacao);
                } else {
                    livro = new Livro(titulo, isbn, dataPublicacao);
                }

                livroRepository.save(livro);
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar o arquivo CSV: " + e.getMessage());
        }
    }
}
