package com.ada.tech.movies_battle.quiz;

import com.ada.tech.movies_battle.integration.TitleResponse;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record QuizSender(
        Long quizId,
        int errors,
        int hits,
        TitleResponse option1,
        TitleResponse option2
) {
}
