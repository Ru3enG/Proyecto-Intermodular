package com.proyecto.proyecto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.proyecto.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

    public Optional <Usuario> findByEmail(String email);
    
    public Optional <Usuario> findByUsername(String username);
}
