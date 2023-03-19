package com.oxande.scenarii.controller;

import com.oxande.scenarii.Utils;
import com.oxande.scenarii.dto.ActorDto;
import com.oxande.scenarii.model.Actor;
import com.oxande.scenarii.model.DBFilm;
import com.oxande.scenarii.model.DBUser;
import com.oxande.scenarii.repository.ActorRepository;
import com.oxande.scenarii.repository.FilmRepository;
import com.oxande.scenarii.service.FilmService;
import io.quarkus.security.UnauthorizedException;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.security.Principal;
import java.util.List;

@Path("/api/v1")
public class ActorController {
    
    @Inject
    ActorRepository actorRepository;

    @Inject
    FilmRepository filmRepository;

    @Inject
    FilmService filmService;
    
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
    @Path("film/{film}/actor")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Actor> findActors(@PathParam("film") Long filmId) {
        DBFilm film = loadFilm(filmId);
        return actorRepository.findByFilm(film);
    }

    @POST
    @Path("film/{filmId}/actor")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(DBUser.USER_ROLE)
    public Response addActor(@PathParam("filmId") Long filmId, ActorDto dto, @Context UriInfo uriInfo) {
        DBFilm film = loadFilm(filmId);
        DBUser user = DBUser.fromSecurityContext(securityContext);
        Actor actor = filmService.createActor(film, user, dto.getName());
        actor.setAge(Utils.checked(dto.getAge()));
        actor.setComments(Utils.checkedCmt(dto.getComments()));
        actor.setAge(Utils.checked(dto.getAge()));
        actor.setSex(Utils.checkIn(dto.getSex(), Actor.MALE, Actor.FEMALE, Actor.SPECIAL));
        actor.persist();
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(actor.id)).build();
        return Response.created(uri).build();
    }

    @Path("film/{filmId}/actor/{id}")
    public Response findActorPhotos(@PathParam("filmId") Long id) {
        DBFilm film = loadFilm(id);
        Long photoId = actorRepository.findByIdOptional(id)
                .filter(a -> a.getFilm().equals(film))
                .map(Actor::getId)
                .orElse(null);
        return Response.noContent().build();
    }
}
