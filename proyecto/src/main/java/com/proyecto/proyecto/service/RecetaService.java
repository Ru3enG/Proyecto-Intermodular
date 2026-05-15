package com.proyecto.proyecto.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
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
    private Cloudinary cloudinary;

    public RecetaService(RecetaRepository recetaRepository, UsuarioRepository usuarioRepository,
            RankingRepository rankingRepository, ComentarioRepository comentarioRepository,
            PuntuacionRecetaRepository puntuacionRecetaRepository,
            VotoDificultadRepository votoDificultadRepository, Cloudinary cloudinary) {
        this.recetaRepository = recetaRepository;
        this.usuarioRepository = usuarioRepository;
        this.rankingRepository = rankingRepository;
        this.comentarioRepository = comentarioRepository;
        this.puntuacionRecetaRepository = puntuacionRecetaRepository;
        this.votoDificultadRepository = votoDificultadRepository;
        this.cloudinary = cloudinary;
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
    public void crear(RecetaDTO dto, String username, MultipartFile imagen) {
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

        if (imagen != null && !imagen.isEmpty()) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> uploadResult = cloudinary.uploader().upload(imagen.getBytes(), Map.of());
                receta.setImagenUrl((String) uploadResult.get("secure_url"));
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen");
            }
        }

        recetaRepository.save(receta);

        VotoDificultad votoInicial = new VotoDificultad();
        votoInicial.setUsuario(usuario);
        votoInicial.setReceta(receta);
        votoInicial.setDificultad(dto.getDificultad());
        votoDificultadRepository.save(votoInicial);
    }

    public void editar(Long id, RecetaDTO dto, MultipartFile imagen) {
        Receta receta = recetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        Ranking ranking = rankingRepository.findById(dto.getRankingId())
                .orElseThrow(() -> new RuntimeException("Ranking no encontrado"));

        receta.setRecetaNombre(dto.getRecetaNombre());
        receta.setIngredientes(dto.getIngredientes());
        receta.setPasos(dto.getPasos());
        receta.setRanking(ranking);

        if (imagen != null && !imagen.isEmpty()) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> uploadResult = cloudinary.uploader().upload(imagen.getBytes(), Map.of());
                receta.setImagenUrl((String) uploadResult.get("secure_url"));
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la imagen");
            }
        }

        recetaRepository.save(receta);
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