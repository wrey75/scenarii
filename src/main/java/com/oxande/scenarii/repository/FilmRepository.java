package com.oxande.scenarii.repository;

import javax.enterprise.context.ApplicationScoped;

import com.oxande.scenarii.model.DBFilm;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class FilmRepository implements PanacheRepository<DBFilm> {
}
