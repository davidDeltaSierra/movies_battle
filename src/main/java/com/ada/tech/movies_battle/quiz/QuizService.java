package com.ada.tech.movies_battle.quiz;

import com.ada.tech.movies_battle.integration.OmdbApiClient;
import com.ada.tech.movies_battle.integration.TitleResponse;
import com.ada.tech.movies_battle.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService implements CommandLineRunner {
    private static List<TitleResponse> TITLES;
    private final QuizRepository quizRepository;
    private final UserService userService;
    private final OmdbApiClient omdbApiClient;

    /**
     * Carrega lista de filmes em memoria no startup
     */
    @Override
    public void run(String... args) {
        TITLES = Stream.of(
                        omdbApiClient.findAllTitleByTerm("the twilight saga").getBody().search().stream(),
                        omdbApiClient.findAllTitleByTerm("the avengers").getBody().search().stream(),
                        omdbApiClient.findAllTitleByTerm("star wars").getBody().search().stream(),
                        omdbApiClient.findAllTitleByTerm("john wick").getBody().search().stream(),
                        omdbApiClient.findAllTitleByTerm("matrix").getBody().search().stream(),
                        omdbApiClient.findAllTitleByTerm("mad max").getBody().search().stream()
                )
                .parallel()
                .flatMap(stream -> stream)
                .toList();
        log.info("Load titles: {}", TITLES.size());
    }

    public QuizSender start() {
        var pair = randomPair();
        var quiz = Quiz.builder()
                .owner(userService.me())
                .build();
        var round = QuizRound.builder()
                .title1(pair.getFirst().imdbID())
                .title2(pair.getSecond().imdbID())
                .quiz(quiz)
                .build();
        quiz.setRounds(Set.of(round));
        quizRepository.save(quiz);
        return QuizSender.builder()
                .quizId(quiz.getId())
                .option1(pair.getFirst())
                .option2(pair.getSecond())
                .errors(0)
                .hits(0)
                .hitPercent(0)
                .build();
    }

    public QuizSender next(Long quizId, QuizReceiver quizReceiver) {
        var quiz = findById(quizId);
        if (!quiz.contains(quizReceiver.hint())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid choice");
        }
        if (quiz.isFinalized()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz already close");
        }
        var currentRound = quiz.findLastRound()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Last round not found"));
        var title1 = omdbApiClient.findTitleById(currentRound.getTitle1()).getBody();
        var title2 = omdbApiClient.findTitleById(currentRound.getTitle2()).getBody();
        var higherScore = higherScore(List.of(title1, title2));
        currentRound.setHint(quizReceiver.hint());
        currentRound.setRightAnswer(higherScore.imdbID());
        if (Objects.equals(quizReceiver.hint(), higherScore.imdbID())) {
            quiz.setHits(quiz.getHits() + 1);
        } else {
            quiz.setErrors(quiz.getErrors() + 1);
        }
        quiz.setHitPercent(quiz.calculateHitPercent());
        if (quiz.getErrors() == 3) {
            quiz.setFinalized(true);
            quizRepository.save(quiz);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game over");
        }
        var pair = randomPair();
        var newRound = QuizRound.builder()
                .title1(pair.getFirst().imdbID())
                .title2(pair.getSecond().imdbID())
                .quiz(quiz)
                .build();
        quiz.getRounds().add(newRound);
        quizRepository.save(quiz);
        return QuizSender.builder()
                .quizId(quiz.getId())
                .option1(pair.getFirst())
                .option2(pair.getSecond())
                .errors(quiz.getErrors())
                .hits(quiz.getHits())
                .hitPercent(quiz.getHitPercent())
                .build();
    }

    public void close(Long id) {
        var quiz = findById(id);
        if (quiz.isFinalized()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quiz already close");
        }
        quiz.setFinalized(true);
        quizRepository.save(quiz);
    }

    public List<RankingView> getRanking() {
        return quizRepository.getRanking();
    }

    public Quiz findById(Long id) {
        var user = userService.me();
        return quizRepository.findByIdAndOwner_Id(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found"));
    }

    private TitleResponse higherScore(List<TitleResponse> titles) {
        return titles.stream()
                .sorted((o1, o2) -> o2.score().compareTo(o1.score()))
                .findFirst()
                .orElseThrow();
    }

    private Pair<TitleResponse, TitleResponse> randomPair() {
        var random = new Random();
        Supplier<Integer> getIndex = () -> random.nextInt(TITLES.size() - 1);
        int random1 = getIndex.get();
        int random2 = getIndex.get();
        while (random2 == random1) {
            random2 = getIndex.get();
        }
        return Pair.of(TITLES.get(random1), TITLES.get(random2));
    }
}
