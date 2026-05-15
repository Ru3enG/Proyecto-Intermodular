package com.proyecto.proyecto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.proyecto.model.VotoDificultad;

@Repository
public interface VotoDificultadRepository extends JpaRepository<VotoDificultad, Long> {

    Optional<VotoDificultad> findByUsuarioIdAndRecetaId(Long usuarioId, Long recetaId);

    List<VotoDificultad> findByRecetaId(Long recetaId);

    void deleteByRecetaId(Long recetaId);

    void deleteByUsuarioId(Long usuarioId);
}
