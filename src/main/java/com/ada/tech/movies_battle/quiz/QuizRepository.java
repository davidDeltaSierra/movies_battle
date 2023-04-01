package com.ada.tech.movies_battle.quiz;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface QuizRepository extends JpaRepository<Quiz, Long> {
    @EntityGraph(attributePaths = {"rounds"})
    Optional<Quiz> findByIdAndOwner_Id(Long id, Long OwnerId);

    @Query("""
            select ((count(*) * sum(q.hitPercent)) / count(*)) as percent, u.username as username, count(*) as total
            from Quiz q
            inner join User u on u.id = q.owner.id
            where q.finalized = true
            group by u.username
            order by (count(*) * sum(q.hitPercent) / count(*)) desc, count(*) desc
            """)
    List<RankingView> getRanking();
}
