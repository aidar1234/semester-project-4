package ru.kpfu.itis.repository;

import ru.kpfu.itis.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    Long save(RefreshToken refreshToken);

    void deleteByTokenName(UUID token);

    void update(RefreshToken token);

    Optional<RefreshToken> findByUserId(UUID id);

    Optional<RefreshToken> findByTokenName(UUID name);
}
