package com.proyecto.proyecto.controller.MVC;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.service.AdminPosicionService;
import com.proyecto.proyecto.service.RankingService;
import com.proyecto.proyecto.service.RecetaService;
import com.proyecto.proyecto.service.UsuarioService;

@Controller
public class AdminController {

    private RankingService rankingService;
    private UsuarioService usuarioService;
    private AdminPosicionService adminPosicionService;
    private RecetaService recetaService;

    public AdminController(RankingService rankingService, UsuarioService usuarioService,
            AdminPosicionService adminPosicionService, RecetaService recetaService) {
        this.rankingService = rankingService;
        this.usuarioService = usuarioService;
        this.adminPosicionService = adminPosicionService;
        this.recetaService = recetaService;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<Ranking> rankings = rankingService.getTodos();
        List<Usuario> usuarios = usuarioService.getTodos();
        List<Posicion> posiciones = adminPosicionService.getTodas();
        List<Receta> recetas = recetaService.getTodas();

        model.addAttribute("rankings", rankings);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("posiciones", posiciones);
        model.addAttribute("recetas", recetas);
        model.addAttribute("nuevoRanking", new Ranking());

        return "admin";
    }

    @PostMapping("/admin/ranking/crear")
    public String crearRanking(@ModelAttribute Ranking nuevoRanking) {
        rankingService.crear(nuevoRanking);
        return "redirect:/admin";
    }

    @PostMapping("/admin/ranking/eliminar/{id}")
    public String eliminarRanking(@PathVariable Long id) {
        rankingService.eliminar(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/usuario/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/usuario/password/{id}")
    public String cambiarPassword(@PathVariable Long id, @RequestParam String passwordTemporal) {
        usuarioService.cambiarPasswordAdmin(id, passwordTemporal);
        return "redirect:/admin";
    }

    @PostMapping("/admin/posicion/añadir")
    public String añadirPosicion(@RequestParam Long usuarioId, @RequestParam Long rankingId) {
        adminPosicionService.añadir(usuarioId, rankingId);
        return "redirect:/admin";
    }

    @PostMapping("/admin/posicion/eliminar/{id}")
    public String eliminarPosicion(@PathVariable Long id) {
        adminPosicionService.eliminar(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/receta/eliminar/{id}")
    public String eliminarReceta(@PathVariable Long id) {
        recetaService.eliminar(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/usuario/rol/{id}")
    public String cambiarRol(@PathVariable Long id, @RequestParam String nuevoRol) {
        usuarioService.cambiarRol(id, nuevoRol);
        return "redirect:/admin";
    }
}