package com.ada.tech.movies_battle.user;

import com.ada.tech.movies_battle.common.JwtHandler;
import com.ada.tech.movies_battle.common.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements CommandLineRunner {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtHandler jwtHandler;
    private final UserRepository userRepository;

    public User me() {
        var principal = (JwtPayload) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findByUsername(principal.username());
    }

    public ResponseEntity<JwtPayload> auth(LoginRequest body) {
        var user = findByUsername(body.username());
        if (!bCryptPasswordEncoder.matches(body.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtHandler.encode(body.username()))
                .body(new JwtPayload(body.username()));
    }

    private User findByUsername(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    /**
     * Carrega usuários durante o start
     */
    @Override
    public void run(String... args) {
        userRepository.saveAll(List.of(
                new User("izuku_midoriya", bCryptPasswordEncoder.encode("izuku_midoriya")),
                new User("katsuki_bakugo", bCryptPasswordEncoder.encode("katsuki_bakugo")),
                new User("shoto_todoroki", bCryptPasswordEncoder.encode("shoto_todoroki"))
        ));
    }
}
