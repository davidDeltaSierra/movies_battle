package com.ada.tech.movies_battle.quiz;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record QuizReceiver(
        @NotEmpty
        String hint
) {
}
