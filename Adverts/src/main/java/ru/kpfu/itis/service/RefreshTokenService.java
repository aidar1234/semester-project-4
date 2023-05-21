package ru.kpfu.itis.service;

import ru.kpfu.itis.model.RefreshToken;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    Long create(RefreshToken refreshToken);

    Optional<RefreshToken> findByTokenName(UUID name);

    void update(RefreshToken refreshToken);

    void deleteByUserIdEfExists(UUID id);

    void deleteByTokenName(UUID token);
}
