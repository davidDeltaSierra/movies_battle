package com.ada.tech.movies_battle.quiz;

import com.ada.tech.movies_battle.common.AbstractEntity;
import com.ada.tech.movies_battle.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.isNull;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quiz")
public class Quiz extends AbstractEntity {
    @Column
    @Builder.Default
    private int hits = 0;

    @Column
    @Builder.Default
    private int errors = 0;

    @Column(name = "hit_percent")
    @Builder.Default
    private double hitPercent = 0;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column
    @Builder.Default
    private boolean finalized = false;

    @JoinColumn(nullable = false, name = "id_owner")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User owner;

    @OrderBy("id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "quiz")
    @ToString.Exclude
    private Set<QuizRound> rounds = new LinkedHashSet<>();

    public User getOwner() {
        return Hibernate.isInitialized(owner) ? owner : null;
    }

    public Set<QuizRound> getRounds() {
        return Hibernate.isInitialized(rounds) ? rounds : null;
    }

    public Optional<QuizRound> findLastRound() {
        if (isNull(getRounds())) {
            return Optional.empty();
        }
        return getRounds().stream()
                .reduce((acc, cur) -> cur);
    }

    public boolean contains(String imdbID) {
        if (isNull(getRounds())) {
            return false;
        }
        return getRounds().stream()
                .anyMatch(it -> it.getTitle1().equals(imdbID) || it.getTitle2().equals(imdbID));
    }

    public double calculateHitPercent() {
        var total = hits + errors;
        return (100.0 / total) * hits;
    }
}
