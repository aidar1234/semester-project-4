package ru.kpfu.itis.repository.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.model.RefreshToken;
import ru.kpfu.itis.repository.RefreshTokenRepository;

import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.UUID;

@Repository
public class RefreshTokenRepositoryJpaImpl implements RefreshTokenRepository {

    @PersistenceContext
    private Session entityManager;

    @Transactional
    @Override
    public UUID save(RefreshToken refreshToken) {
        return (UUID) entityManager.save(refreshToken);
    }

    @Transactional
    @Override
    public void deleteByToken(UUID token) {
        entityManager.remove(findByToken(token));
    }

    @Override
    public RefreshToken findByToken(UUID token) {
        TypedQuery<RefreshToken> typedQuery = entityManager.createQuery(
                "SELECT refreshToken FROM RefreshToken refreshToken where refreshToken.token = :token", RefreshToken.class);
        typedQuery.setParameter("token", token);
        return typedQuery.getSingleResult();
    }
}
