package com.proyecto.proyecto.controller.REST;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.repository.RankingRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/ranking")
public class ApiRestControllerRanking {

    private RankingRepository rankingRepository;

    public ApiRestControllerRanking(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    // GET todos los rankings
    @GetMapping
    public ResponseEntity<List<Ranking>> obtenerTodos() {
        List<Ranking> rankings = rankingRepository.findAll();
        return ResponseEntity.ok(rankings);
    }

    // GET ranking por ID
    @GetMapping("{id}")
    public ResponseEntity<Ranking> obtenerPorId(@PathVariable Long id) {
        Ranking ranking = rankingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ranking no encontrado"));
        return ResponseEntity.ok(ranking);
    }

    // POST crear nuevo ranking
    @PostMapping
    public ResponseEntity<Ranking> crear(@Valid @RequestBody Ranking ranking) {
        Ranking nuevoRanking = rankingRepository.save(ranking);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoRanking.getId())
                .toUri();
        return ResponseEntity.created(location).body(nuevoRanking);
    }

    // PUT actualizar ranking
    @PutMapping("{id}")
    public ResponseEntity<Ranking> actualizar(@PathVariable Long id, @Valid @RequestBody Ranking ranking) {
        Ranking existente = rankingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ranking no encontrado"));
        existente.setRankingName(ranking.getRankingName());
        Ranking actualizado = rankingRepository.save(existente);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE eliminar ranking
    @DeleteMapping("{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rankingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
