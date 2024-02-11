package com.play.java.bbgeducation.infrastructure.auth.config;

import com.play.java.bbgeducation.infrastructure.auth.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.play.java.bbgeducation.infrastructure.auth.Roles.ADMIN;
import static com.play.java.bbgeducation.infrastructure.auth.Roles.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    private static final String[] WHITE_LIST_URL = {
            "/api",
            "/api-docs",
            "/api/auth/**"
    };

    public SecurityConfiguration(AuthenticationProvider authenticationProvider, JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    //TODO: security move to Method Security in the controllers if httpmethod categorizing doesn't meet requirements
    // (instead of request matches below)

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                .requestMatchers(GET, "/api/programs/**").hasAnyRole(ADMIN,USER)
                                .requestMatchers(POST, "/api/programs/**").hasRole(ADMIN)
                                .requestMatchers(PUT, "/api/programs/**").hasRole(ADMIN)
                                .requestMatchers(DELETE, "/api/programs/**").hasRole(ADMIN)
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
