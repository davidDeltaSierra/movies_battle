package com.ada.tech.movies_battle.quiz;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface QuizRepository extends JpaRepository<Quiz, Long> {
    @EntityGraph(attributePaths = {"rounds"})
    Optional<Quiz> findByIdAndOwner_Id(Long id, Long OwnerId);
}
