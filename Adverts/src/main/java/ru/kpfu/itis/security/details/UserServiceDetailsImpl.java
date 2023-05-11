package ru.kpfu.itis.security.details;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.kpfu.itis.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserServiceDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new UserDetailsImpl(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
        );
    }
}
