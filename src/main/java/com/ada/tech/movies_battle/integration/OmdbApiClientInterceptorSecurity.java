package com.ada.tech.movies_battle.integration;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

class OmdbApiClientInterceptorSecurity {
    @Value("${integration.omdbapi.key}")
    private String apiKey;

    @Bean
    RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.query("apiKey", apiKey);
    }
}
