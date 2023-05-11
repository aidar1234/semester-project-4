package ru.kpfu.itis.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.kpfu.itis.security.filter.JwtAuthenticationFilter;
import ru.kpfu.itis.security.filter.JwtLogoutFilter;
import ru.kpfu.itis.security.filter.JwtUsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@PropertySource("classpath:security.properties")
public class SecurityConfig {

    public static final String STATIC_RESOURCE_PREFIX_URL = "/static/";

    public static final String[] permitAllUrls = new String[]{
            "/home", "/sign_in", "/sign_up"
    };

    public static final String[] adminUrls = new String[]{
            "/admin"
    };

    public static final String[] authenticateUrls = new String[]{
            "/admin", "/profile"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtLogoutFilter jwtLogoutFilter,
                                                   JwtUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter,
                                                   JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilter(jwtLogoutFilter);
        http.addFilter(usernamePasswordAuthenticationFilter);
        http.addFilterAfter(jwtAuthenticationFilter, JwtUsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .antMatchers(permitAllUrls).permitAll()
                .antMatchers(authenticateUrls).authenticated()
                .antMatchers(adminUrls).hasRole("ADMIN");

        http.logout().logoutUrl("/logout");
        return http.build();
    }

}

