package com.oxande.scenarii.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.security.Principal;

@Entity
@Table(name = "film")
@NoArgsConstructor
@Getter
@Setter
public class DBFilm extends DBEntity {
    public static final String PUBLIC = "PUBLIC";
    public static final String PRIVATE = "PRIVATE";

    private String title;

    private String visibilty = PUBLIC;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private DBUser owner;

    public boolean visibleBy(Principal principal) {
        if (this.visibilty.equalsIgnoreCase(PUBLIC)) {
            return true;
        }
        return (principal != null && principal.getName().equals(this.owner.getLogin()));
    }

}
