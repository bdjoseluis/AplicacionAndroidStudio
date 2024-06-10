package com.backend.TGF.model.repository;

import com.backend.TGF.model.entity.Comida;
import com.backend.TGF.model.entity.Comprable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IComprableRepository extends CrudRepository<Comprable, Long>{
    List<Comprable> findByTituloStartingWith(String nombre);
}
