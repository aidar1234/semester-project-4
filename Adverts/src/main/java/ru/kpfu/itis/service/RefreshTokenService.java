package ru.kpfu.itis.service;

import ru.kpfu.itis.model.RefreshToken;

import java.util.UUID;

public interface RefreshTokenService {

    UUID create(RefreshToken refreshToken);

    void deleteByToken(UUID token);
}
