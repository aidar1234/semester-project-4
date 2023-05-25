package ru.kpfu.itis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.model.RefreshToken;
import ru.kpfu.itis.model.enums.Role;
import ru.kpfu.itis.model.enums.State;
import ru.kpfu.itis.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static ru.kpfu.itis.model.RefreshToken.EXPIRE_DAYS;
import static ru.kpfu.itis.security.AccessToken.EXPIRE_MINUTES;

@Component
public class JwtUtil {

    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";

    private final Algorithm algorithm;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createEmailVerificationJwt(String email, int expireDays) {
        return JWT.create()
                .withExpiresAt(LocalDateTime
                        .now()
                        .plusDays(expireDays)
                        .toInstant(ZonedDateTime.now(ZoneId.systemDefault()).getOffset()))
                .withClaim("email", email)
                .sign(algorithm);
    }

    public String createJwt(User user, String csrf) {
        return JWT.create()
                .withExpiresAt(LocalDateTime
                        .now()
                        .plusMinutes(EXPIRE_MINUTES)
                        .toInstant(ZonedDateTime.now(ZoneId.systemDefault()).getOffset()))
                .withClaim("id", user.getId().toString())
                .withClaim("email", user.getEmail())
                .withClaim("firstName", user.getFirstName())
                .withClaim("lastName", user.getLastName())
                .withClaim("role", user.getRole().toString())
                .withClaim("state", user.getState().toString())
                .withClaim("csrf", csrf)
                .sign(algorithm);
    }

    public AccessToken createAccessToken(User user, String csrf) {
        return AccessToken.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .state(user.getState())
                .csrf(csrf)
                .expire(LocalDateTime.now().plusDays(EXPIRE_MINUTES))
                .build();
    }

    public AccessToken getAccessToken(DecodedJWT decodedJWT) {
        return AccessToken.builder()
                .id(UUID.fromString(decodedJWT.getClaim("id").asString()))
                .email(decodedJWT.getClaim("email").asString())
                .role(Role.valueOf(decodedJWT.getClaim("role").asString()))
                .state(State.valueOf(decodedJWT.getClaim("state").asString()))
                .expire(LocalDateTime.ofInstant(decodedJWT.getExpiresAtAsInstant(),
                        ZonedDateTime.now(ZoneId.systemDefault()).getOffset()))
                .csrf(decodedJWT.getClaim("csrf").asString())
                .build();
    }

    public RefreshToken createRefreshToken(User user) {
        return RefreshToken.builder()
                .token(UUID.randomUUID())
                .expire(LocalDateTime.now().plusDays(EXPIRE_DAYS))
                .user(user)
                .build();
    }

    public DecodedJWT verify(String jwt) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(jwt);
    }

    public DecodedJWT decode(String jwt) {
        return JWT.decode(jwt);
    }
}
