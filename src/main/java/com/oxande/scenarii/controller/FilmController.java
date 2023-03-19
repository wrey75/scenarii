package com.oxande.scenarii.controller;


import com.oxande.scenarii.dto.FilmDto;
import com.oxande.scenarii.model.DBFilm;
import com.oxande.scenarii.model.DBUser;
import com.oxande.scenarii.dto.DtoMappers;
import com.oxande.scenarii.repository.FilmRepository;
import com.oxande.scenarii.service.FilmService;
import com.oxande.scenarii.service.LatexGenerator;
import org.jboss.resteasy.reactive.RestResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;


@Path("/api/v1")
public class FilmController {

    @Inject
    LatexGenerator latexGenerator;
    
    @Inject
    FilmService filmService;

    @Inject
    FilmRepository filmRepository;

    @Context
    SecurityContext securityContext;

    @POST
    @Path("film")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response createOne(com.oxande.scenarii.dto.FilmDto filmDto, @Context UriInfo uriInfo) {
        securityContext.getUserPrincipal();
        DBUser user = DBUser.fromSecurityContext(securityContext);
        DBFilm film = filmService.createFilm(user, filmDto.getTitle());
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(film.id)).build();
        return Response.created(uri).build();
    }

    @GET
    @Path("film")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FilmDto> findAll(@QueryParam("page") @DefaultValue("0") int pageIndex,
                                                         @QueryParam("page_size") @DefaultValue("25") int pageSize) {
        
        return filmRepository.streamAll()
                /*.page(pageIndex, pageSize) */
                .filter(f -> f.visibleBy(securityContext.getUserPrincipal()))
                .map(DtoMappers::filmToDto)
                .collect(Collectors.toList());
    }

    @GET
    @Path("film/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public FilmDto findOneFilm(@PathParam("id") Long id) {
        return filmRepository.findByIdOptional(id)
                .map(DtoMappers::filmToDto)
                .orElse(null);
    }

    @GET
    @Path("film/{id}.tex")
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
    @Path("/film/{id}.json")
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
