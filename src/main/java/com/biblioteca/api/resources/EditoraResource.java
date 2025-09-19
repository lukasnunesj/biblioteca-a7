package com.biblioteca.api.resources;

import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recurso REST para operações relacionadas a editoras.
 * <p>
 * Esta classe fornece endpoints para gerenciar editoras no sistema,
 * incluindo listagem, busca, criação, atualização e remoção.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
@Path("/editoras")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EditoraResource {

    @Inject
    private IEditoraService editoraService;

    /**
     * Busca editoras por um termo de pesquisa.
     *
     * @param termo o termo a ser pesquisado
     * @return Response contendo a lista de editoras que correspondem ao termo
     */
    @GET
    @Path("/search")
    public Response search(@QueryParam("termo") String termo) {
        List<EditoraDTO> editoras = editoraService.findByTermo(termo).stream()
                .map(EditoraDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(editoras).build();
    }

    /**
     * Lista todas as editoras cadastradas no sistema.
     *
     * @return Response contendo a lista de editoras em formato DTO
     */
    @GET
    public Response listarTodos() {
        List<EditoraDTO> editoras = editoraService.buscarTodos().stream()
                .map(EditoraDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(editoras).build();
    }

    /**
     * Busca uma editora pelo seu ID.
     *
     * @param id o ID da editora a ser buscada
     * @return Response contendo a editora encontrada ou 404 se não encontrada
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return editoraService.buscarPorId(id)
                .map(editora -> Response.ok(EditoraDTO.fromEntity(editora)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Cria uma nova editora no sistema.
     *
     * @param editoraDTO o DTO contendo os dados da editora a ser criada
     * @return Response contendo a editora criada ou mensagem de erro
     */
    @POST
    public Response criar(EditoraDTO editoraDTO) {
        try {
            Editora editoraSalva = editoraService.salvar(editoraDTO);
            return Response.status(Response.Status.CREATED).entity(EditoraDTO.fromEntity(editoraSalva)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Atualiza uma editora existente.
     *
     * @param id o ID da editora a ser atualizada
     * @param editoraDTO o DTO contendo os novos dados da editora
     * @return Response contendo a editora atualizada ou mensagem de erro
     */
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, EditoraDTO editoraDTO) {
        try {
            editoraDTO.setId(id);
            Editora editoraAtualizada = editoraService.salvar(editoraDTO);
            return Response.ok(EditoraDTO.fromEntity(editoraAtualizada)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Remove uma editora do sistema.
     *
     * @param id o ID da editora a ser removida
     * @return Response indicando sucesso ou erro na remoção
     */
    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        try {
            EditoraDTO dto = new EditoraDTO();
            dto.setId(id);
            editoraService.remover(dto);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
