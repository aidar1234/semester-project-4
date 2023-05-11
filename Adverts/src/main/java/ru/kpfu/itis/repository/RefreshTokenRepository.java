package ru.kpfu.itis.repository;

import ru.kpfu.itis.model.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepository {

    UUID save(RefreshToken refreshToken);

    void deleteByToken(UUID token);

    RefreshToken findByToken(UUID token);
}
