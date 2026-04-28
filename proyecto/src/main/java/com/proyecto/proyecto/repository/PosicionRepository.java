package com.proyecto.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.proyecto.model.Posicion;

@Repository
public interface PosicionRepository extends JpaRepository<Posicion,Long>{

}
