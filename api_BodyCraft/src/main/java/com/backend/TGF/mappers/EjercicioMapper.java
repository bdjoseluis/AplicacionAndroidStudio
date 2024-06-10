package com.backend.TGF.mappers;

import com.backend.TGF.model.entity.Ejercicio;
import com.backend.TGF.dto.EjercicioDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EjercicioMapper {
    public EjercicioDTO toDTO(Ejercicio ejercicio) {
        return new EjercicioDTO(
                ejercicio.getId(),
                ejercicio.getNombre(),
                ejercicio.getGrupoMuscular(),
                ejercicio.getUrlVideo()
        );
    }

    public Set<EjercicioDTO> toDTOSet(List<Ejercicio> ejercicios) {
        return ejercicios.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    public Ejercicio toEntity(EjercicioDTO ejercicioDTO) {
        // Crea una nueva instancia de Ejercicio
        Ejercicio ejercicio = new Ejercicio();

        // Configura las propiedades del objeto Ejercicio a partir de EjercicioDTO
        ejercicio.setId(ejercicioDTO.getId());
        ejercicio.setNombre(ejercicioDTO.getNombre());
        ejercicio.setGrupoMuscular(ejercicioDTO.getGrupoMuscular());
        // Configura otras propiedades según sea necesario

        return ejercicio;
    }
}