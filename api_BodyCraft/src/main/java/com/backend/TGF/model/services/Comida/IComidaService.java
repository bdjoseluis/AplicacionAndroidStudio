package com.backend.TGF.model.services.Comida;

import com.backend.TGF.model.entity.Comida;

import java.util.List;

public interface IComidaService {
    List<Comida> saveAll(List<Comida> comidas);

    void deleteById(Long comidaId);

    Comida findById(Long comidaId);

    void save(Comida comida);

    List<Comida> findAll();

    boolean existsById(Long comidaId);
}
