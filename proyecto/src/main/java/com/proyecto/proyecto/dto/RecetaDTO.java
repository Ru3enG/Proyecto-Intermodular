package com.proyecto.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecetaDTO {

    private Long id;
    private String recetaNombre;
    private String ingredientes;
    private String pasos;
    
    private Long rankingId;
}