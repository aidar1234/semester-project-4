package ru.kpfu.itis.security.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import ru.kpfu.itis.service.RefreshTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static ru.kpfu.itis.security.JwtUtil.ACCESS_TOKEN_NAME;
import static ru.kpfu.itis.security.JwtUtil.REFRESH_TOKEN_NAME;

public class JwtLogoutFilter extends OncePerRequestFilter {

    private final String logoutUrl;
    private final RefreshTokenService refreshTokenService;

    public JwtLogoutFilter(String logoutUrl, RefreshTokenService refreshTokenService) {
        this.logoutUrl = logoutUrl;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        if (request.getServletPath().equals(logoutUrl)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null)
                for (Cookie cookie : cookies) {
                    String cookieName = cookie.getName();
                    if (REFRESH_TOKEN_NAME.equals(cookieName)) {
                        refreshTokenService.deleteByTokenName(UUID.fromString(cookie.getValue()));
                        cookie.setMaxAge(0);
                    }
                    if (ACCESS_TOKEN_NAME.equals(cookieName)) {
                        cookie.setMaxAge(0);
                    }
                }
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("/home");
        }
        filterChain.doFilter(request, response);
    }
}
