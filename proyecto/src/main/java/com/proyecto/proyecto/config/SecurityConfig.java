package com.proyecto.proyecto.config;

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
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/welcome")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login","/registro").permitAll()
                .requestMatchers("/welcome").hasAnyRole("USER","ADMIN")
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
