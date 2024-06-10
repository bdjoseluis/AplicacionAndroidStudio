package com.backend.TGF.model.repository;

import com.backend.TGF.model.entity.Producto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoRepository extends CrudRepository<Producto, Long> {
    List<Producto> findByMarca(String marca);

    List<Producto> findByTituloStartingWith(String nombre);
}
