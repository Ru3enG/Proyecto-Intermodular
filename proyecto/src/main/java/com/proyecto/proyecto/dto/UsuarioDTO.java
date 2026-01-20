package com.proyecto.proyecto.dto;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
