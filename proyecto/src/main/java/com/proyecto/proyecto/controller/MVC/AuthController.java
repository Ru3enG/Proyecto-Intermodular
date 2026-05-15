package com.proyecto.proyecto.controller.MVC;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.service.PosicionService;
import com.proyecto.proyecto.service.RankingService;

@Controller
public class AuthController {

    private RankingService rankingService;
    private PosicionService posicionService;

    public AuthController(RankingService rankingService, PosicionService posicionService) {
        this.rankingService = rankingService;
        this.posicionService = posicionService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/principal")
    public String principal(
            @RequestParam(defaultValue = "0") int idx,
            @RequestParam(defaultValue = "0") int pagina,
            Model model) {

        List<Ranking> rankings = rankingService.getTodos();

        if (rankings.isEmpty()) {
            model.addAttribute("rankings", rankings);
            return "principal";
        }

        int total = rankings.size();
        idx = ((idx % total) + total) % total;

        int indiceAnterior = (idx - 1 + total) % total;
        int indiceSiguiente = (idx + 1) % total;

        // cojo todas las posiciones del ranking actual
        ArrayList<Posicion> todasLasPosiciones = posicionService.getPosiciones(rankings.get(idx).getId());

        // calculo rangos reales considerando empates (1,2,2,4...)
        List<Integer> todosLosRangos = new ArrayList<>();
        int rango = 1;
        for (int i = 0; i < todasLasPosiciones.size(); i++) {
            if (i > 0 && todasLasPosiciones.get(i).getPuntuacion().equals(todasLasPosiciones.get(i - 1).getPuntuacion())) {
                todosLosRangos.add(todosLosRangos.get(i - 1));
            } else {
                todosLosRangos.add(rango);
            }
            rango++;
        }

        // paginacion de 20 en 20
        int tamanioPagina = 20;
        int totalPaginas = (int) Math.ceil((double) todasLasPosiciones.size() / tamanioPagina);
        if (totalPaginas == 0) totalPaginas = 1;

        // validacion circular de pagina
        pagina = ((pagina % totalPaginas) + totalPaginas) % totalPaginas;

        int desde = pagina * tamanioPagina;
        int hasta = Math.min(desde + tamanioPagina, todasLasPosiciones.size());

        // sublista de posiciones y rangos para la pagina actual
        ArrayList<Posicion> posicionesPagina = new ArrayList<>(todasLasPosiciones.subList(desde, hasta));
        List<Integer> rangosPagina = new ArrayList<>(todosLosRangos.subList(desde, hasta));

        model.addAttribute("rankings", rankings);
        model.addAttribute("posiciones", posicionesPagina);
        model.addAttribute("rangos", rangosPagina);
        model.addAttribute("indiceActual", idx);
        model.addAttribute("indiceAnterior", indiceAnterior);
        model.addAttribute("indiceSiguiente", indiceSiguiente);
        model.addAttribute("pagina", pagina);
        model.addAttribute("totalPaginas", totalPaginas);
        model.addAttribute("paginaAnterior", (pagina - 1 + totalPaginas) % totalPaginas);
        model.addAttribute("paginaSiguiente", (pagina + 1) % totalPaginas);
        model.addAttribute("offset", desde);

        return "principal";
    }
}