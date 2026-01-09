package com.proyecto.proyecto.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name="usuarios")
public class Usuario {
    @Id
    
    private Long nºusuario;
    private String nombreUsuario;
    private String contraseña;
    private LocalDate fechaNacimiento;
}
