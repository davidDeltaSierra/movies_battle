package com.ada.tech.movies_battle.quiz;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/quiz")
class QuizController {
    private final QuizService quizService;

    @Operation(summary = "Start new Quiz")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizSender> start() {
        return ResponseEntity.ok(quizService.start());
    }

    @Operation(summary = "Show game ranking")
    @GetMapping(value = "ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RankingView>> ranking() {
        return ResponseEntity.ok(quizService.getRanking());
    }

    @Operation(summary = "Advance quiz")
    @PostMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizSender> next(@PathVariable Long id,
                                           @Valid @RequestBody QuizReceiver quizReceiver) {
        return ResponseEntity.ok(quizService.next(id, quizReceiver));
    }

    @Operation(summary = "Close Quiz")
    @PostMapping(value = "{id}/close", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> close(@PathVariable Long id) {
        quizService.close(id);
        return ResponseEntity.noContent().build();
    }
}
