package ru.kpfu.itis.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kpfu.itis.security.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.kpfu.itis.security.JwtUtil.ACCESS_TOKEN_NAME;

@RequiredArgsConstructor
public class JwtCsrfFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final String[] urls;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        for (String url : urls) {
            if (request.getServletPath().equals(url) && request.getMethod().equals("POST")) {
                Cookie[] cookies = request.getCookies();
                if (cookies == null) {
                    response.sendRedirect("/bad_csrf");
                    return;
                }
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(ACCESS_TOKEN_NAME)) {
                        DecodedJWT decodedJWT = jwtUtil.decode(cookie.getValue());
                        String realCsrf = decodedJWT.getClaim("csrf").asString();
                        String csrf = request.getParameter("_csrf");
                        if (!realCsrf.equals(csrf)) {
                            response.sendRedirect("/bad_csrf");
                            return;
                        }
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
