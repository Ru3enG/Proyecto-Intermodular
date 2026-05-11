package com.proyecto.proyecto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.PosicionRepository;
import com.proyecto.proyecto.repository.RankingRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;

@Service
public class AdminPosicionService {

    private PosicionRepository posicionRepository;
    private UsuarioRepository usuarioRepository;
    private RankingRepository rankingRepository;

    public AdminPosicionService(PosicionRepository posicionRepository,
            UsuarioRepository usuarioRepository, RankingRepository rankingRepository) {
        this.posicionRepository = posicionRepository;
        this.usuarioRepository = usuarioRepository;
        this.rankingRepository = rankingRepository;
    }

    // devuelve todas las posiciones
    public List<Posicion> getTodas() {
        return posicionRepository.findAll();
    }

    // añade un usuario a un ranking con 0 puntos si no esta ya
    public String añadir(Long usuarioId, Long rankingId) {
        Optional<Posicion> existente = posicionRepository.findByUsuarioIdAndRankingId(usuarioId, rankingId);
        if (existente.isPresent()) {
            return "El usuario ya está en ese ranking";
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Ranking ranking = rankingRepository.findById(rankingId)
                .orElseThrow(() -> new RuntimeException("Ranking no encontrado"));

        Posicion posicion = new Posicion();
        posicion.setUsuario(usuario);
        posicion.setRanking(ranking);
        posicion.setPuntuacion(0);
        posicionRepository.save(posicion);

        return "ok";
    }

    // elimina a un usuario de un ranking por id de posicion
    public void eliminar(Long posicionId) {
        posicionRepository.deleteById(posicionId);
    }
}