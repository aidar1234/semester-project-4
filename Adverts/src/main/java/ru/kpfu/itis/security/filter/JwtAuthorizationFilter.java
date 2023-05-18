package ru.kpfu.itis.security.filter;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kpfu.itis.security.manager.JwtAuthorizationManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final String[] adminUrls;
    private final JwtAuthorizationManager authorizationManager;
    private final String errorUrl;

    public JwtAuthorizationFilter(String errorUrl, String... adminUrls) {
        this.adminUrls = adminUrls;
        this.errorUrl = errorUrl;
        this.authorizationManager = new JwtAuthorizationManager(adminUrls);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (String url : adminUrls) {
            if (request.getServletPath().equals(url)) {
                try {
                    authorizationManager.verify(() -> authentication, request);

                } catch (AccessDeniedException e) {
                    response.sendError(403, errorUrl); // send to BasicErrorController
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
