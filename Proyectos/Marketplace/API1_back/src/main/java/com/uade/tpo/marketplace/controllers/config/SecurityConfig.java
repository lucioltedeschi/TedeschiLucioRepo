package com.uade.tpo.marketplace.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(req -> req

                                .requestMatchers(HttpMethod.POST,"/api/v1/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/categories/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/categories/").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.GET,"/api/v1/users").hasRole("ADMIN")

                                .requestMatchers(HttpMethod.POST,"/api/v1/products/**").hasRole("SELLER")
                                .requestMatchers(HttpMethod.PUT,"/api/v1/products/**").hasRole("SELLER")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/products/**").hasAnyRole("ADMIN","SELLER")

                                .requestMatchers(HttpMethod.GET,"/api/v1/orders").hasRole("USER")
                                .requestMatchers(HttpMethod.POST,"/api/v1/orders").hasRole("USER")

                                .requestMatchers(HttpMethod.GET,"/api/v1/sellers/**").hasAnyRole("ADMIN","SELLER")
                                .requestMatchers(HttpMethod.GET,"/api/v1/sellers/**").hasAnyRole("ADMIN","SELLER")
                                .requestMatchers(HttpMethod.DELETE,"/api/v1/sellers/**").hasAnyRole("ADMIN","SELLER")

                                .requestMatchers(HttpMethod.GET,"/products/**").permitAll()
                                .requestMatchers(HttpMethod.GET,"/categories/**").permitAll()



                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .anyRequest()
                                .authenticated())
                        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                        .authenticationProvider(authenticationProvider)
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}