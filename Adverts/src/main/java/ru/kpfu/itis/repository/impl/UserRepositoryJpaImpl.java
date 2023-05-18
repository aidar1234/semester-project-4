package ru.kpfu.itis.repository.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.repository.UserRepository;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryJpaImpl implements UserRepository {

    @PersistenceContext
    private Session entityManager;

    @Transactional
    @Override
    public UUID save(User user) {
        UUID id = (UUID) entityManager.save(user);
        entityManager.flush();
        return id;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> typedQuery = entityManager.createQuery("SELECT user FROM User user WHERE user.email = :email", User.class);
        typedQuery.setParameter("email", email);
        try {
            User user = typedQuery.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
