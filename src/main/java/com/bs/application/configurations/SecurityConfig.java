package com.bs.application.configurations;

import com.bs.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.bs.application.security.Permission.*;
import static com.bs.application.security.Role.ADMIN;
import static com.bs.application.security.Role.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/**"))
                        .permitAll()

                       /*
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/user/**")).hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(GET, "/api/v1/user/**")).hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(POST, "/api/v1/user/**")).hasAnyAuthority(ADMIN_WRITE.name(), USER_WRITE.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(PUT, "/api/v1/user/**")).hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(DELETE, "/api/v1/user/**")).hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())

                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/perfume/**")).hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(GET, "/api/v1/perfume/**")).hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(POST, "/api/v1/perfume/**")).hasAnyAuthority(ADMIN_WRITE.name(), USER_WRITE.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(PUT, "/api/v1/perfume/**")).hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(DELETE, "/api/v1/perfume/**")).hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())

                        .requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/admin/**")).hasRole(ADMIN.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(GET, "/api/v1/admin/**")).hasAuthority(ADMIN_READ.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(POST, "/api/v1/admin/**")).hasAuthority(ADMIN_WRITE.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(PUT, "/api/v1/admin/**")).hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(AntPathRequestMatcher.antMatcher(DELETE, "/api/v1/admin/**")).hasAuthority(ADMIN_DELETE.name())
                       */

                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService.userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
