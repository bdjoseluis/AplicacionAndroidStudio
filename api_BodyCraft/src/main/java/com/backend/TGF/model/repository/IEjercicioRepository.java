package com.backend.TGF.model.repository;

import com.backend.TGF.model.entity.DiasSemana;
import com.backend.TGF.model.entity.Ejercicio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEjercicioRepository extends CrudRepository<Ejercicio, Long> {
    Ejercicio findByNombre(String nombre);

    List<Ejercicio> findByGrupoMuscular(String grupoMuscular);

    List<Ejercicio> findByNombreStartingWith(String nombre);

}
