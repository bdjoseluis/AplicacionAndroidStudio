package com.backend.TGF.mappers;

import com.backend.TGF.dto.ComidaDTO;
import com.backend.TGF.dto.CreateDiasSemanaDTO;
import com.backend.TGF.dto.DiasSemanaDTO;
import com.backend.TGF.dto.EjercicioDTO;
import com.backend.TGF.exceptions.InvalidDataException;
import com.backend.TGF.model.entity.DiasSemana;
import com.backend.TGF.model.entity.User;
import com.backend.TGF.model.services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DiasSemanaMapper {

    private final ComidaMapper comidaMapper; // Mapper para entidades de comida
    private final EjercicioMapper ejercicioMapper; // Mapper para entidades de ejercicio
    private final UserService userService; // Servicio de usuarios

    @Autowired
    public DiasSemanaMapper(ComidaMapper comidaMapper, EjercicioMapper ejercicioMapper, UserService userService) {
        this.comidaMapper = comidaMapper;
        this.ejercicioMapper = ejercicioMapper;
        this.userService = userService;
    }

    /**
     * Convierte un objeto DiasSemana a un DiasSemanaDTO.
     *
     * @param diasSemana El objeto DiasSemana a convertir.
     * @return El objeto DiasSemanaDTO correspondiente.
     */
    public DiasSemanaDTO toDTO(DiasSemana diasSemana) {
        // Manejar posibles valores nulos de ejercicios y comidas
        List<EjercicioDTO> ejercicios = diasSemana.getEjercicios() != null
                ? diasSemana.getEjercicios().stream().map(ejercicioMapper::toDTO).collect(Collectors.toList())
                : Collections.emptyList();

        List<ComidaDTO> comidas = diasSemana.getComidas() != null
                ? diasSemana.getComidas().stream().map(comidaMapper::toDTO).collect(Collectors.toList())
                : Collections.emptyList();

        return new DiasSemanaDTO(
                diasSemana.getId(),
                diasSemana.getTitulo(),
                diasSemana.getPic(),
                ejercicios,
                comidas,
                diasSemana.getUser() != null ? diasSemana.getUser().getId() : null
        );
    }

    /**
     * Convierte un objeto DiasSemanaDTO a un objeto DiasSemana.
     *
     * @param diasSemanaDTO El objeto DiasSemanaDTO a convertir.
     * @return El objeto DiasSemana correspondiente.
     */
    public DiasSemana toEntity(DiasSemanaDTO diasSemanaDTO) {
        DiasSemana diasSemana = new DiasSemana();
        diasSemana.setId(diasSemanaDTO.getId());
        diasSemana.setTitulo(diasSemanaDTO.getTitulo());
        diasSemana.setPic(diasSemanaDTO.getPic());
        // Aquí puedes establecer otras propiedades según sea necesario

        // Si se pasan listas de ejercicios y comidas, mapéalas a entidades
        if (diasSemanaDTO.getEjercicios() != null) {
            diasSemana.setEjercicios(
                    diasSemanaDTO.getEjercicios().stream()
                            .map(ejercicioMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        if (diasSemanaDTO.getComidas() != null) {
            diasSemana.setComidas(
                    diasSemanaDTO.getComidas().stream()
                            .map(comidaMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        return diasSemana;
    }

    /**
     * Convierte un objeto CreateDiasSemanaDTO a un objeto DiasSemana.
     *
     * @param createDiasSemanaDTO El objeto CreateDiasSemanaDTO a convertir.
     * @return El objeto DiasSemana correspondiente.
     * @throws InvalidDataException Si el usuario no existe.
     */
    public DiasSemana fromCreateDiasSemanaDTO(CreateDiasSemanaDTO createDiasSemanaDTO) {
        DiasSemana diasSemana = new DiasSemana();
        diasSemana.setTitulo(createDiasSemanaDTO.getTitulo());
        diasSemana.setPic(createDiasSemanaDTO.getPic());

        // Configura el usuario usando el usuarioId
        if (createDiasSemanaDTO.getUsuarioId() != null) {
            Optional<User> optionalUser = userService.findById(createDiasSemanaDTO.getUsuarioId());
            if (optionalUser.isPresent()) {
                // Si el usuario está presente, configúralo en DiasSemana
                diasSemana.setUser(optionalUser.get());
            } else {
                // Si el usuario no existe, lanza una excepción
                throw new InvalidDataException("Usuario con ID " + createDiasSemanaDTO.getUsuarioId() + " no encontrado.");
            }
        }

        return diasSemana;
    }
}
