package com.oxande.scenarii.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.stream.Stream;

@Entity
@Table(name = "actor")
@Setter @Getter
@NoArgsConstructor
@SuppressWarnings("java:S2160")
public class Actor extends DBEntity {

    /**
     * Le nom du personnage
     */
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private DBFilm film;


    public static Stream<Actor> streamByFilm(DBFilm film) {
        return find("film", film).stream();
    }
}
