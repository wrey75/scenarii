package com.oxande.scenarii.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "user_access")
@Data
@NoArgsConstructor
public class UserRole {
    
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private DBFilm film;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private String roles = "owner";

}
