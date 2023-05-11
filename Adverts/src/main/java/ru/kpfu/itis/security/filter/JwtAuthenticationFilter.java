package ru.kpfu.itis.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kpfu.itis.config.SecurityConfig;
import ru.kpfu.itis.security.AccessToken;
import ru.kpfu.itis.security.JwtUtil;
import ru.kpfu.itis.security.authentication.JwtAuthentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static ru.kpfu.itis.config.SecurityConfig.STATIC_RESOURCE_PREFIX_URL;
import static ru.kpfu.itis.security.JwtUtil.ACCESS_TOKEN_NAME;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().startsWith(STATIC_RESOURCE_PREFIX_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String[] urls = SecurityConfig.authenticateUrls;
        for (String url : urls) {
            if (request.getServletPath().equals(url)) {
                Optional<String> jwt = Arrays.stream(request.getCookies())
                        .filter((cookie) -> cookie.getName().equals(ACCESS_TOKEN_NAME))
                        .map(Cookie::getValue)
                        .findFirst();

                if (jwt.isEmpty()) {
                    response.sendRedirect("/sign_in");
                    return;
                }
                try {
                    DecodedJWT decodedJWT = jwtUtil.verify(jwt.get());
                    AccessToken accessToken = jwtUtil.createAccessToken(decodedJWT);
                    JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, true);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
                } catch (JWTVerificationException e) {
                    //TODO: create new token by refresh token, if refresh token not exist - redirect to sign_in
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
