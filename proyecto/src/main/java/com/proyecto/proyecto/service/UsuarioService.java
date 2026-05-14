package com.proyecto.proyecto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.proyecto.dto.UsuarioDTO;
import com.proyecto.proyecto.exception.ConflictoException;
import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.PosicionRepository;
import com.proyecto.proyecto.repository.RankingRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private UsuarioRepository usuarioRepository;
    private RankingRepository rankingRepository;
    private PosicionRepository posicionRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            RankingRepository rankingRepository, PosicionRepository posicionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rankingRepository = rankingRepository;
        this.posicionRepository = posicionRepository;
    }

    public List<Usuario> getTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario getByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional
    public void registrarUsuario(UsuarioDTO dto) {

        // compruebo que el usuario no exista ya
        Optional<Usuario> opt = usuarioRepository.findByUsername(dto.getUsername());
        if (opt.isPresent()) {
            throw new ConflictoException("El usuario ya existe");
        }

        // creo el usuario y lo guardo
        Usuario usuario = dtoToEntity(dto);
        usuario.setRol("USER");
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuarioRepository.save(usuario);

        // cojo todos los rankings y creo una posicion con 0 puntos para cada uno
        List<Ranking> rankings = rankingRepository.findAll();
        for (Ranking r : rankings) {
            Posicion posicion = new Posicion();
            posicion.setRanking(r);
            posicion.setUsuario(usuario);
            posicion.setPuntuacion(0);
            posicionRepository.save(posicion);
        }
    }

    // elimina un usuario por id
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    // el admin cambia la contraseña de un usuario a una temporal
    public void cambiarPasswordAdmin(Long id, String passwordTemporal) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setPassword(passwordEncoder.encode(passwordTemporal));
        usuarioRepository.save(usuario);
    }

    // el usuario cambia su propia contraseña, comprueba que la actual sea correcta
    public boolean cambiarPassword(Long id, String passwordActual, String passwordNueva) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // compruebo que la contraseña actual coincide con la que tiene en la base de datos
        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            return false;
        }

        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
        return true;
    }

    public Usuario dtoToEntity(UsuarioDTO dto) {
        return new Usuario(
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getRol(),
                dto.getFechaNacimiento(),
                dto.getEmail());
    }
}