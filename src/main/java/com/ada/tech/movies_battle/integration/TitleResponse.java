package com.ada.tech.movies_battle.integration;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import static java.util.Objects.isNull;

@Builder
@Jacksonized
public record TitleResponse(
        @JsonAlias({"Title", "title"})
        String title,
        String imdbID,
        @JsonAlias({"Poster", "poster"})
        String poster,
        String imdbRating,
        String imdbVotes
) {
    public Double score() {
        if (isNull(imdbVotes) || isNull(imdbRating)) {
            return null;
        }
        return parseDouble(imdbVotes) * parseDouble(imdbRating);
    }

    private static double parseDouble(String value) {
        return Double.parseDouble(
                value.replaceAll(",", "")
        );
    }
}
