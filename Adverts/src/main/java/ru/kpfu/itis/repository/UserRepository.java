package ru.kpfu.itis.repository;

import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UUID save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    void update(User user);

    void banByEmail(String email) throws UserNotFoundException;

    void deleteByEmail(String email) throws UserNotFoundException;

    boolean isBanned(String email) throws UserNotFoundException;

    boolean isDeleted(String email) throws UserNotFoundException;
}
