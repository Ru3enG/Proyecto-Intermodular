package com.proyecto.proyecto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.proyecto.dto.UsuarioDTO;
import com.proyecto.proyecto.exception.ConflictoException;
import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.ComentarioRepository;
import com.proyecto.proyecto.repository.PosicionRepository;
import com.proyecto.proyecto.repository.PuntuacionRecetaRepository;
import com.proyecto.proyecto.repository.RankingRepository;
import com.proyecto.proyecto.repository.RecetaRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;
import com.proyecto.proyecto.repository.VotoDificultadRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private UsuarioRepository usuarioRepository;
    private RankingRepository rankingRepository;
    private PosicionRepository posicionRepository;
    private ComentarioRepository comentarioRepository;
    private PuntuacionRecetaRepository puntuacionRecetaRepository;
    private VotoDificultadRepository votoDificultadRepository;
    private RecetaRepository recetaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            RankingRepository rankingRepository, PosicionRepository posicionRepository,
            ComentarioRepository comentarioRepository, PuntuacionRecetaRepository puntuacionRecetaRepository,
            VotoDificultadRepository votoDificultadRepository, RecetaRepository recetaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rankingRepository = rankingRepository;
        this.posicionRepository = posicionRepository;
        this.comentarioRepository = comentarioRepository;
        this.puntuacionRecetaRepository = puntuacionRecetaRepository;
        this.votoDificultadRepository = votoDificultadRepository;
        this.recetaRepository = recetaRepository;
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

    // elimina un usuario y todos sus datos relacionados
    @Transactional
    public void eliminar(Long id) {
        // borrar comentarios del usuario en recetas ajenas
        comentarioRepository.deleteByUsuarioId(id);
        // borrar puntuaciones y votos del usuario
        puntuacionRecetaRepository.deleteByUsuarioId(id);
        votoDificultadRepository.deleteByUsuarioId(id);
        // borrar las recetas del usuario (y sus comentarios, puntuaciones y votos asociados)
        List<Receta> recetas = recetaRepository.findByUsuarioId(id);
        for (Receta receta : recetas) {
            comentarioRepository.deleteByRecetaId(receta.getId());
            puntuacionRecetaRepository.deleteByRecetaId(receta.getId());
            votoDificultadRepository.deleteByRecetaId(receta.getId());
            recetaRepository.delete(receta);
        }
        // borrar posiciones del usuario en los rankings
        posicionRepository.deleteByUsuarioId(id);
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
    public String cambiarPassword(Long id, String passwordActual, String passwordNueva) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(passwordActual, usuario.getPassword())) {
            return "La contraseña actual no es correcta";
        }

        if (passwordNueva == null || passwordNueva.isBlank()) {
            return "La nueva contraseña no puede estar vacía";
        }

        if (passwordActual.equals(passwordNueva)) {
            return "La nueva contraseña debe ser distinta a la actual";
        }

        usuario.setPassword(passwordEncoder.encode(passwordNueva));
        usuarioRepository.save(usuario);
        return null;
    }

    public void cambiarRol(Long id, String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);
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