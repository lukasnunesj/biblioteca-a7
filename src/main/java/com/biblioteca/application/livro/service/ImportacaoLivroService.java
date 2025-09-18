package com.biblioteca.application.livro.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.biblioteca.application.livro.usecases.IImportacaoLivroService;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;
import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.interfaces.ILivroRepository;
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
                .setHeader("titulo", "isbn", "data_publicacao", "editora", "cnpj_editora", "autores",
                        "cpf_cnpj_autores")
                .setSkipHeaderRecord(true)
                .build();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                CSVParser csvParser = new CSVParser(fileReader, csvFormat)) {

            for (CSVRecord csvRecord : csvParser) {
                System.err.println("Conteúdo do arquivo: " + csvRecord);
                // Processar Editora
                String nomeEditora = csvRecord.get("editora");
                String cnpjEditora = csvRecord.get("cnpj_editora").trim();
                Editora editora = buscarOuCriarEditora(nomeEditora, cnpjEditora);

                // Processar Autores
                String[] nomesAutores = csvRecord.get("autores").split(",");
                String[] cpfsCnpjsAutores = csvRecord.get("cpf_cnpj_autores").split(",");

                List<Long> autoresIds = java.util.stream.IntStream.range(0, nomesAutores.length)
                        .mapToObj(i -> buscarOuCriarAutor(nomesAutores[i].trim(), cpfsCnpjsAutores[i].trim()))
                        .map(Autor::getId)
                        .collect(Collectors.toList());

                // Criar LivroDTO
                LivroDTO livroDTO = new LivroDTO();
                livroDTO.setTitulo(csvRecord.get("titulo"));
                livroDTO.setIsbn(csvRecord.get("isbn"));
                livroDTO.setDataPublicacao(LocalDate.parse(csvRecord.get("data_publicacao")));
                livroDTO.setEditoraId(editora.getId());
                livroDTO.setAutoresIds(autoresIds);

                livroService.salvar(livroDTO);
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao processar o arquivo CSV: " + e.getMessage());
        }
    }

    private Editora buscarOuCriarEditora(String nomeEditora, String cnpjEditora) {
        Optional<Editora> editoraExistente = editoraService.buscarPorCnpj(cnpjEditora);
        System.err.println("Editora existente: " + editoraExistente);
        if (editoraExistente.isPresent()) {
            Editora editora = editoraExistente.get();
            if (!editora.getNome().equalsIgnoreCase(nomeEditora)) {
                editora.setNome(nomeEditora);
                EditoraDTO dto = EditoraDTO.fromEntity(editora);
                return editoraService.salvar(dto);
            }
            return editora;
        } else {
            EditoraDTO novaEditoraDTO = new EditoraDTO();
            novaEditoraDTO.setNome(nomeEditora);
            novaEditoraDTO.setCnpj(cnpjEditora);
            novaEditoraDTO.setTelefone("(00) 00000-0000"); // Telefone padrão
            novaEditoraDTO.setEmail("contato@" + nomeEditora.toLowerCase().replaceAll("[^a-z0-9]", "") + ".com");
            return editoraService.salvar(novaEditoraDTO);
        }
    }

    private Autor buscarOuCriarAutor(String nomeAutor, String cpfCnpj) {
        Optional<Autor> autorExistente = autorService.buscarPorCpfcnpj(cpfCnpj);

        if (autorExistente.isPresent()) {
            Autor autor = autorExistente.get();
            if (!autor.getNome().equalsIgnoreCase(nomeAutor)) {
                autor.setNome(nomeAutor);
                AutorDTO dto = AutorDTO.fromEntity(autor);
                return autorService.salvar(dto);
            }
            return autor;
        } else {
            AutorDTO novoAutorDTO = new AutorDTO();
            novoAutorDTO.setNome(nomeAutor);
            novoAutorDTO.setCpfcnpj(cpfCnpj);
            novoAutorDTO.setTelefone("(00) 00000-0000"); // Telefone padrão
            novoAutorDTO.setEmail("contato@" + nomeAutor.toLowerCase().replaceAll("[^a-z0-9]", "") + ".com");
            return autorService.salvar(novoAutorDTO);
        }
    }
}
