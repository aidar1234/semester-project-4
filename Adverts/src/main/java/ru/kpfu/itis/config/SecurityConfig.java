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
import ru.kpfu.itis.security.filter.*;
import ru.kpfu.itis.security.manager.JwtAuthorizationManager;
import ru.kpfu.itis.service.RefreshTokenService;
import ru.kpfu.itis.service.UserService;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:security.properties")
public class SecurityConfig {

    public static final String SIGN_IN_URL = "/sign_in";

    //If the access token is still alive, and the user is banned or deleted, and he wants, for example, to create a new ad
    public static final String[] DANGER_URL_PREFIXES = new String[]{
            "/admin", "/advert/new/transport", "/advert/new/electronics"
    };

    public static final String[] ADMIN_URL_PREFIX = new String[]{
            "/admin"
    };

    public static final String[] AUTHENTICATION_URL_PREFIXES = new String[]{
            "/admin", "/profile", "/advert/new"
    };

    public static final String[] CSRF_URLS = new String[]{
            "/admin/ban", "/admin/delete", "/advert/new/transport", "/advert/new/electronics"
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
                AUTHENTICATION_URL_PREFIXES
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
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtAuthorizationManager authorizationManager) {
        return new JwtAuthorizationFilter("/error", authorizationManager, ADMIN_URL_PREFIX);
    }

    @Bean
    public JwtLogoutFilter jwtLogoutFilter(RefreshTokenService refreshTokenService) {
        return new JwtLogoutFilter("/logout", refreshTokenService);
    }

    @Bean
    public UserStateFilter userStateFilter(UserService userService) {
        return new UserStateFilter(userService, DANGER_URL_PREFIXES);
    }

    @Bean
    public JwtCsrfFilter jwtCsrfFilter(JwtUtil jwtUtil) {
        return new JwtCsrfFilter(jwtUtil, CSRF_URLS);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtCsrfFilter jwtCsrfFilter,
                                                   JwtLogoutFilter jwtLogoutFilter,
                                                   JwtUsernamePasswordAuthenticationFilter jwtUsernamePasswordAuthenticationFilter,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   JwtAuthorizationFilter jwtAuthorizationFilter,
                                                   UserStateFilter userStateFilter) throws Exception {

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.logout().disable();
        http.anonymous().disable();
        http.rememberMe().disable();

        http.addFilterAfter(jwtLogoutFilter, HeaderWriterFilter.class);
        http.addFilterAfter(jwtCsrfFilter, JwtLogoutFilter.class);
        http.addFilterAfter(jwtUsernamePasswordAuthenticationFilter, JwtLogoutFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter, JwtUsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);
        http.addFilterAfter(userStateFilter, JwtAuthorizationFilter.class);

        return http.build();
    }
}
