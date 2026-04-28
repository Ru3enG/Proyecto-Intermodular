package com.proyecto.proyecto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.repository.RankingRepository;
import com.proyecto.proyecto.exception.RecursoNoEncontradoException;


@Service
public class RankingService {
    
    private RankingRepository rankingRepository;
    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository; 
    }
    
    public List<Ranking> obtenerTodos() {
        return rankingRepository.findAll();
    }
    
    public Ranking obtenerPorId(Long id) {
        if (rankingRepository.findById(id).isPresent()) {
            return rankingRepository.findById(id).get();
        }
        throw new RecursoNoEncontradoException("Ranking no encontrado");
    }
    
    public Ranking crearRanking(Ranking ranking) {
        return rankingRepository.save(ranking);
    }
    
    public Ranking actualizar(Long id, Ranking ranking) {
        Ranking existente = obtenerPorId(id);
        existente.setRankingName(ranking.getRankingName());
        return rankingRepository.save(existente);
    }
    
    public void eliminar(Long id) {
        rankingRepository.deleteById(id);
    }
}
