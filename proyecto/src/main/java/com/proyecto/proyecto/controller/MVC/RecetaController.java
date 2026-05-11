package com.proyecto.proyecto.controller.MVC;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.proyecto.dto.RecetaDTO;
import com.proyecto.proyecto.model.Comentario;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.UsuarioRepository;
import com.proyecto.proyecto.service.ComentarioService;
import com.proyecto.proyecto.service.PuntuacionRecetaService;
import com.proyecto.proyecto.service.RankingService;
import com.proyecto.proyecto.service.RecetaService;

@Controller
public class RecetaController {

    private RecetaService recetaService;
    private RankingService rankingService;
    private PuntuacionRecetaService puntuacionRecetaService;
    private ComentarioService comentarioService;
    private UsuarioRepository usuarioRepository;

    public RecetaController(RecetaService recetaService, RankingService rankingService,
            PuntuacionRecetaService puntuacionRecetaService, ComentarioService comentarioService,
            UsuarioRepository usuarioRepository) {
        this.recetaService = recetaService;
        this.rankingService = rankingService;
        this.puntuacionRecetaService = puntuacionRecetaService;
        this.comentarioService = comentarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/recetas")
    public String recetas(
            @RequestParam(required = false) Long rankingId,
            @RequestParam(required = false) String nombre,
            Model model) {

        List<Receta> recetas;

        if (rankingId != null) {
            recetas = recetaService.getRecetasPorRanking(rankingId);
        } else if (nombre != null && !nombre.isBlank()) {
            recetas = recetaService.getRecetasPorNombre(nombre);
        } else {
            recetas = recetaService.getTodas();
        }

        List<Ranking> rankings = rankingService.getTodos();

        model.addAttribute("recetas", recetas);
        model.addAttribute("rankings", rankings);
        model.addAttribute("rankingId", rankingId);
        model.addAttribute("nombre", nombre);

        return "recetas";
    }

    @GetMapping("/recetas/nueva")
    public String nuevaReceta(Model model) {
        List<Ranking> rankings = rankingService.getTodos();
        model.addAttribute("recetaDTO", new RecetaDTO());
        model.addAttribute("rankings", rankings);
        return "recetaNueva";
    }

    @PostMapping("/recetas/nueva")
    public String guardarReceta(@ModelAttribute RecetaDTO recetaDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        recetaService.crear(recetaDTO, userDetails.getUsername());
        return "redirect:/recetas";
    }

    @GetMapping("/recetas/{id}")
    public String detalleReceta(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        Receta receta = recetaService.getById(id);
        List<Comentario> comentarios = comentarioService.getComentarios(id);

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // compruebo si ya voto y si es su propia receta
        boolean yaVoto = puntuacionRecetaService.yaVoto(id, usuario.getId());
        boolean esAutor = receta.getUsuario().getId().equals(usuario.getId());

        model.addAttribute("receta", receta);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("yaVoto", yaVoto);
        model.addAttribute("esAutor", esAutor);
        model.addAttribute("usernameActual", userDetails.getUsername());

        return "recetaDetalle";
    }

    @PostMapping("/recetas/eliminar/{id}")
    public String eliminarReceta(@PathVariable Long id) {
        recetaService.eliminar(id);
        return "redirect:/recetas";
    }

    // puntuar una receta
    @PostMapping("/recetas/{id}/puntuar")
    public String puntuar(@PathVariable Long id,
            @RequestParam Integer puntos,
            @AuthenticationPrincipal UserDetails userDetails) {
        puntuacionRecetaService.puntuar(id, puntos, userDetails.getUsername());
        return "redirect:/recetas/" + id;
    }

    // comentar una receta
    @PostMapping("/recetas/{id}/comentar")
    public String comentar(@PathVariable Long id,
            @RequestParam String texto,
            @AuthenticationPrincipal UserDetails userDetails) {
        comentarioService.comentar(id, texto, userDetails.getUsername());
        return "redirect:/recetas/" + id;
    }
}