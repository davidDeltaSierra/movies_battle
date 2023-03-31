package com.ada.tech.movies_battle.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

@Component
public class JwtHandler {
    private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    @Value("${jwt.secret}")
    private String secret;

    public JwtPayload decode(String token) {
        var bearer = token.replace(AUTHORIZATION_TOKEN_PREFIX, "").trim();
        var decodedJWT = JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(bearer);
        return new JwtPayload(decodedJWT.getClaim("username").asString());
    }

    public String encode(String username) {
        return JWT.create()
                .withExpiresAt(Date.from(
                        LocalDateTime.now()
                                .plusHours(1)
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                ))
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(secret));
    }
}
