package com.proyecto.proyecto.dto;

import java.time.LocalDate;

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
    private String username;
    private String password;
    private String rol;
    private LocalDate fechaNacimiento;

    public int Edad(){
        LocalDate fecha=LocalDate.now();
        int edad=0;
        int añoActu=fecha.getYear();int mesActu=fecha.getMonthValue();int diaActu=fecha.getDayOfMonth();
        int añoNaci=this.getFechaNacimiento().getYear();
        int mesNaci=this.getFechaNacimiento().getMonthValue();
        int diaNaci=this.getFechaNacimiento().getDayOfMonth();
        edad=añoActu-añoNaci;
        if(mesActu<mesNaci){
            edad--;
        }else{ 
            if (mesActu==mesNaci){
                if (diaActu<=diaNaci) {
                    edad--;
                }
            }
        }
        return edad;
    }
}
