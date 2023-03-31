package com.ada.tech.movies_battle.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record LoginRequest(
        @NotEmpty
        String username,
        @NotEmpty
        String password
) {
}
