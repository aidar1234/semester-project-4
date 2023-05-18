package ru.kpfu.itis.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kpfu.itis.security.JwtUtil;
import ru.kpfu.itis.security.filter.JwtAuthenticationFilter;
import ru.kpfu.itis.security.filter.JwtAuthorizationFilter;
import ru.kpfu.itis.security.filter.JwtLogoutFilter;
import ru.kpfu.itis.security.filter.JwtUsernamePasswordAuthenticationFilter;
import ru.kpfu.itis.service.RefreshTokenService;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:security.properties")
public class SecurityConfig {

    public static final String SIGN_IN_URL = "/sign_in";

    public static final String[] ADMIN_URLS = new String[]{
            "/admin"
    };

    public static final String[] AUTHENTICATION_URLS = new String[]{
            "/admin", "/profile"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        return new JwtAuthenticationFilter(
                jwtUtil,
                refreshTokenService,
                SIGN_IN_URL,
                AUTHENTICATION_URLS
        );
    }

    @Bean
    public JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                                                           RefreshTokenService refreshTokenService,
                                                                                           JwtUtil jwtUtil) {
        return new JwtUsernamePasswordAuthenticationFilter(
                authenticationManager,
                refreshTokenService,
                jwtUtil,
                new AntPathRequestMatcher("/sign_in", "POST"),
                "email",
                "password",
                "/sign_in/error"
        );
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter("/error", ADMIN_URLS);
    }

    @Bean
    public JwtLogoutFilter jwtLogoutFilter(RefreshTokenService refreshTokenService) {
        return new JwtLogoutFilter("/logout", refreshTokenService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtLogoutFilter jwtLogoutFilter,
                                                   JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {

        http.csrf().disable(); //CsrfTokenRepository ?
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.logout().disable();
        http.anonymous().disable();
        http.rememberMe().disable();

        http.addFilterAfter(jwtLogoutFilter, HeaderWriterFilter.class);
        http.addFilterAfter(jwtUsernamePasswordAuthenticationFilter, JwtLogoutFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter, JwtUsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}
