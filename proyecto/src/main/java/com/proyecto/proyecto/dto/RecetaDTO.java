package com.proyecto.proyecto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String recetaNombre;

    @NotBlank
    private String ingredientes;

    @NotBlank
    private String pasos;

    @NotNull
    private Long rankingId;

    @NotNull
    private Integer dificultad;
}