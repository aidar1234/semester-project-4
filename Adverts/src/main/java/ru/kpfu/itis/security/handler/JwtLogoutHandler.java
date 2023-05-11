package ru.kpfu.itis.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.service.RefreshTokenService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static ru.kpfu.itis.security.JwtUtil.*;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final RefreshTokenService refreshTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            String cookieName = cookie.getName();
            if (REFRESH_TOKEN_NAME.equals(cookieName)) {
                refreshTokenService.deleteByToken(UUID.fromString(cookie.getValue()));
                cookie.setMaxAge(0);
            }
            if (ACCESS_TOKEN_NAME.equals(cookieName)) {
                cookie.setMaxAge(0);
            }
        }
    }
}
