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
                String isbn = csvRecord.get("isbn");
                String titulo = csvRecord.get("titulo");
                int dataPublicacao = Integer.parseInt(csvRecord.get("data_publicacao"));

                // Processar Editora
                String nomeEditora = csvRecord.get("editora");
                Editora editora = buscarOuCriarEditora(nomeEditora);

                // Processar Autores
                String[] nomesAutores = csvRecord.get("autores").split(",");

                List<Long> autoresIds = java.util.stream.Stream.of(nomesAutores)
                        .map(nome -> buscarOuCriarAutor(nome.trim()))
                        .map(Autor::getId)
                        .collect(Collectors.toList());

                // Verificar se o livro já existe pelo ISBN
                Optional<Livro> livroExistenteOpt = livroService.buscarPorIsbn(isbn);

                System.err.println("Livro existente: " + livroExistenteOpt);

                if (livroExistenteOpt.isPresent()) {
                    // Se o livro já existe, atualiza os dados
                    Livro livroExistente = livroExistenteOpt.get();

                    // Atualizar os dados do livro existente
                    livroExistente.setTitulo(titulo);
                    livroExistente.setDataPublicacao(dataPublicacao);
                    livroExistente.setEditora(editora);

                    // Atualizar autores
                    Set<Autor> autores = new HashSet<>();
                    for (Long autorId : autoresIds) {
                        Autor autor = autorService.buscarPorId(autorId)
                                .orElseThrow(
                                        () -> new RuntimeException("Autor com ID " + autorId + " não encontrado."));
                        autores.add(autor);
                    }
                    livroExistente.setAutores(autores);

                    // Salvar o livro atualizado diretamente
                    System.err.println("Livro atualizado: " + livroExistente);
                    livroService.salvar(LivroDTO.fromEntity(livroExistente));
                } else {
                    // Criar um novo livro
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
