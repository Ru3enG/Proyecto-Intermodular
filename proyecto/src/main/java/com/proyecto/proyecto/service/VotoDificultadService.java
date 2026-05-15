package com.proyecto.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.model.VotoDificultad;
import com.proyecto.proyecto.repository.RecetaRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;
import com.proyecto.proyecto.repository.VotoDificultadRepository;

@Service
public class VotoDificultadService {

    private final VotoDificultadRepository votoDificultadRepository;
    private final RecetaRepository recetaRepository;
    private final UsuarioRepository usuarioRepository;

    public VotoDificultadService(VotoDificultadRepository votoDificultadRepository,
            RecetaRepository recetaRepository, UsuarioRepository usuarioRepository) {
        this.votoDificultadRepository = votoDificultadRepository;
        this.recetaRepository = recetaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void votar(Long recetaId, Integer dificultad, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        if (receta.getUsuario().getId().equals(usuario.getId())) return;
        if (votoDificultadRepository.findByUsuarioIdAndRecetaId(usuario.getId(), recetaId).isPresent()) return;
        if (dificultad < 1 || dificultad > 5) return;

        VotoDificultad voto = new VotoDificultad();
        voto.setUsuario(usuario);
        voto.setReceta(receta);
        voto.setDificultad(dificultad);
        votoDificultadRepository.save(voto);
    }

    public boolean yaVoto(Long recetaId, Long usuarioId) {
        return votoDificultadRepository.findByUsuarioIdAndRecetaId(usuarioId, recetaId).isPresent();
    }

    public String getDificultadMedia(Long recetaId) {
        List<VotoDificultad> votos = votoDificultadRepository.findByRecetaId(recetaId);
        if (votos.isEmpty()) return null;
        int media = (int) Math.round(votos.stream().mapToInt(VotoDificultad::getDificultad).average().orElse(0));
        return switch (media) {
            case 1 -> "Muy Fácil";
            case 2 -> "Fácil";
            case 3 -> "Normal";
            case 4 -> "Complicada";
            case 5 -> "Muy Complicada";
            default -> null;
        };
    }
}
