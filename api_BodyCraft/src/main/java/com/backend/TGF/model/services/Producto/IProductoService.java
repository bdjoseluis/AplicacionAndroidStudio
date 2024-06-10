package com.backend.TGF.model.services.Producto;

import com.backend.TGF.model.entity.Producto;

import java.util.List;

public interface IProductoService {
    List<Producto> findAll();

    Producto findById(Long productoId);

    void save(Producto producto);

    Producto saveProducto(Producto producto);

    void deleteById(Long productoId);

    List<Producto> obtenerProductosPorMarca(String marca);

    List<Producto> findByTituloStartingWith(String nombre);
}
