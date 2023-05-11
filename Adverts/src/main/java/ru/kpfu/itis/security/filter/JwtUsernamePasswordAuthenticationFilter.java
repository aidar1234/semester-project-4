package ru.kpfu.itis.security.filter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.model.RefreshToken;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.security.AccessToken;
import ru.kpfu.itis.security.JwtUtil;
import ru.kpfu.itis.security.authentication.JwtAuthentication;
import ru.kpfu.itis.security.details.UserDetailsImpl;
import ru.kpfu.itis.service.RefreshTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.kpfu.itis.security.JwtUtil.ACCESS_TOKEN_NAME;
import static ru.kpfu.itis.security.JwtUtil.REFRESH_TOKEN_NAME;

@Component
public class JwtUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String USERNAME_PARAMETER = "email";

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    public JwtUsernamePasswordAuthenticationFilter(@Qualifier("usernamePasswordAuthenticationManager") AuthenticationManager authenticationManager,
                                                   PasswordEncoder passwordEncoder,
                                                   RefreshTokenService refreshTokenService,
                                                   JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.setAuthenticationManager(authenticationManager);
        this.setUsernameParameter(USERNAME_PARAMETER);
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/sign_in", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        String email = request.getParameter("email");
        String hashPassword = passwordEncoder.encode(request.getParameter("password"));
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(email, hashPassword);
        return authenticationManager.authenticate(token); //TODO: try catch?
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {

        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();

        RefreshToken refreshToken = jwtUtil.createRefreshToken(user);
        refreshTokenService.create(refreshToken);
        String jwt = jwtUtil.createJwt(user);

        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_NAME, jwt);
        accessTokenCookie.setHttpOnly(true);

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_NAME, refreshToken.getToken().toString());
        refreshTokenCookie.setHttpOnly(true);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        AccessToken accessToken = jwtUtil.createAccessToken(user);

        JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, true);
        SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);

        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        request.getRequestDispatcher("/sign_in/error").forward(request, response);
    }
}
