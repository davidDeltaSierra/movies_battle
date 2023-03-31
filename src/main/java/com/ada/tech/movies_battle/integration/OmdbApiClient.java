package com.ada.tech.movies_battle.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "OmdbApiClient",
        url = "${integration.omdbapi.url}",
        configuration = OmdbApiClientInterceptorSecurity.class
)
public interface OmdbApiClient {
    @GetMapping
    ResponseEntity<TitleResponse> findTitleById(
            @RequestParam("i") String id,
            @RequestParam(value = "type", defaultValue = "movie") String type
    );

    @GetMapping
    ResponseEntity<TitleSearchResponse> findAllTitleByTerm(
            @RequestParam("s") String term,
            @RequestParam(value = "type", defaultValue = "movie") String type
    );

    default ResponseEntity<TitleResponse> findTitleById(String id) {
        return findTitleById(id, null);
    }

    default ResponseEntity<TitleSearchResponse> findAllTitleByTerm(String term) {
        return findAllTitleByTerm(term, null);
    }
}
