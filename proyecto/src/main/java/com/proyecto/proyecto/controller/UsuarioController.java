package com.proyecto.proyecto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UsuarioController {

        @GetMapping("/Main")
        public String getMethodName(@RequestParam String param) {
            return "Main";
        }
        
    
}
