package com.proyecto.proyecto.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String rol;
    private LocalDate fechaNacimiento;

    @NotBlank
    @Email
    private String email;
}
