package com.oxande.scenarii.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "sc_config")
@Setter @Getter
@NoArgsConstructor
@ToString
public class Configuration extends PanacheEntityBase {
    @Id
    private String key;
    
    private String value;
}
