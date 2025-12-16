package com.example.perpustakaan.perpustakaan.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/users/register", "/users/login", "/css/**",
                                                                "/js/**", "/vendor/**",
                                                                "/img/**", "/assets/**", "/uploads/**")
                                                .permitAll()
                                                .requestMatchers("/books/catalog").hasAnyRole("ADMIN", "VISITOR")
                                                .requestMatchers("/books/**", "/users/**").hasRole("ADMIN")
                                                .requestMatchers("/loans/**").authenticated()
                                                .anyRequest().authenticated())
                                .formLogin(login -> login
                                                .loginPage("/users/login")
                                                .loginProcessingUrl("/perform_login") // URL untuk submit form
                                                .defaultSuccessUrl("/?loggedin", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/users/login?logout")
                                                .permitAll());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
