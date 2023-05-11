package ru.kpfu.itis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.model.RefreshToken;
import ru.kpfu.itis.repository.RefreshTokenRepository;
import ru.kpfu.itis.service.RefreshTokenService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UUID create(RefreshToken refreshToken) {
        refreshToken.setId(null);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteByToken(UUID token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
