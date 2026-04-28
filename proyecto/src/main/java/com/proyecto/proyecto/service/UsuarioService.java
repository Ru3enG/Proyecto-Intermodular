package com.proyecto.proyecto.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.proyecto.dto.UsuarioDTO;
import com.proyecto.proyecto.exception.ConflictoException;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
@Service
public class UsuarioService {


    private final PasswordEncoder passwordEncoder;

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registrarUsuario(UsuarioDTO dto){
        Usuario usuario = dtoToEntity(dto);
        usuario.setRol("USER");
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        Optional<Usuario> opt = usuarioRepository.findByUsername(dto.getUsername());
        if (opt.isPresent()) {
            throw new ConflictoException("El usuario ya existe");
        }
        usuarioRepository.save(usuario);
    }


    public Usuario dtoToEntity(UsuarioDTO dto){
        return new Usuario
        (
            dto.getId(),
            dto.getUsername(),
            dto.getPassword(),
            dto.getRol(),
            dto.getFechaNacimiento(),
            dto.getEmail()
        );
    }
}
