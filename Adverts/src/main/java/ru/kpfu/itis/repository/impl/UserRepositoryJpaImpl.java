package ru.kpfu.itis.repository.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.repository.UserRepository;

import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryJpaImpl implements UserRepository {

    @PersistenceContext
    private Session entityManager;

    @Override
    public UUID save(User user) {
        return (UUID) entityManager.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> typedQuery = entityManager.createQuery("SELECT user FROM User user WHERE user.email = :email", User.class);
        typedQuery.setParameter("email", email);
        return Optional.of(typedQuery.getSingleResult());
    }
}
