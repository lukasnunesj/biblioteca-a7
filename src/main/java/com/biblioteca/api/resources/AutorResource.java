package com.biblioteca.api.resources;

import com.biblioteca.domain.entities.autor.Autor;
import com.biblioteca.domain.entities.autor.DTO.AutorDTO;
import com.biblioteca.domain.entities.autor.interfaces.IAutorService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Recurso REST para operações relacionadas a autores.
 * <p>
 * Esta classe fornece endpoints para gerenciar autores no sistema,
 * incluindo listagem, busca, criação, atualização e remoção.
 * </p>
 *
 * @author Biblioteca A7
 * @version 1.0
 */
@Path("/autores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AutorResource {

    @Inject
    private IAutorService autorService;

    /**
     * Busca autores por um termo de pesquisa.
     *
     * @param termo o termo a ser pesquisado
     * @return Response contendo a lista de autores que correspondem ao termo
     */
    @GET
    @Path("/search")
    public Response search(@QueryParam("termo") String termo) {
        List<AutorDTO> autores = autorService.findByTermo(termo).stream()
                .map(AutorDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(autores).build();
    }

    /**
     * Lista todos os autores cadastrados no sistema.
     *
     * @return Response contendo a lista de autores em formato DTO
     */
    @GET
    public Response listarTodos() {
        List<AutorDTO> autores = autorService.buscarTodos().stream()
                .map(AutorDTO::fromEntity)
                .collect(Collectors.toList());
        return Response.ok(autores).build();
    }

    /**
     * Busca um autor pelo seu ID.
     *
     * @param id o ID do autor a ser buscado
     * @return Response contendo o autor encontrado ou 404 se não encontrado
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return autorService.buscarPorId(id)
                .map(autor -> Response.ok(AutorDTO.fromEntity(autor)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Cria um novo autor no sistema.
     *
     * @param autorDTO o DTO contendo os dados do autor a ser criado
     * @return Response contendo o autor criado ou mensagem de erro
     */
    @POST
    public Response criar(AutorDTO autorDTO) {
        try {
            Autor autorSalvo = autorService.salvar(autorDTO);
            return Response.status(Response.Status.CREATED).entity(AutorDTO.fromEntity(autorSalvo)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Atualiza um autor existente.
     *
     * @param id o ID do autor a ser atualizado
     * @param autorDTO o DTO contendo os novos dados do autor
     * @return Response contendo o autor atualizado ou mensagem de erro
     */
    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") Long id, AutorDTO autorDTO) {
        try {
            autorDTO.setId(id);
            Autor autorAtualizado = autorService.salvar(autorDTO);
            return Response.ok(AutorDTO.fromEntity(autorAtualizado)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    /**
     * Remove um autor do sistema.
     *
     * @param id o ID do autor a ser removido
     * @return Response indicando sucesso ou erro na remoção
     */
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
