package com.proyecto.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.dto.RecetaDTO;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.model.VotoDificultad;
import com.proyecto.proyecto.repository.ComentarioRepository;
import com.proyecto.proyecto.repository.PuntuacionRecetaRepository;
import com.proyecto.proyecto.repository.RankingRepository;
import com.proyecto.proyecto.repository.RecetaRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;
import com.proyecto.proyecto.repository.VotoDificultadRepository;


import jakarta.transaction.Transactional;

@Service
public class RecetaService {

    private RecetaRepository recetaRepository;
    private UsuarioRepository usuarioRepository;
    private RankingRepository rankingRepository;
    private ComentarioRepository comentarioRepository;
    private PuntuacionRecetaRepository puntuacionRecetaRepository;
    private VotoDificultadRepository votoDificultadRepository;

    public RecetaService(RecetaRepository recetaRepository, UsuarioRepository usuarioRepository,
            RankingRepository rankingRepository, ComentarioRepository comentarioRepository,
            PuntuacionRecetaRepository puntuacionRecetaRepository,
            VotoDificultadRepository votoDificultadRepository) {
        this.recetaRepository = recetaRepository;
        this.usuarioRepository = usuarioRepository;
        this.rankingRepository = rankingRepository;
        this.comentarioRepository = comentarioRepository;
        this.puntuacionRecetaRepository = puntuacionRecetaRepository;
        this.votoDificultadRepository = votoDificultadRepository;
    }

    // devuelve todas las recetas
    public List<Receta> getTodas() {
        return recetaRepository.findAll();
    }

    // devuelve las recetas de un usuario
    public List<Receta> getRecetasDeUsuario(Long usuarioId) {
        return recetaRepository.findByUsuarioId(usuarioId);
    }

    // filtra por ranking
    public List<Receta> getRecetasPorRanking(Long rankingId) {
        return recetaRepository.findByRankingId(rankingId);
    }

    // filtra por nombre
    public List<Receta> getRecetasPorNombre(String nombre) {
        return recetaRepository.findByRecetaNombreContainingIgnoreCase(nombre);
    }

    // crea una receta nueva asociada al usuario logueado y al ranking elegido
    public void crear(RecetaDTO dto, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Ranking ranking = rankingRepository.findById(dto.getRankingId())
                .orElseThrow(() -> new RuntimeException("Ranking no encontrado"));

        Receta receta = new Receta();
        receta.setRecetaNombre(dto.getRecetaNombre());
        receta.setIngredientes(dto.getIngredientes());
        receta.setPasos(dto.getPasos());
        receta.setUsuario(usuario);
        receta.setRanking(ranking);

        recetaRepository.save(receta);

        VotoDificultad votoInicial = new VotoDificultad();
        votoInicial.setUsuario(usuario);
        votoInicial.setReceta(receta);
        votoInicial.setDificultad(dto.getDificultad());
        votoDificultadRepository.save(votoInicial);
    }

    @Transactional
    public void eliminar(Long id) {
        comentarioRepository.deleteByRecetaId(id);
        puntuacionRecetaRepository.deleteByRecetaId(id);
        votoDificultadRepository.deleteByRecetaId(id);
        recetaRepository.deleteById(id);
    }

    // devuelve una receta por id
    public Receta getById(Long id) {
        return recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));
    }
}