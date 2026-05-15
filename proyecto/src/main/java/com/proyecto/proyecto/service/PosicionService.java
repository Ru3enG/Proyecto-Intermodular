package com.proyecto.proyecto.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.model.Ranking;
import com.proyecto.proyecto.repository.PosicionRepository;
import com.proyecto.proyecto.repository.RankingRepository;

@Service
public class PosicionService {

    private RankingRepository rankingRepository;
    private PosicionRepository posicionRepository;

    public PosicionService(RankingRepository rankingRepository, PosicionRepository posicionRepository) {
        this.rankingRepository = rankingRepository;
        this.posicionRepository = posicionRepository;
    }

    public List<Ranking> getRankings() {
        return rankingRepository.findAll();
    }

    public ArrayList<Posicion> getPosiciones(Long rankingId) {
        return new ArrayList<>(posicionRepository.findByRankingIdOrderByPuntuacionDescUltimaActualizacionAsc(rankingId));
    }
}