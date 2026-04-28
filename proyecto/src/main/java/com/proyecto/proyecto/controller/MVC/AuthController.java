package com.proyecto.proyecto.controller.MVC;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/")
    public String index() {
        return "registro";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/principal")
    public String principal() {
        return "principal";
    }
    /* 
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
    */
}
