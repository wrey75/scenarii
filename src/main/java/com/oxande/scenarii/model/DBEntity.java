package com.oxande.scenarii.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Same as a {@link io.quarkus.hibernate.orm.panache.PanacheEntity} but provides
 * the equals and the hashcode.
 */
@MappedSuperclass
public abstract class DBEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @Getter
    public Long id;

    @Override
    public String toString() {
        String name = this.getClass().getSimpleName();
        return name + "<" + this.id + ">";
    }

    @Override
    public boolean equals(Object other) {
        if (this.id != null && this.getClass().isInstance(other)) {
            return this.getId().equals(((DBEntity) other).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}