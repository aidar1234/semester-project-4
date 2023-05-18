package ru.kpfu.itis.repository.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.model.RefreshToken;
import ru.kpfu.itis.repository.RefreshTokenRepository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RefreshTokenRepositoryJpaImpl implements RefreshTokenRepository {

    @PersistenceContext
    private Session entityManager;

    @Transactional
    @Override
    public Long save(RefreshToken refreshToken) {
        return (Long) entityManager.save(refreshToken);
    }

    @Transactional
    @Override
    public void deleteByTokenName(UUID token) {
        Optional<RefreshToken> optionalToken = findByTokenName(token);
        if (optionalToken.isPresent()) {
            entityManager.remove(findByTokenName(token));
        }
    }

    @Transactional
    @Override
    public void update(RefreshToken token) {
        entityManager.update(token);
    }

    @Override
    public Optional<RefreshToken> findByTokenName(UUID name) {
        TypedQuery<RefreshToken> typedQuery = entityManager.createQuery(
                "SELECT refreshToken FROM RefreshToken refreshToken where refreshToken.token = :token", RefreshToken.class);
        typedQuery.setParameter("token", name);
        try {
            RefreshToken refreshToken = typedQuery.getSingleResult();
            return Optional.of(refreshToken);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
