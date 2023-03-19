package com.oxande.scenarii.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.oxande.scenarii.model.Actor;
import com.oxande.scenarii.model.DBFilm;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class ActorRepository implements PanacheRepository<Actor> {
    public List<Actor> findByFilm(DBFilm film) {
        return Actor.find("film", film).list();
    }
}
