package ru.kpfu.itis.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
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
import java.util.UUID;

import static ru.kpfu.itis.security.JwtUtil.ACCESS_TOKEN_NAME;
import static ru.kpfu.itis.security.JwtUtil.REFRESH_TOKEN_NAME;

public class JwtUsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final AntPathRequestMatcher requestMatcher;
    private final String usernameParameter;
    private final String passwordParameter;
    private final String unsuccessfulAuthenticationUrl;

    public JwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                   RefreshTokenService refreshTokenService,
                                                   JwtUtil jwtUtil,
                                                   AntPathRequestMatcher requestMatcher,
                                                   String usernameParameter,
                                                   String passwordParameter,
                                                   String unsuccessfulAuthenticationUrl) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtil = jwtUtil;
        this.requestMatcher = requestMatcher;
        this.usernameParameter = usernameParameter;
        this.passwordParameter = passwordParameter;
        this.unsuccessfulAuthenticationUrl = unsuccessfulAuthenticationUrl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            try {
                Authentication authentication = attemptAuthentication(request);
                successfulAuthentication(response, authentication);
                filterChain.doFilter(request, response);
            } catch (UsernameNotFoundException | AuthenticationServiceException e) {
                unsuccessfulAuthentication(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
        String email = request.getParameter(usernameParameter);
        String password = request.getParameter(passwordParameter);
        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(email, password);
        return authenticationManager.authenticate(token);
    }

    protected void successfulAuthentication(HttpServletResponse response,
                                            Authentication authentication) {

        User user = ((UserDetailsImpl) authentication.getDetails()).getUser();

        refreshTokenService.deleteByUserIdEfExists(user.getId()); //delete old token if exists in database
        RefreshToken refreshToken = jwtUtil.createRefreshToken(user);
        refreshTokenService.create(refreshToken);

        String csrf = UUID.randomUUID().toString();
        String jwt = jwtUtil.createJwt(user, csrf);

        Cookie accessTokenCookie = new Cookie(ACCESS_TOKEN_NAME, jwt);
        accessTokenCookie.setMaxAge(2_592_000); // one month
        accessTokenCookie.setPath("/");

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_NAME, refreshToken.getToken().toString());
        refreshTokenCookie.setMaxAge(2_592_000);
        refreshTokenCookie.setPath("/");

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        AccessToken accessToken = jwtUtil.createAccessToken(user, csrf);

        JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, true);
        SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response) throws IOException, ServletException {

        request.getRequestDispatcher(unsuccessfulAuthenticationUrl).forward(request, response);
    }
}
