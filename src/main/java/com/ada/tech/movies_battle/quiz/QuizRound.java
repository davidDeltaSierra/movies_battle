package com.ada.tech.movies_battle.quiz;

import com.ada.tech.movies_battle.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quiz_round")
public class QuizRound extends AbstractEntity {
    @Column
    private String title1;

    @Column
    private String title2;

    @Column
    private String hint;

    @Column(name = "right_answer")
    private String rightAnswer;

    @JoinColumn(nullable = false, name = "id_quiz")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Quiz quiz;

    public Quiz getQuiz() {
        return Hibernate.isInitialized(quiz) ? quiz : null;
    }
}
