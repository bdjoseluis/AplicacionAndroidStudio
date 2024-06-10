package com.backend.TGF.model.services.Ejercicio;

import com.backend.TGF.model.entity.Ejercicio;
import com.backend.TGF.model.repository.IDiasSemanaRepository;
import com.backend.TGF.model.repository.IEjercicioRepository;
import com.backend.TGF.model.services.DiasSemana.IDiasemanaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EjercicioService implements IEjercicioService {
    @Autowired
    IEjercicioRepository repository;

    @Override
    public Ejercicio findByName(String nombre) {
        return repository.findByNombre(nombre);
    }

    @Override
    public List<Ejercicio> findAll() {
        return (List<Ejercicio>) repository.findAll();
    }

    @Override
    public Ejercicio findById(Long ejercicioId) {
        return repository.findById(ejercicioId).orElseThrow();
    }

    @Override
    public List<Ejercicio> findByGrupoMuscular(String grupoMuscular) {
        return repository.findByGrupoMuscular(grupoMuscular);
    }

    @Override
    public List<Ejercicio> findByNameContaining(String nombre) {
        return repository.findByNombreStartingWith(nombre);
    }
}
