package com.backend.TGF.mappers;

import com.backend.TGF.dto.ActualizarComidaDTO;
import com.backend.TGF.dto.ComidaDTO;
import com.backend.TGF.model.entity.Comida;
import org.springframework.stereotype.Component;

@Component
public class ComidaMapper {

    // Método para convertir la entidad Comida a ComidaDTO
    public ComidaDTO toDTO(Comida comida) {
        return new ComidaDTO(
                comida.getId(),
                comida.getTitulo(),
                comida.getPic(),
                comida.getKcal(),
                comida.getProteinas(),
                comida.getGramos(),
                comida.getCarbohidratos()
        );
    }

    // Método para actualizar la entidad Comida a partir de ActualizarComidaDTO
    public void updateEntityFromDTO(Comida comida, ActualizarComidaDTO actualizarComidaDTO) {
        if (comida != null && actualizarComidaDTO != null) {
            // Actualiza las propiedades de Comida con los valores de ActualizarComidaDTO
            comida.setKcal(actualizarComidaDTO.getTotalKcal());
            comida.setProteinas(actualizarComidaDTO.getTotalProteinas());
            comida.setGramos(actualizarComidaDTO.getTotalGramos());
            comida.setCarbohidratos(actualizarComidaDTO.getTotalCarbohidratos());
        }
    }

    // Método para convertir un ComidaDTO en una entidad Comida
    public Comida toEntity(ComidaDTO comidaDTO) {
        Comida comida = new Comida();
        comida.setId(comidaDTO.getId());
        comida.setTitulo(comidaDTO.getTitulo());
        comida.setPic(comidaDTO.getPic());
        comida.setKcal(comidaDTO.getKcal());
        comida.setProteinas(comidaDTO.getProteinas());
        comida.setGramos(comidaDTO.getGramos());
        comida.setCarbohidratos(comidaDTO.getCarbohidratos());
        // Establece otras propiedades según sea necesario
        return comida;
    }
}
