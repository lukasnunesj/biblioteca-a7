package com.biblioteca.api.resources;

import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.application.livro.usecases.IImportacaoLivroService;
import com.biblioteca.application.livro.service.CadastrarLivroPorIsbnService;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recurso REST para operações relacionadas a livros.
 * <p>
 * Esta classe fornece endpoints para gerenciar livros no sistema,
 * incluindo listagem, busca, criação, atualização, remoção e importação.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
@Path("/livros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LivroResource {

    @Inject
    private ILivroService livroService;

    @Inject
    private IImportacaoLivroService importacaoLivroService;

    @Inject
    private CadastrarLivroPorIsbnService cadastrarLivroPorIsbnService;

    /**
     * Lista todos os livros cadastrados no sistema.
     *
     * @return Response contendo a lista de livros em formato DTO
     */
    @GET
    public Response listarTodos() {
        List<LivroDTO> livros = livroService.buscarTodos().stream()
                .map(LivroDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(livros).build();
    }

    /**
     * Busca livros por um termo de pesquisa.
     *
     * @param termo o termo a ser pesquisado
     * @return Response contendo a lista de livros que correspondem ao termo
     */
    @GET
    @Path("/search")
    public Response search(@QueryParam("termo") String termo) {
        List<LivroDTO> livros = livroService.findByTermo(termo).stream()
                .map(LivroDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(livros).build();
    }

    /**
     * Busca um livro pelo seu ID.
     *
     * @param id o ID do livro a ser buscado
     * @return Response contendo o livro encontrado ou 404 se não encontrado
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return livroService.buscarPorId(id)
                .map(livro -> Response.ok(LivroDTO.fromEntity(livro)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Cadastra um livro a partir de seu ISBN, buscando informações na OpenLibrary.
     *
     * @param isbn o ISBN do livro a ser cadastrado
     * @return Response contendo o livro cadastrado ou mensagem de erro
     */
    @POST
    @Path("/isbn/{isbn}")
    public Response cadastrarPorIsbn(@PathParam("isbn") String isbn) {
        try {
            return cadastrarLivroPorIsbnService.execute(isbn)
                    .map(livro -> Response.status(Response.Status.CREATED).entity(LivroDTO.fromEntity(livro)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity("Livro não encontrado na OpenLibrary.").build());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Cria um novo livro no sistema.
     *
     * @param livroDTO o DTO contendo os dados do livro a ser criado
     * @return Response contendo o livro criado ou mensagem de erro
     */
    @POST
    public Response criar(LivroDTO livroDTO) {
        try {
            Livro livroSalvo = livroService.salvar(livroDTO);
            return Response.status(Response.Status.CREATED).entity(LivroDTO.fromEntity(livroSalvo)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Atualiza um livro existente.
     *
     * @param id o ID do livro a ser atualizado
     * @param livroDTO o DTO contendo os novos dados do livro
     * @return Response contendo o livro atualizado ou mensagem de erro
     */
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, LivroDTO livroDTO) {
        try {
            livroDTO.setId(id);
            Livro livroAtualizado = livroService.salvar(livroDTO);
            return Response.ok(LivroDTO.fromEntity(livroAtualizado)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Importa livros a partir de um arquivo CSV.
     *
     * @param input o objeto MultipartFormDataInput contendo o arquivo CSV
     * @return Response indicando sucesso ou erro na importação
     */
    @POST
    @Path("/importar-csv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response importarCsv(MultipartFormDataInput input) {
        try {
            InputStream inputStream = input.getFormDataPart("file", InputStream.class, null);
            importacaoLivroService.importar(inputStream);
            return Response.ok("Arquivo CSV importado com sucesso.").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Erro ao importar CSV: " + e.getMessage()).build();
        }
    }

    /**
     * Remove um livro do sistema.
     *
     * @param id o ID do livro a ser removido
     * @return Response indicando sucesso ou erro na remoção
     */
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        try {
            LivroDTO dto = new LivroDTO();
            dto.setId(id);
            livroService.remover(dto);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
