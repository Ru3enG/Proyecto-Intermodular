package com.proyecto.proyecto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.proyecto.proyecto.repository.UsuarioRepository;
import com.proyecto.proyecto.service.UsuarioDetailsUserService;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return new UsuarioDetailsUserService(usuarioRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http
                .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/**")
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/principal", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/registro","/principal").permitAll()
                        .requestMatchers("/recetas").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());
        return http.build();
    }
}
