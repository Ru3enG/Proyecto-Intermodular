package com.proyecto.proyecto.controller.MVC;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.proyecto.model.Receta;
import com.proyecto.proyecto.model.Usuario;
import com.proyecto.proyecto.repository.UsuarioRepository;
import com.proyecto.proyecto.service.RecetaService;
import com.proyecto.proyecto.service.UsuarioService;

@Controller
public class OpcionesController {

    private UsuarioService usuarioService;
    private UsuarioRepository usuarioRepository;
    private RecetaService recetaService;

    public OpcionesController(UsuarioService usuarioService, UsuarioRepository usuarioRepository,
            RecetaService recetaService) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.recetaService = recetaService;
    }

    @GetMapping("/opciones")
    public String opciones(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // cojo las recetas del usuario
        List<Receta> recetas = recetaService.getRecetasDeUsuario(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("recetas", recetas);

        return "opciones";
    }

    @PostMapping("/opciones/cambiarPassword")
    public String cambiarPassword(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String passwordActual,
            @RequestParam String passwordNueva,
            Model model) {

        Usuario usuario = usuarioRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean exito = usuarioService.cambiarPassword(usuario.getId(), passwordActual, passwordNueva);

        if (!exito) {
            List<Receta> recetas = recetaService.getRecetasDeUsuario(usuario.getId());
            model.addAttribute("usuario", usuario);
            model.addAttribute("recetas", recetas);
            model.addAttribute("error", "La contraseña actual no es correcta");
            return "opciones";
        }

        return "redirect:/opciones?ok";
    }
}