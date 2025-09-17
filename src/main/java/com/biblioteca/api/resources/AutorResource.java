package com.biblioteca.api.resources;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/autores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AutorResource {

    @Inject
    private IAutorService autorService;

    @GET
    public Response listarTodos() {
        List<AutorDTO> autores = autorService.buscarTodos().stream()
                .map(AutorDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(autores).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return autorService.buscarPorId(id)
                .map(autor -> Response.ok(AutorDTO.fromEntity(autor)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response criar(AutorDTO autorDTO) {
        try {
            Autor autorSalvo = autorService.salvar(autorDTO);
            return Response.status(Response.Status.CREATED).entity(AutorDTO.fromEntity(autorSalvo)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, AutorDTO autorDTO) {
        try {
            autorDTO.setId(id); // Garante que o ID do DTO é o mesmo da URL
            Autor autorAtualizado = autorService.salvar(autorDTO);
            return Response.ok(AutorDTO.fromEntity(autorAtualizado)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") Long id) {
        try {
            AutorDTO dto = new AutorDTO();
            dto.setId(id);
            autorService.remover(dto);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
