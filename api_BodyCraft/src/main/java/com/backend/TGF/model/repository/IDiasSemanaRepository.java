package com.backend.TGF.model.repository;

import com.backend.TGF.model.entity.DiasSemana;
import com.backend.TGF.model.entity.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDiasSemanaRepository extends CrudRepository<DiasSemana, Long> {

    DiasSemana findByTitulo(String titulo);
}
