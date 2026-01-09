package com.proyecto.proyecto.model;

import org.attoparser.dom.Text;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recetas {

    private Long id;

    @ManyToOne
    @JoinColumn(unique = true)
    private Long id_usuario;

    private String nombre;

    private Text ingredientes;

    private Text instrucciones;
    

}
