package com.proyecto.proyecto.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.proyecto.proyecto.dto.UsuarioDTO;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

public class UsuarioService {


    
    private final PasswordEncoder passwordEncoder;

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registrar(UsuarioDTO dto){
        Usuario usuario = dtoToEntity(dto);
        usuario.setRol(dto.getRol());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuarioRepository.save(usuario);
    }

    public Usuario dtoToEntity(UsuarioDTO dto){
        return new Usuario
        (
            dto.nºusuario(),
            dto.username(),
            dto.getPassword(),
            dto.getRol()
        );
    }
}
