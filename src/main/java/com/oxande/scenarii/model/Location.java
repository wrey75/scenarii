package com.oxande.scenarii.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "places")
@Setter @Getter
@SuppressWarnings("java:S2160")
public class Location extends DBEntity {
    
    private String name;
    
    private String comments;
}
