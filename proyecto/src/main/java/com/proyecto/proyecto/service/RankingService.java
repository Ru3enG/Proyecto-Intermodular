package com.proyecto.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.PosicionRepository;
import com.proyecto.proyecto.repository.RankingRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;

@Service
public class RankingService {

    private RankingRepository rankingRepository;
    private UsuarioRepository usuarioRepository;
    private PosicionRepository posicionRepository;

    public RankingService(RankingRepository rankingRepository, UsuarioRepository usuarioRepository,
            PosicionRepository posicionRepository) {
        this.rankingRepository = rankingRepository;
        this.usuarioRepository = usuarioRepository;
        this.posicionRepository = posicionRepository;
    }

    // devuelve todos los rankings
    public List<Ranking> getTodos() {
        return rankingRepository.findAll();
    }

    // crea un nuevo ranking y añade a todos los usuarios con 0 puntos
    public void crear(Ranking ranking) {
        rankingRepository.save(ranking);

        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            Posicion posicion = new Posicion();
            posicion.setRanking(ranking);
            posicion.setUsuario(u);
            posicion.setPuntuacion(0);
            posicionRepository.save(posicion);
        }
    }

    // elimina un ranking por id
    public void eliminar(Long id) {
        rankingRepository.deleteById(id);
    }
}