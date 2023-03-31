package com.ada.tech.movies_battle.quiz;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/quiz")
class QuizController {
    private final QuizService quizService;

    @Operation(summary = "Save new quiz")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizSender> start() {
        return ResponseEntity.ok(
                quizService.start()
        );
    }

    @Operation(summary = "Advance quiz")
    @PostMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizSender> next(@PathVariable Long id,
                                           @Valid @RequestBody QuizReceiver quizReceiver) {
        return ResponseEntity.ok(
                quizService.next(id, quizReceiver)
        );
    }
}
