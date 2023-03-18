package com.oxande.scenarii.controller;


import com.oxande.scenarii.model.DBFilm;
import com.oxande.scenarii.model.User;
import com.oxande.scenarii.dto.DtoMappers;
import com.oxande.scenarii.repository.FilmRepository;
import com.oxande.scenarii.service.FilmService;
import com.oxande.scenarii.service.LatexGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@Path("/api/v1/film")
public class FilmController {

    @Inject
    LatexGenerator latexGenerator;
    
    @Inject
    FilmService filmService;

    @Inject
    FilmRepository filmRepository;

    @Context
    SecurityContext securityContext;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @NoArgsConstructor
    public static class FilmDto {

//        public static FilmDto map(Film f) {
//            FilmDto dto = new FilmDto();
//            dto.title = f.getTitle();
//            dto.ownerId = f.getOwner().getLogin();
//            return dto;
//        }

        private String title;
        private Long id;
        private String owner;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response createOne(com.oxande.scenarii.dto.FilmDto filmDto, @Context UriInfo uriInfo) {
        securityContext.getUserPrincipal();
        User user = User.fromPrincipal(securityContext.getUserPrincipal());
        DBFilm film = filmService.createFilm(user, filmDto.getTitle());
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(film.id)).build();
        return Response.created(uri).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<com.oxande.scenarii.dto.FilmDto> findAll(@QueryParam("page") @DefaultValue("0") int pageIndex,
                                                         @QueryParam("page_size") @DefaultValue("25") int pageSize) {
        
        return filmRepository.streamAll()
                /*.page(pageIndex, pageSize) */
                .filter(f -> f.visibleBy(securityContext.getUserPrincipal()))
                .map(DtoMappers::filmToDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public com.oxande.scenarii.dto.FilmDto findOneFilm(@PathParam("id") Long id) {
        return filmRepository.findByIdOptional(id)
                .map(DtoMappers::filmToDto)
                .orElse(null);
    }

    @GET
    @Path("{id}.tex")
    public Response showAsText(@PathParam("id") Long id) throws IOException {
        DBFilm film = filmRepository.findByIdOptional(id).orElse(null);
        if(film == null){
            return Response.status(RestResponse.Status.NOT_FOUND).build();
        }
        return Response.ok()
                .type(MediaType.valueOf("application/x-latex"))
                .entity(latexGenerator.produceLatex(film))
                .build();
    }

    @GET
    @Path("{id}.json")
    public Response showAsJson(@PathParam("id") Long id) throws IOException {
        DBFilm film = filmRepository.findByIdOptional(id).orElse(null);
        if(film == null){
            return Response.status(RestResponse.Status.NOT_FOUND).build();
        }
        return Response.ok()
                .type(MediaType.valueOf("application/x-latex"))
                .entity(latexGenerator.produceLatex(film))
                .build();
    }

}
