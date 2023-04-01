package com.ada.tech.movies_battle.quiz;

import com.ada.tech.movies_battle.common.BaseIntegrationTest;
import com.ada.tech.movies_battle.common.JwtHandler;
import com.ada.tech.movies_battle.common.JwtPayload;
import com.ada.tech.movies_battle.integration.OmdbApiClient;
import com.ada.tech.movies_battle.integration.TitleResponse;
import com.ada.tech.movies_battle.integration.TitleSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class QuizControllerTest extends BaseIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private JwtHandler jwtHandler;
    @Autowired
    private OmdbApiClient omdbApiClient;

    @BeforeEach
    void init() {
        Mockito.when(omdbApiClient.findTitleById(any()))
                .thenReturn(ResponseEntity.ok(mock1()));
        Mockito.when(jwtHandler.decode(any()))
                .thenReturn(new JwtPayload("shoto_todoroki"));
    }

    @Test
    void startTest() {
        var response = restTemplate.exchange(
                "/v1/quiz",
                HttpMethod.POST,
                null,
                QuizSender.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasNoNullFieldsOrProperties();
    }

    @Test
    @Sql({"/h2-scripts/quiz.sql", "/h2-scripts/quiz_round.sql"})
    void nextTest() {
        var response = restTemplate.exchange(
                "/v1/quiz/100",
                HttpMethod.POST,
                new HttpEntity<>(QuizReceiver.builder()
                        .hint("tt2527336")
                        .build()),
                QuizSender.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasNoNullFieldsOrProperties();
    }

    @Test
    @Sql({"/h2-scripts/quiz.sql"})
    void closeTest() {
        var response = restTemplate.exchange(
                "/v1/quiz/100/close",
                HttpMethod.POST,
                null,
                QuizSender.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Sql({"/h2-scripts/quiz.sql"})
    void rankingTest() {
        var response = restTemplate.exchange(
                "/v1/quiz/ranking",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RankingViewImpl>>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty().hasSize(2);
    }

    private static TitleResponse mock1() {
        return TitleResponse.builder()
                .title("Star Wars: Episode VIII - The Last Jedi")
                .imdbID("tt2527336")
                .poster("https://m.media-amazon.com/images/M/MV5BMjQ1MzcxNjg4N15BMl5BanBnXkFtZTgwNzgwMjY4MzI@._V1_SX300.jpg")
                .imdbRating("12345")
                .imdbVotes("12")
                .build();
    }

    private static TitleResponse mock2() {
        return TitleResponse.builder()
                .title("John Wick: Chapter 3 - Parabellum")
                .imdbID("tt6146586")
                .poster("https://m.media-amazon.com/images/M/MV5BMDg2YzI0ODctYjliMy00NTU0LTkxODYtYTNkNjQwMzVmOTcxXkEyXkFqcGdeQXVyNjg2NjQwMDQ@._V1_SX300.jpg")
                .imdbRating("12345")
                .imdbVotes("12")
                .build();
    }

    @TestConfiguration
    public static class MockTitlesOnStart implements CommandLineRunner {
        @MockBean
        private OmdbApiClient omdbApiClient;

        @Override
        public void run(String... args) {
            Mockito.when(omdbApiClient.findAllTitleByTerm("the twilight saga"))
                    .thenReturn(
                            ResponseEntity.ok(
                                    TitleSearchResponse.builder()
                                            .search(List.of(mock1(), mock2()))
                                            .build()
                            )
                    );
        }
    }
}
