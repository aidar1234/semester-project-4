package ru.kpfu.itis.service;

import ru.kpfu.itis.dto.request.UserSignUpRequest;

import java.util.UUID;

public interface UserService {

    UUID create(UserSignUpRequest request);
}
