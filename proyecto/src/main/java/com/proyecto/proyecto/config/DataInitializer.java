package com.proyecto.proyecto.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.RankingRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RankingRepository rankingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (rankingRepository.count() == 0) {
            rankingRepository.save(new Ranking(null, "General"));
        }

        if (usuarioRepository.findByUsername("Administrador").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("Administrador");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRol("ADMIN");
            usuarioRepository.save(admin);
        }

        if (usuarioRepository.findByUsername("Ruben").isEmpty()) {
            Usuario ruben = new Usuario();
            ruben.setUsername("Ruben");
            ruben.setEmail("ruben@ruben.com");
            ruben.setPassword(passwordEncoder.encode("Ruben"));
            ruben.setRol("USER");
            usuarioRepository.save(ruben);
        }
    }
}
