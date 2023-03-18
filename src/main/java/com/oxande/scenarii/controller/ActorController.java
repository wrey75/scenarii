package com.oxande.scenarii.controller;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.oxande.scenarii.model.Actor;
import com.oxande.scenarii.model.DBFilm;
import com.oxande.scenarii.model.User;
import com.oxande.scenarii.repository.ActorRepository;
import com.oxande.scenarii.repository.FilmRepository;
import com.oxande.scenarii.service.FilmService;

import io.quarkus.security.UnauthorizedException;

@Path("/api/v1")
public class ActorController {
    
    @Inject
    ActorRepository actorRepository;

    @Inject
    FilmRepository filmRepository;

    FilmService filmService;

    public static class ActorDto extends Actor {
        
    }
    
    @Context
    SecurityContext securityContext;
    
    DBFilm loadFilm(Long id){
        DBFilm f = filmRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Film not found"));
        Principal principal = securityContext.getUserPrincipal();
        if(!f.visibleBy(principal)){
            throw new UnauthorizedException("You can not access this film");
        }
        return f;
    }
    
    @GET
    @Path("film/{filmId}/actor")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Actor> findActors(@PathParam("filmId") Long id) {
        DBFilm film = loadFilm(id);
        return actorRepository.findByFilm(film);
    }

    @POST
    @Path("film/{filmId}/actor")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(User.USER_ROLE)
    public Response addActor(@PathParam("filmId") Long id, ActorDto dto, @Context UriInfo uriInfo) {
        DBFilm film = loadFilm(id);
        Actor actor = filmService.createActor(film, null, dto.getName());
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(actor.id)).build();
        return Response.created(uri).build();
    }
}
