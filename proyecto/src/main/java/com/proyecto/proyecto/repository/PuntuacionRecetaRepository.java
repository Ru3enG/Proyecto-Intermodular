package com.proyecto.proyecto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.proyecto.model.PuntuacionReceta;

@Repository
public interface PuntuacionRecetaRepository extends JpaRepository<PuntuacionReceta, Long> {

    Optional<PuntuacionReceta> findByUsuarioIdAndRecetaId(Long usuarioId, Long recetaId);

    void deleteByRecetaId(Long recetaId);
}