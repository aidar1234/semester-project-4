package ru.kpfu.itis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.model.RefreshToken;
import ru.kpfu.itis.model.Role;
import ru.kpfu.itis.model.User;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
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

    public String createJwt(User user) {
        return JWT.create()
                .withExpiresAt(Instant.from(LocalDateTime.now().plusMinutes(EXPIRE_MINUTES)))
                .withClaim("email", user.getEmail())
                .withClaim("firstName", user.getFirstName())
                .withClaim("lastName", user.getLastName())
                .withClaim("role", user.getRole().toString())
                .sign(algorithm);
    }

    public AccessToken createAccessToken(User user) {
        return AccessToken.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public AccessToken createAccessToken(DecodedJWT decodedJWT) {
        return AccessToken.builder()
                .email(decodedJWT.getClaim("email").asString())
                .firstName(decodedJWT.getClaim("firstName").asString())
                .lastName(decodedJWT.getClaim("lastName").asString())
                .role(Role.valueOf(decodedJWT.getClaim("role").asString()))
                .build();
    }

    public RefreshToken createRefreshToken(User user) {
        return RefreshToken.builder()
                .token(UUID.randomUUID())
                .user(user)
                .expire(LocalDateTime.now().plusDays(EXPIRE_DAYS))
                .build();
    }

    public DecodedJWT verify(String jwt) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(jwt);
    }
}
