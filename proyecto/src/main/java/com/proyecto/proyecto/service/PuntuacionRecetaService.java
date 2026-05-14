package com.proyecto.proyecto.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.model.PuntuacionReceta;
import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.PosicionRepository;
import com.proyecto.proyecto.repository.PuntuacionRecetaRepository;
import com.proyecto.proyecto.repository.RecetaRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class PuntuacionRecetaService {

    private PuntuacionRecetaRepository puntuacionRecetaRepository;
    private RecetaRepository recetaRepository;
    private UsuarioRepository usuarioRepository;
    private PosicionRepository posicionRepository;

    public PuntuacionRecetaService(PuntuacionRecetaRepository puntuacionRecetaRepository,
            RecetaRepository recetaRepository, UsuarioRepository usuarioRepository,
            PosicionRepository posicionRepository) {
        this.puntuacionRecetaRepository = puntuacionRecetaRepository;
        this.recetaRepository = recetaRepository;
        this.usuarioRepository = usuarioRepository;
        this.posicionRepository = posicionRepository;
    }

    @Transactional
    public void puntuar(Long recetaId, Integer puntos, String username) {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        if (receta.getUsuario().getId().equals(usuario.getId())) return;

        Optional<PuntuacionReceta> yaVoto = puntuacionRecetaRepository
                .findByUsuarioIdAndRecetaId(usuario.getId(), recetaId);
        if (yaVoto.isPresent()) return;

        if (puntos < 1 || puntos > 10) return;

        PuntuacionReceta puntuacion = new PuntuacionReceta();
        puntuacion.setUsuario(usuario);
        puntuacion.setReceta(receta);
        puntuacion.setPuntos(puntos);
        puntuacionRecetaRepository.save(puntuacion);

        Optional<Posicion> posicionOpt = posicionRepository
                .findByUsuarioIdAndRankingId(receta.getUsuario().getId(), receta.getRanking().getId());

        if (posicionOpt.isPresent()) {
            Posicion posicion = posicionOpt.get();
            posicion.setPuntuacion(posicion.getPuntuacion() + puntos);
            posicionRepository.save(posicion);
        }
    }

    // comprueba si un usuario ya puntuo una receta (para el html)
    public boolean yaVoto(Long recetaId, Long usuarioId) {
        return puntuacionRecetaRepository.findByUsuarioIdAndRecetaId(usuarioId, recetaId).isPresent();
    }
}