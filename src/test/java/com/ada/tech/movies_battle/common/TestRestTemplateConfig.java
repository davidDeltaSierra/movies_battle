package com.ada.tech.movies_battle.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class TestRestTemplateConfig {
    @LocalServerPort
    private int port;
    @Value("${server.servlet.context-path}")
    private String context;

    @Bean
    TestRestTemplate testRestTemplate() {
        return new TestRestTemplate(
                new RestTemplateBuilder()
                        .rootUri("http://localhost:%s/%s".formatted(port, context))
        );
    }
}