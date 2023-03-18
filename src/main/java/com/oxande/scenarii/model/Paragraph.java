package com.oxande.scenarii.model;

import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.List;
import java.util.stream.Stream;

/**
 * This entity stores the
 */
@Entity
@Table(name = "screenplay")
@Setter
@Getter
public class Paragraph extends AbstractParagraph {
    public static final String SEPARATOR = "|";

    public static final char DIALOG = 'D';
    public static final char INTERCUT = 'I';
    public static final char SCENE = 'S';
    public static final char TITLE = 'T';

    /**
     * Not in the abstract because in the "history", we MUST have the version
     * of the original {@link Paragraph}. This is a trick to workaround JPA
     * behind the scenes stuff.
     *
     */
    @Version
    private Integer version;

    public static Stream<Paragraph> streamByFilm(DBFilm film) {
        return find("film", film).stream();
    }

    public static Stream<Paragraph> streamLastVersionByFilm(DBFilm film) {
        return find("film", Sort.ascending("rank"), film)
                .filter("active", Parameters.with("replacedBy", null))
                .stream();
    }

    public void setDirections(List<String> directions) {
        boolean first = true;
        StringBuilder buf = new StringBuilder();
        for (String d : directions) {
            if (first) {
                buf.append(Paragraph.SEPARATOR);
                first = false;
            }
            if (StringUtils.isNotEmpty(d)) buf.append(d);
        }
        this.setDirection(buf.toString());
    }
}
