package com.oxande.scenarii.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.stream.Stream;

/**
 * An actor. Note the photography of an actor is found in "/actor/photo/{id}".
 */
@Entity
@Table(name = "actor")
@Setter @Getter
@NoArgsConstructor
@SuppressWarnings("java:S2160")
public class Actor extends DBEntity {
    public static final char FEMALE ='F';
    public static final char MALE ='M';
    public static final char ROBOT ='R';
    public static final char SPECIAL ='S';
    public static final char CHILD ='C';
    public static final char NEW_BORN ='B';

    /**
     * Name of the actor
     */
    private String name;

    /**
     * The age (can be any specification).
     */
    private String age;

    @Column(length=4000)
    private String comments;

    private char sex = SPECIAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id")
    private DBFilm film;


    public static Stream<Actor> streamByFilm(DBFilm film) {
        return find("film", film).stream();
    }
}
