package ru.kpfu.itis.service;

import ru.kpfu.itis.dto.request.PasswordChangeRequest;
import ru.kpfu.itis.dto.request.UserEditRequest;
import ru.kpfu.itis.dto.request.UserSignUpRequest;
import ru.kpfu.itis.dto.response.UserResponse;
import ru.kpfu.itis.exception.AdvertAlreadyFavoriteException;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UUID create(UserSignUpRequest request);

    void signUp(UserSignUpRequest request);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    Optional<UserResponse> getUserResponseById(UUID id);

    void updateById(UserEditRequest request, UUID id);

    void updateById(PasswordChangeRequest request, UUID id);

    void sendVerificationEmail(User user) throws Exception;

    void verifyEmail(String email) throws UserNotFoundException;

    void banByEmail(String email) throws UserNotFoundException;

    void deleteByEmail(String email) throws UserNotFoundException;

    void update(User user);

    void removeFavoriteTransportAdvertByUserIdAndAdvertId(UUID userId, UUID advertId) throws UserNotFoundException, AdvertNotFoundException;

    void addFavoriteTransportAdvertByUserIdAndAdvertId(UUID userId, UUID advertId) throws UserNotFoundException, AdvertNotFoundException, AdvertAlreadyFavoriteException;

    void removeFavoriteElectronicsAdvertByUserIdAndAdvertId(UUID userId, UUID advertId) throws UserNotFoundException, AdvertNotFoundException;

    void addFavoriteElectronicsAdvertByUserIdAndAdvertId(UUID userId, UUID advertId) throws UserNotFoundException, AdvertNotFoundException, AdvertAlreadyFavoriteException;
}
