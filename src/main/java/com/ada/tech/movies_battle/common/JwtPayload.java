package com.ada.tech.movies_battle.common;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record JwtPayload(
        String username
) {
}
