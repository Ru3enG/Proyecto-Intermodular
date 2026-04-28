package com.proyecto.proyecto.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.proyecto.exception.RecursoNoEncontradoException;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.UsuarioRepository;
@Service
public class UsuarioDetailsUserService implements UserDetailsService {
    private UsuarioRepository usuarioRepository;

    public UsuarioDetailsUserService(UsuarioRepository usuarioRepository){
        this.usuarioRepository=usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> opt = usuarioRepository.findByUsername(username);
        if (opt.isEmpty())
            throw new RecursoNoEncontradoException("No se a encontrado el usuario " + username);

        Usuario u = opt.get();
        return User
                .withUsername(username)
                .password(u.getPassword())
                .roles(u.getRol())
                .build();
    }
}
