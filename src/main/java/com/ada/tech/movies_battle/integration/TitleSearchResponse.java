package com.ada.tech.movies_battle.integration;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder
@Jacksonized
public record TitleSearchResponse(
        @JsonAlias("Search")
        List<TitleResponse> search
) {
}
