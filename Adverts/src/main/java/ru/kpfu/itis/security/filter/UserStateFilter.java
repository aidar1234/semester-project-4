package ru.kpfu.itis.security.filter;

import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kpfu.itis.exception.UserNotFoundException;
import ru.kpfu.itis.model.State;
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
import java.util.logging.Level;

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
                if (request.getServletPath().startsWith(url)) {
                    Optional<User> optionalUser = userService.findByEmail(email);
                    if (optionalUser.isPresent()) {
                        if (optionalUser.get().getState().equals(State.BANNED)) {
                            response.sendRedirect("/banned");
                            deleteCookies(request);
                            return;
                        }
                        if (optionalUser.get().getState().equals(State.DELETED)) {
                            response.sendRedirect("/deleted");
                            deleteCookies(request);
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
                response.sendRedirect("/banned");
                deleteCookies(request);
                return;
            }

            if (user.getState().equals(State.DELETED)) {
                response.sendRedirect("/deleted");
                deleteCookies(request);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void deleteCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Arrays.stream(cookies).forEach(cookie -> cookie.setMaxAge(0));
        }
    }
}
