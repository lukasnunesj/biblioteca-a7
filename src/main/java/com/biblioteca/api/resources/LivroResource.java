package com.biblioteca.api.resources;

import com.biblioteca.domain.entities.livro.DTO.LivroDTO;
import com.biblioteca.domain.entities.livro.Livro;
import com.biblioteca.domain.entities.livro.interfaces.ILivroService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/livros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LivroResource {

    @Inject
    private ILivroService livroService;

    @GET
    public Response listarTodos() {
        List<LivroDTO> livros = livroService.buscarTodos().stream()
                .map(LivroDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(livros).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return livroService.buscarPorId(id)
                .map(livro -> Response.ok(LivroDTO.fromEntity(livro)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/isbn/{isbn}")
    public Response cadastrarPorIsbn(@PathParam("isbn") String isbn) {
        try {
            return livroService.cadastrarPorIsbn(isbn)
                    .map(livro -> Response.status(Response.Status.CREATED).entity(LivroDTO.fromEntity(livro)).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity("Livro não encontrado na OpenLibrary.").build());
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    public Response criar(LivroDTO livroDTO) {
        try {
            Livro livroSalvo = livroService.salvar(livroDTO);
            return Response.status(Response.Status.CREATED).entity(LivroDTO.fromEntity(livroSalvo)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

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
