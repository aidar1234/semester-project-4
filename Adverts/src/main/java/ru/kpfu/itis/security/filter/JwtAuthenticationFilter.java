package ru.kpfu.itis.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kpfu.itis.model.RefreshToken;
import ru.kpfu.itis.security.AccessToken;
import ru.kpfu.itis.security.JwtUtil;
import ru.kpfu.itis.security.authentication.JwtAuthentication;
import ru.kpfu.itis.service.RefreshTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static ru.kpfu.itis.model.RefreshToken.EXPIRE_DAYS;
import static ru.kpfu.itis.security.JwtUtil.ACCESS_TOKEN_NAME;
import static ru.kpfu.itis.security.JwtUtil.REFRESH_TOKEN_NAME;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final String[] authenticationUrlPrefixes;
    private final RefreshTokenService refreshTokenService;
    private final String signInUrl;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   RefreshTokenService refreshTokenService,
                                   String signInUrl,
                                   String... authenticationUrlPrefixes) {
        this.jwtUtil = jwtUtil;
        this.authenticationUrlPrefixes = authenticationUrlPrefixes;
        this.refreshTokenService = refreshTokenService;
        this.signInUrl = signInUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // if before this filter usernamePasswordFilter did authentication by username and password
        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        // if request url is authentication url
        for (String url : authenticationUrlPrefixes) {
            if (request.getServletPath().startsWith(url)) {

                if (request.getCookies() == null) {
                    response.sendRedirect(signInUrl);
                    return;
                }

                // get json web token as string
                Optional<String> optionalJwt = Arrays.stream(request.getCookies())
                        .filter(cookie -> cookie.getName().equals(ACCESS_TOKEN_NAME))
                        .map(Cookie::getValue)
                        .findFirst();

                if (optionalJwt.isEmpty()) {
                    response.sendRedirect(signInUrl);
                    return;
                }

                //jwt verifier throws JWTVerificationException
                try {
                    DecodedJWT decodedJWT = jwtUtil.verify(optionalJwt.get());
                    AccessToken accessToken = jwtUtil.getAccessToken(decodedJWT); // it`s json web token as java object

                    JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, true);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
                } catch (TokenExpiredException e) {
                    Optional<String> optionalTokenString = Arrays.stream(request.getCookies())
                            .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_NAME))
                            .map(Cookie::getValue)
                            .findFirst();

                    if (optionalTokenString.isEmpty()) {
                        response.sendRedirect(signInUrl);
                        return;
                    }

                    // get refresh token as string from repo
                    Optional<RefreshToken> optionalToken = refreshTokenService.findByTokenName(
                            UUID.fromString(optionalTokenString.get())
                    );
                    if (optionalToken.isEmpty()) {
                        response.sendRedirect("/logout");
                        return;
                    }

                    RefreshToken refreshToken = optionalToken.get();

                    LocalDateTime expire = refreshToken.getExpire();
                    if (LocalDateTime.now().isAfter(expire)) {
                        response.sendRedirect(signInUrl);
                        return;
                    }

                    refreshToken.setToken(UUID.randomUUID());
                    refreshToken.setExpire(LocalDateTime.now().plusDays(EXPIRE_DAYS));
                    refreshTokenService.update(refreshToken);

                    String csrf = UUID.randomUUID().toString();
                    AccessToken accessToken = jwtUtil.createAccessToken(refreshToken.getUser(), csrf);

                    String jwt = jwtUtil.createJwt(refreshToken.getUser(), csrf);

                    Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_NAME, jwt);
                    accessTokenCookie.setMaxAge(2_592_000);
                    accessTokenCookie.setPath("/");

                    Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_NAME, refreshToken.getToken().toString());
                    refreshTokenCookie.setMaxAge(2_592_000);
                    refreshTokenCookie.setPath("/");

                    response.addCookie(accessTokenCookie);
                    response.addCookie(refreshTokenCookie);

                    JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, true);
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
                } catch (JWTVerificationException e) {
                    response.sendRedirect(signInUrl);
                    return;
                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
