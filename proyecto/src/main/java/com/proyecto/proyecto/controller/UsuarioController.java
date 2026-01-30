package com.proyecto.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UsuarioController {

        @GetMapping("/index")
        public String inicio() {
            return "index";
        }
        
    
}
