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

        String password = (String) authentication.getCredentials();

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new AuthenticationServiceException("password not match");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(null, null);
        token.setDetails(userDetails);
        return token;
    }
}
