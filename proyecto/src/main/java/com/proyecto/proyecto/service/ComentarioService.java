package com.proyecto.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.model.Comentario;
import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.ComentarioRepository;
import com.proyecto.proyecto.repository.RecetaRepository;
import com.proyecto.proyecto.repository.UsuarioRepository;

@Service
public class ComentarioService {

    private ComentarioRepository comentarioRepository;
    private UsuarioRepository usuarioRepository;
    private RecetaRepository recetaRepository;

    public ComentarioService(ComentarioRepository comentarioRepository,
            UsuarioRepository usuarioRepository, RecetaRepository recetaRepository) {
        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.recetaRepository = recetaRepository;
    }

    // devuelve todos los comentarios de una receta
    public List<Comentario> getComentarios(Long recetaId) {
        return comentarioRepository.findByRecetaId(recetaId);
    }

    // guarda un comentario nuevo
    public void comentar(Long recetaId, String texto, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        Comentario comentario = new Comentario();
        comentario.setTexto(texto);
        comentario.setUsuario(usuario);
        comentario.setReceta(receta);

        comentarioRepository.save(comentario);
    }
}