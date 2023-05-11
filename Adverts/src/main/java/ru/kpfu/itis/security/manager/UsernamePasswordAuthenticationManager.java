package ru.kpfu.itis.security.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationManager implements AuthenticationManager {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!userDetails.getPassword().equals(passwordEncoder.encode((String) authentication.getCredentials()))) {
            throw new AuthenticationServiceException("password not match");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);
        auth.setAuthenticated(true);
        return auth;
    }
}
