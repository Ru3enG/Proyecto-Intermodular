package com.proyecto.proyecto.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.proyecto.dto.PosicionDTO;
import com.proyecto.proyecto.model.Posicion;
import com.proyecto.proyecto.repository.PosicionRepository;

import jakarta.transaction.Transactional;

@Service
public class PosicionService {
    private PosicionRepository posicionRepository;
    public PosicionService(PosicionRepository posicionRepository) {
        this.posicionRepository = posicionRepository; 
    }
    @Transactional
    public List<PosicionDTO> Posiciones(){
        List<Posicion> posiciones = posicionRepository.findAll();
        List<PosicionDTO> posicionesDTO = new ArrayList<>();
        for (Posicion p : posiciones) {
            posicionesDTO.add(this.entityToDto(p));
        }
        return posicionesDTO;
    }
    public void guardarPosicion(PosicionDTO dto){
        Posicion posicion = dtoToEntity(dto);
        posicionRepository.save(posicion);
    }
    public Posicion dtoToEntity(PosicionDTO dto){
        Posicion posicion = new Posicion();
        posicion.setId(dto.getId());
        posicion.setPuntuacion(dto.getPuntuacion());
        return posicion;
    }
    public PosicionDTO entityToDto(Posicion posicion){
        PosicionDTO dto = new PosicionDTO();
        dto.setId(posicion.getId());
        dto.setPuntuacion(posicion.getPuntuacion());
        return dto;
    }
}