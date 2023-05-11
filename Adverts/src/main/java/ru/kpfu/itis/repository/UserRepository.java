package ru.kpfu.itis.repository;

import ru.kpfu.itis.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UUID save(User user);

    Optional<User> findByEmail(String email);
}
