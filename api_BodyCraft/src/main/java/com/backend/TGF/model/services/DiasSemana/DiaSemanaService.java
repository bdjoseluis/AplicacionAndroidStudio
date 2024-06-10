package com.backend.TGF.model.services.DiasSemana;

import com.backend.TGF.model.entity.DiasSemana;
import com.backend.TGF.model.entity.Ejercicio;
import com.backend.TGF.model.repository.IDiasSemanaRepository;
import com.backend.TGF.model.repository.IEjercicioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiaSemanaService implements IDiasemanaService {

    @Autowired
    private IDiasSemanaRepository diasSemanaRepository;

    @Autowired
    private IEjercicioRepository ejercicioRepository;

    @Override
    public DiasSemana addDiaSemana(DiasSemana diasSemana) {
        return diasSemanaRepository.save(diasSemana);
    }

    @Override
    public List<DiasSemana> findAll() {
        return (List<DiasSemana>) diasSemanaRepository.findAll();
    }

    @Override
    public boolean addEjercicioToDiaSemana(Long diaSemanaId, Long ejercicioId) {
        // Buscar el día de la semana
        DiasSemana diaSemana = diasSemanaRepository.findById(diaSemanaId)
                .orElseThrow(() -> new EntityNotFoundException("Día de la semana no encontrado con ID: " + diaSemanaId));

        // Buscar el ejercicio
        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId)
                .orElseThrow(() -> new EntityNotFoundException("Ejercicio no encontrado con ID: " + ejercicioId));

        // Añadir el ejercicio al día de la semana
        diaSemana.getEjercicios().add(ejercicio);

        // Guardar los cambios en el repositorio
        diasSemanaRepository.save(diaSemana);

        return true;
    }

    @Override
    public boolean removeEjercicioFromDiaSemana(Long diaSemanaId, Long ejercicioId) {
        // Buscar el día de la semana
        DiasSemana diaSemana = diasSemanaRepository.findById(diaSemanaId).orElse(null);
        if (diaSemana == null) {
            // No se encontró el día de la semana
            return false;
        }

        // Buscar el ejercicio
        Ejercicio ejercicio = ejercicioRepository.findById(ejercicioId).orElse(null);
        if (ejercicio == null) {
            // No se encontró el ejercicio
            return false;
        }

        // Eliminar el ejercicio del día de la semana
        boolean removed = diaSemana.getEjercicios().remove(ejercicio);
        if (removed) {
            // Guardar los cambios en el repositorio
            diasSemanaRepository.save(diaSemana);
            return true;
        } else {
            // Si no se eliminó el ejercicio, significa que no estaba asociado al día de la semana
            return false;
        }
    }

    @Override
    public DiasSemana findById(Long diaSemanaId) {
        return diasSemanaRepository.findById(diaSemanaId).orElseThrow();
    }

    @Override
    public void save(DiasSemana diaSemana) {
        diasSemanaRepository.save(diaSemana);
    }
}
