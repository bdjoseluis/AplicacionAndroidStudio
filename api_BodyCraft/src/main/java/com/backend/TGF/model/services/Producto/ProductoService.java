package com.backend.TGF.model.services.Producto;

import com.backend.TGF.model.entity.Producto;
import com.backend.TGF.model.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService{
    @Autowired
    IProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() {
        return (List<Producto>) productoRepository.findAll();
    }

    @Override
    public Producto findById(Long productoId) {
        return productoRepository.findById(productoId).orElseThrow();
    }

    @Override
    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    @Override
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(Long productoId) {
        productoRepository.deleteById(productoId);
    }

    @Override
    public List<Producto> obtenerProductosPorMarca(String marca) {
        return productoRepository.findByMarca(marca);
    }

    @Override
    public List<Producto> findByTituloStartingWith(String nombre) {
        return productoRepository.findByTituloStartingWith(nombre);
    }
}
