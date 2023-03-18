package com.oxande.scenarii.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;

@Entity
@Table(name="history")
@NoArgsConstructor
@Getter
@Setter
@SuppressWarnings("java:S2160")
public class ParagraphHistory extends AbstractParagraph {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * The original id linked to the original dialog.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialog_id")
    private Paragraph original;

    /**
     * This version is NOT an optimistic way of saving stuff. It is
     * a copy of the original version from {@link Paragraph}.
     */
    private Integer version;

    /**
     * Construct the histoty from the original {@link Paragraph}.
     *
     * @param d the original {@link Paragraph} (before the changes).
     * @return the copy to be saved.
     */
    public ParagraphHistory from(Paragraph d) {
        try {
            ParagraphHistory change = new ParagraphHistory();
            BeanUtils.copyProperties(change, d);
            change.id = null; // provides a new identifier
            change.original = d;
            return change;
        } catch (InvocationTargetException | IllegalAccessException ex) {
            // Make it a runtime exception because should NEVER arise.
            throw new UnsupportedOperationException("Can not copy " + d, ex);
        }
    }
}
