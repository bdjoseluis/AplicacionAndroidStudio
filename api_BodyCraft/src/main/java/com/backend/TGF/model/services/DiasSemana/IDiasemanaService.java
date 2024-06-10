package com.backend.TGF.model.services.DiasSemana;

import com.backend.TGF.model.entity.DiasSemana;

import java.util.List;

public interface IDiasemanaService {
    DiasSemana addDiaSemana(DiasSemana diasSemana);

    List<DiasSemana> findAll();

    boolean addEjercicioToDiaSemana(Long diaSemanaId, Long ejercicioId);

    boolean removeEjercicioFromDiaSemana(Long diaSemanaId, Long ejercicioId);

    DiasSemana findById(Long diaSemanaId);

    void save(DiasSemana diaSemana);
}
