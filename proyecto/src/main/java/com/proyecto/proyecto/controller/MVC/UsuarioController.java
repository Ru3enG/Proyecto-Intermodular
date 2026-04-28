package com.proyecto.proyecto.controller.MVC;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.proyecto.proyecto.dto.UsuarioDTO;
import com.proyecto.proyecto.service.UsuarioService;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String registrar(Model model) {
        model.addAttribute("u", new UsuarioDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute UsuarioDTO dto) {
        usuarioService.registrarUsuario(dto);
        return "login";
    }

}
