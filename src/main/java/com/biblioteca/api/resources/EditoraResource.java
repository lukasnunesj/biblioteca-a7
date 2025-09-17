package com.biblioteca.api.resources;

import com.biblioteca.domain.entities.editora.DTO.EditoraDTO;
import com.biblioteca.domain.entities.editora.Editora;
import com.biblioteca.domain.entities.editora.interfaces.IEditoraService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/editoras")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EditoraResource {

    @Inject
    private IEditoraService editoraService;

    @GET
    public Response listarTodas() {
        List<EditoraDTO> editoras = editoraService.buscarTodos().stream()
                .map(EditoraDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(editoras).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return editoraService.buscarPorId(id)
                .map(editora -> Response.ok(EditoraDTO.fromEntity(editora)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response criar(EditoraDTO editoraDTO) {
        try {
            Editora editoraSalva = editoraService.salvar(editoraDTO);
            return Response.status(Response.Status.CREATED).entity(EditoraDTO.fromEntity(editoraSalva)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

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
