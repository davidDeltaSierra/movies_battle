package com.ada.tech.movies_battle.common;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "hibernate_sequence")
    @SequenceGenerator(name = "hibernate_sequence", allocationSize = 1)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.id == null || this.getClass() != o.getClass()) return false;
        AbstractEntity that = (AbstractEntity) o;
        return this.id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
