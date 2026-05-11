package com.proyecto.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.proyecto.model.Receta;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {

    // recetas de un usuario
    List<Receta> findByUsuarioId(Long usuarioId);

    // recetas de un ranking concreto
    List<Receta> findByRankingId(Long rankingId);

    // recetas que contienen una palabra en el nombre (ignora mayusculas)
    List<Receta> findByRecetaNombreContainingIgnoreCase(String nombre);
}