package com.proyecto.proyecto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.proyecto.model.Posicion;

@Repository
public interface PosicionRepository extends JpaRepository<Posicion, Long> {

    List<Posicion> findByRankingIdOrderByPuntuacionDesc(Long rankingId);

    List<Posicion> findAllByOrderByPuntuacionDesc();

    Optional<Posicion> findByUsuarioIdAndRankingId(Long usuarioId, Long rankingId);
}