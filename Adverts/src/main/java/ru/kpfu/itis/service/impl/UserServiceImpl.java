package ru.kpfu.itis.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.dto.request.PasswordChangeRequest;
import ru.kpfu.itis.dto.request.UserEditRequest;
import ru.kpfu.itis.dto.request.UserSignUpRequest;
import ru.kpfu.itis.dto.response.UserResponse;
import ru.kpfu.itis.exception.AdvertAlreadyFavoriteException;
import ru.kpfu.itis.exception.AdvertNotFoundException;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.ElectronicsAdvert;
import ru.kpfu.itis.model.TransportAdvert;
import ru.kpfu.itis.model.enums.Role;
import ru.kpfu.itis.model.enums.State;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.repository.UserRepository;
import ru.kpfu.itis.security.JwtUtil;
import ru.kpfu.itis.service.ElectronicsAdvertService;
import ru.kpfu.itis.service.TransportAdvertService;
import ru.kpfu.itis.service.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

@Component
@RequiredArgsConstructor
@Log
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ElectronicsAdvertService electronicsAdvertService;
    private final TransportAdvertService transportAdvertService;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil;

    @Override
    public UUID create(UserSignUpRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .locality(request.getLocality())
                .state(State.NOT_CONFIRMED)
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    @ru.kpfu.itis.annotation.Log
    @Override
    public void signUp(UserSignUpRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .locality(request.getLocality())
                .state(State.NOT_CONFIRMED)
                .role(Role.USER)
                .build();

        userRepository.save(user);
        try {
            sendVerificationEmail(user);
        } catch (Exception e) {
            log.log(Level.INFO, "was thrown " + e.getClass());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserResponse> getUserResponseById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return Optional.of(UserResponse.builder()
                    .fileName(user.getFile() != null ? user.getFile().getName() : null)
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .locality(user.getLocality())
                    .phone(user.getPhone())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void updateById(UserEditRequest request, UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!request.getEmail().equals("")) {
                user.setEmail(request.getEmail());
            }
            if (!request.getFirstName().equals("")) {
                user.setFirstName(request.getFirstName());
            }
            if (!request.getLastName().equals("")) {
                user.setLastName(request.getLastName());
            }
            if (!request.getLocality().equals("")) {
                user.setLocality(request.getLocality());
            }
            if (!request.getPhone().equals("")) {
                user.setPhone(request.getPhone());
            }
            userRepository.update(user);
        }
    }

    @Override
    public void updateById(PasswordChangeRequest request, UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setHashPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.update(user);
        }
    }

    public void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String addressTo = user.getEmail();
        String addressFrom = environment.getRequiredProperty("spring.mail.username");
        String sender = "Adverts";
        String subject = "Пожалуйста, подтвердите электронную почту";


        //language=html
        String content = "[[name]],<br>"
                + "Пожалуйста, нажмите на ссылку для подтверждения почты:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_blank\">ПОДТВЕРДИТЬ</a></h3>"
                + "Спасибо за регистрацию,<br>"
                + "Adverts.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom(addressFrom, sender);
        messageHelper.setTo(addressTo);
        messageHelper.setSubject(subject);

        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());

        String jwt = jwtUtil.createEmailVerificationJwt(user.getEmail(), 30);
        String verifyURL = "http://localhost:8080" + "/verify/" + jwt;

        content = content.replace("[[URL]]", verifyURL);

        messageHelper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public void verifyEmail(String email) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setState(State.CONFIRMED);
            userRepository.update(user);
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void banByEmail(String email) throws UserNotFoundException {
        userRepository.banByEmail(email);
    }

    @Override
    public void deleteByEmail(String email) throws UserNotFoundException {
        userRepository.deleteByEmail(email);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    @Override
    public void removeFavoriteTransportAdvertByUserIdAndAdvertId(UUID userId, UUID advertId) throws UserNotFoundException, AdvertNotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<TransportAdvert> optionalAdvert = transportAdvertService.findById(advertId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (optionalAdvert.isEmpty()) {
            throw new AdvertNotFoundException();
        }
        User user = optionalUser.get();
        TransportAdvert advert = optionalAdvert.get();
        user.getFavoriteTransportAdverts().remove(advert);
        userRepository.update(user);
    }

    @Override
    public void addFavoriteTransportAdvertByUserIdAndAdvertId(UUID userId, UUID advertId) throws UserNotFoundException, AdvertNotFoundException, AdvertAlreadyFavoriteException {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<TransportAdvert> optionalAdvert = transportAdvertService.findById(advertId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (optionalAdvert.isEmpty()) {
            throw new AdvertNotFoundException();
        }
        User user = optionalUser.get();
        TransportAdvert advert = optionalAdvert.get();
        if (user.getFavoriteTransportAdverts().contains(advert)) {
            throw new AdvertAlreadyFavoriteException();
        }
        user.getFavoriteTransportAdverts().add(advert);
        userRepository.update(user);
    }

    @Override
    public void removeFavoriteElectronicsAdvertByUserIdAndAdvertId(UUID userId, UUID advertId) throws UserNotFoundException, AdvertNotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<ElectronicsAdvert> optionalAdvert = electronicsAdvertService.findById(advertId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (optionalAdvert.isEmpty()) {
            throw new AdvertNotFoundException();
        }
        User user = optionalUser.get();
        ElectronicsAdvert advert = optionalAdvert.get();
        user.getFavoriteElectronicsAdverts().remove(advert);
        userRepository.update(user);
    }

    @Override
    public void addFavoriteElectronicsAdvertByUserIdAndAdvertId(UUID userId, UUID advertId) throws UserNotFoundException, AdvertNotFoundException, AdvertAlreadyFavoriteException {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<ElectronicsAdvert> optionalAdvert = electronicsAdvertService.findById(advertId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        if (optionalAdvert.isEmpty()) {
            throw new AdvertNotFoundException();
        }
        User user = optionalUser.get();
        ElectronicsAdvert advert = optionalAdvert.get();
        if (user.getFavoriteElectronicsAdverts().contains(advert)) {
            throw new AdvertAlreadyFavoriteException();
        }
        user.getFavoriteElectronicsAdverts().add(advert);
        userRepository.update(user);
    }
}
