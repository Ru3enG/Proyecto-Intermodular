package com.proyecto.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PosicionDTO {
    private Long id;
    private String rankingName;
    private String username;
    private Integer puntuacion;
}
