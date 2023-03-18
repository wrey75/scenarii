package com.oxande.scenarii.service;

import com.oxande.scenarii.model.Actor;
import com.oxande.scenarii.model.DBFilm;
import com.oxande.scenarii.model.Paragraph;
import com.oxande.scenarii.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Iterator;

@ApplicationScoped
public class FilmService {
    @Inject
    EntityManager em;

    @Transactional
    public DBFilm createFilm(User user, String title) {
        DBFilm film = new DBFilm();
        film.setTitle(title);
        film.setOwner(user);
        film.persist();
        return film;
    }

    @Transactional
    public Actor createActor(DBFilm f, User u, String name){
        Actor actor = new Actor();
        actor.setFilm(f);
        actor.setName(name);
        actor.persist();
        return actor;
    }
    
    /**
     * Complete deletion of a film
     * 
     * @param film the film to delete
     */
    @Transactional
    public void deleteFilm(DBFilm film) {
        Actor.streamByFilm(film).forEach(Actor::delete);
        Paragraph.streamByFilm(film).forEach(Paragraph::delete);
        film.delete();
    }


    public Iterator<Paragraph> streamCurrentDialogs(DBFilm film) {
        return Paragraph.streamByFilm(film).iterator();
    }
}
