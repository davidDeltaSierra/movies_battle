package com.ada.tech.movies_battle.user;

import com.ada.tech.movies_battle.common.BaseIntegrationTest;
import com.ada.tech.movies_battle.common.JwtPayload;
import com.ada.tech.movies_battle.integration.OmdbApiClient;
import com.ada.tech.movies_battle.integration.TitleSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class UserControllerTest extends BaseIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private OmdbApiClient omdbApiClient;

    @BeforeEach
    void init() {
        Mockito.when(omdbApiClient.findAllTitleByTerm(any(), any()))
                .thenReturn(getOk());
        Mockito.when(omdbApiClient.findAllTitleByTerm(any()))
                .thenReturn(getOk());
    }

    private static ResponseEntity<TitleSearchResponse> getOk() {
        return ResponseEntity.ok(
                TitleSearchResponse.builder()
                        .search(List.of())
                        .build()
        );
    }

    @Test
    void authTest() {
        var response = restTemplate.exchange(
                "public/v1/user/auth",
                HttpMethod.POST,
                new HttpEntity<>(LoginRequest.builder()
                        .username("shoto_todoroki")
                        .password("shoto_todoroki")
                        .build()),
                JwtPayload.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders()).containsKey("Authorization");
        assertThat(response.getBody()).hasNoNullFieldsOrProperties();
    }
}
