package com.backend.TGF.model.repository;

import com.backend.TGF.model.entity.Comida;
import com.backend.TGF.model.entity.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComidaRepository extends CrudRepository<Comida, Long> {
}
