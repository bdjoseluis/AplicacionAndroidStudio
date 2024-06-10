package com.backend.TGF.model.services.Ejercicio;

import com.backend.TGF.model.entity.Ejercicio;

import java.util.List;

public interface IEjercicioService {
    Ejercicio findByName(String nombre);

    List<Ejercicio> findAll();

    Ejercicio findById(Long ejercicioId);

    List<Ejercicio> findByGrupoMuscular(String grupoMuscular);

    List<Ejercicio> findByNameContaining(String nombre);
}
