package ru.kpfu.itis.security.filter;

import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kpfu.itis.model.enums.State;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.security.details.UserDetailsImpl;
import ru.kpfu.itis.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Log
public class UserStateFilter extends OncePerRequestFilter {

    private final String[] dangerUrlPrefixes;
    private final UserService userService;

    public UserStateFilter(UserService userService, String[] dangerUrlPrefixes) {
        this.dangerUrlPrefixes = dangerUrlPrefixes;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            User user = ((UserDetailsImpl) authentication.getDetails()).getUser();
            String email = user.getEmail();

            for (String url : dangerUrlPrefixes) {
                if (request.getServletPath().startsWith(url) && "POST".equals(request.getMethod())) {
                    Optional<User> optionalUser = userService.findByEmail(email);
                    if (optionalUser.isPresent()) {
                        if (optionalUser.get().getState().equals(State.BANNED)) {
                            deleteCookies(request, response);
                            response.sendRedirect("/banned");
                            return;
                        }
                        if (optionalUser.get().getState().equals(State.DELETED)) {
                            deleteCookies(request, response);
                            response.sendRedirect("/deleted");
                            return;
                        }
                    }

                }
            }

            if (user.getState().equals(State.NOT_CONFIRMED)) {
                response.sendRedirect("/not_confirmed");
                return;
            }

            if (user.getState().equals(State.BANNED)) {
                deleteCookies(request, response);
                response.sendRedirect("/banned");
                return;
            }

            if (user.getState().equals(State.DELETED)) {
                deleteCookies(request, response);
                response.sendRedirect("/deleted");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies).forEach(cookie -> {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            });
        }
    }
}
