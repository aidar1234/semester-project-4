package ru.kpfu.itis.security.manager;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

public class JwtAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private final String[] adminUrls;

    public JwtAuthorizationManager(String... adminUrls) {
        this.adminUrls = adminUrls;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> auth, HttpServletRequest request) {
        Authentication authentication = auth.get();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return new AuthorizationDecision(true);
        } else {
            return new AuthorizationDecision(false);
        }
    }
}
