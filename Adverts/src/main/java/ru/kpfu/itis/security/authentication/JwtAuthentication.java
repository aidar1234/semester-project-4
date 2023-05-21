package ru.kpfu.itis.security.authentication;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.kpfu.itis.model.User;
import ru.kpfu.itis.security.AccessToken;
import ru.kpfu.itis.security.details.UserDetailsImpl;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class JwtAuthentication implements Authentication {

    private AccessToken accessToken;
    private boolean authenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(accessToken.getRole().toString()));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return new UserDetailsImpl(
                User.builder()
                        .email(accessToken.getEmail())
                        .role(accessToken.getRole())
                        .state(accessToken.getState())
                        .build()
        );
    }

    @Override
    public Object getPrincipal() {
        return accessToken;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return accessToken.getEmail();
    }
}
