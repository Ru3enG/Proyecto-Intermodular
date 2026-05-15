package com.proyecto.proyecto.controller.MVC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import com.proyecto.proyecto.dto.RecetaDTO;
import com.proyecto.proyecto.model.Comentario;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.service.ComentarioService;
import com.proyecto.proyecto.service.PuntuacionRecetaService;
import com.proyecto.proyecto.service.RankingService;
import com.proyecto.proyecto.service.RecetaService;
import com.proyecto.proyecto.service.UsuarioService;
import com.proyecto.proyecto.service.VotoDificultadService;

@Controller
public class RecetaController {

    private RecetaService recetaService;
    private RankingService rankingService;
    private PuntuacionRecetaService puntuacionRecetaService;
    private ComentarioService comentarioService;
    private UsuarioService usuarioService;
    private VotoDificultadService votoDificultadService;

    public RecetaController(RecetaService recetaService, RankingService rankingService,
            PuntuacionRecetaService puntuacionRecetaService, ComentarioService comentarioService,
            UsuarioService usuarioService, VotoDificultadService votoDificultadService) {
        this.recetaService = recetaService;
        this.rankingService = rankingService;
        this.puntuacionRecetaService = puntuacionRecetaService;
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
        this.votoDificultadService = votoDificultadService;
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

        Map<Long, String> dificultades = new HashMap<>();
        for (Receta r : recetas) {
            String d = votoDificultadService.getDificultadMedia(r.getId());
            if (d != null) dificultades.put(r.getId(), d);
        }

        model.addAttribute("recetas", recetas);
        model.addAttribute("rankings", rankings);
        model.addAttribute("rankingId", rankingId);
        model.addAttribute("nombre", nombre);
        model.addAttribute("dificultades", dificultades);

        return "recetas";
    }

    @GetMapping("/recetas/nueva")
    public String nuevaReceta(Model model) {
        List<Ranking> rankings = rankingService.getTodos();
        model.addAttribute("recetaDTO", new RecetaDTO());
        model.addAttribute("rankings", rankings);
        return "recetanueva";
    }

    @PostMapping("/recetas/nueva")
    public String guardarReceta(@Valid @ModelAttribute RecetaDTO recetaDTO,
            @RequestParam(required = false) MultipartFile imagen,
            @AuthenticationPrincipal UserDetails userDetails) {
        recetaService.crear(recetaDTO, userDetails.getUsername(), imagen);
        return "redirect:/recetas";
    }

    @GetMapping("/recetas/{id}")
    public String detalleReceta(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        Receta receta = recetaService.getById(id);
        List<Comentario> comentarios = comentarioService.getComentarios(id);

        Usuario usuario = usuarioService.getByUsername(userDetails.getUsername());

        // compruebo si ya voto y si es su propia receta
        boolean yaVoto = puntuacionRecetaService.yaVoto(id, usuario.getId());
        boolean esAutor = receta.getUsuario().getId().equals(usuario.getId());
        boolean yaVotoDificultad = votoDificultadService.yaVoto(id, usuario.getId());
        String dificultadMedia = votoDificultadService.getDificultadMedia(id);

        model.addAttribute("receta", receta);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("yaVoto", yaVoto);
        model.addAttribute("esAutor", esAutor);
        model.addAttribute("yaVotoDificultad", yaVotoDificultad);
        model.addAttribute("dificultadMedia", dificultadMedia);
        model.addAttribute("usernameActual", userDetails.getUsername());

        return "recetadetalle";
    }

    @GetMapping("/recetas/{id}/editar")
    public String editarRecetaForm(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        Receta receta = recetaService.getById(id);
        List<Ranking> rankings = rankingService.getTodos();

        RecetaDTO dto = new RecetaDTO();
        dto.setId(receta.getId());
        dto.setRecetaNombre(receta.getRecetaNombre());
        dto.setIngredientes(receta.getIngredientes());
        dto.setPasos(receta.getPasos());
        dto.setRankingId(receta.getRanking().getId());

        model.addAttribute("recetaDTO", dto);
        model.addAttribute("rankings", rankings);
        model.addAttribute("receta", receta);
        return "recetaeditar";
    }

    @PostMapping("/recetas/{id}/editar")
    public String editarReceta(@PathVariable Long id,
            @Valid @ModelAttribute RecetaDTO recetaDTO,
            @RequestParam(required = false) MultipartFile imagen,
            @AuthenticationPrincipal UserDetails userDetails) {
        recetaService.editar(id, recetaDTO, imagen);
        return "redirect:/recetas/" + id;
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

    // votar dificultad de una receta
    @PostMapping("/recetas/{id}/dificultad")
    public String votarDificultad(@PathVariable Long id,
            @RequestParam Integer dificultad,
            @AuthenticationPrincipal UserDetails userDetails) {
        votoDificultadService.votar(id, dificultad, userDetails.getUsername());
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