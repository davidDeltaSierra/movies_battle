package com.ada.tech.movies_battle.user;

import com.ada.tech.movies_battle.common.JwtPayload;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/v1/user")
class UserController {
    private final UserService userService;

    @Operation(summary = "Login")
    @PostMapping(value = "auth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtPayload> auth(@Valid @RequestBody LoginRequest body) {
        return userService.auth(body);
    }
}
