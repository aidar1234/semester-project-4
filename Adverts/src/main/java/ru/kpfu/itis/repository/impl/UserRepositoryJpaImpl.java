package ru.kpfu.itis.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.model.enums.State;
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

    @Transactional
    @Override
    public Optional<User> findById(UUID id) {
        try {
            User user = entityManager.get(User.class, id);
            return Optional.of(user);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public void update(User user) {
        entityManager.update(user);
        entityManager.flush();
    }

    @Transactional
    @Override
    public void delete(User user) {
        entityManager.delete(user);
        entityManager.flush();
    }

    @Transactional
    @Override
    public void banByEmail(String email) throws UserNotFoundException {
        Optional<User> optionalUser = findByEmail(email);
        if (optionalUser.isPresent()) {
            Query query = entityManager.createQuery(
                    "UPDATE User user SET user.state = 'BANNED' WHERE user.id = (SELECT id FROM user WHERE email = :email)"
            );
            query.setParameter("email", email);
            query.executeUpdate();
        } else {
            throw new UserNotFoundException();
        }
    }

    @Transactional
    @Override
    public void deleteByEmail(String email) throws UserNotFoundException {
        Optional<User> optionalUser = findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setState(State.DELETED);
            entityManager.update(user);
            entityManager.flush();
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public boolean isBanned(String email) throws UserNotFoundException {
        Optional<User> optionalUser = findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getState() == State.BANNED;
        }
        throw new UserNotFoundException();
    }

    @Override
    public boolean isDeleted(String email) throws UserNotFoundException {
        Optional<User> optionalUser = findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getState() == State.DELETED;
        }
        throw new UserNotFoundException();
    }
}
