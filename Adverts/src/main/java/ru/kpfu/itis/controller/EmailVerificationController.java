package ru.kpfu.itis.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.security.JwtUtil;
import ru.kpfu.itis.service.UserService;

@Log
@Controller
@RequestMapping("/verify")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/{token}")
    public String verify(@PathVariable("token") String jwt) {
        try {
            DecodedJWT decodedJWT = jwtUtil.verify(jwt);
            userService.verifyEmail(decodedJWT.getClaim("email").asString());
            return "sign_in";
        } catch (JWTVerificationException | UserNotFoundException e) {
            return "unsuccessful_email_verifying";
        }
    }
}
