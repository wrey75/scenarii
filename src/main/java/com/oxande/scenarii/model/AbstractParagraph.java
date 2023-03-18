package com.oxande.scenarii.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Setter @Getter
@NoArgsConstructor
@MappedSuperclass
@SuppressWarnings("java:S2160")
abstract class AbstractParagraph extends DBEntity {

    /**
     * The date of the creation for this dialog
     */
    private Date date = new Date();

    private char type = Paragraph.DIALOG;

    /**
     * The rank is the order for the sequences. It can be changed.
     */
    private Integer rank;

    private String direction;

    @Column(length = 4000)
    private String text;

    /**
     * The user who added the data.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = true)
    private Actor actor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id", nullable = false)
    private DBFilm film;



    public String[] getDirections(int expectedEntries) {
        String[] array = new String[expectedEntries];
        List<String> directions = getDirections();
        for(int i = 0; i < expectedEntries; i++){
            array[i] = (i < directions.size() ? directions.get(i) : "");
        }
        return array;
    }
    
    public List<String> getDirections() {
        if (this.direction != null) {
            return Arrays.stream(this.direction.split("\\|"))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
