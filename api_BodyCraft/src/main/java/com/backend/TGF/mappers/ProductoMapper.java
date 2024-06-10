package com.backend.TGF.mappers;

import com.backend.TGF.dto.CreateProductoDTO;
import com.backend.TGF.dto.ProductosDTO;
import com.backend.TGF.model.entity.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    // Convierte un Producto a ProductoDTO
    public ProductosDTO toDTO(Producto producto) {
        if (producto == null) {
            return null;
        }

        return new ProductosDTO(
                producto.getId(),
                producto.getTitulo(),
                producto.getMarca(),
                producto.getCantGramos(),
                producto.getKcal(),
                producto.getGrasas(),
                producto.getCarbohidratos(),
                producto.getProteinas(),
                producto.getUrlimagen()
        );
    }

    // Convierte un ProductoDTO a Producto
    public Producto toEntity(ProductosDTO productoDTO) {
        if (productoDTO == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setId(productoDTO.getId());
        producto.setTitulo(productoDTO.getTitulo());
        producto.setMarca(productoDTO.getMarca());
        producto.setCantGramos(productoDTO.getCantGramos());
        producto.setKcal(productoDTO.getKcal());
        producto.setGrasas(productoDTO.getGrasas());
        producto.setCarbohidratos(productoDTO.getCarbohidratos());
        producto.setProteinas(productoDTO.getProteinas());
        producto.setUrlimagen(productoDTO.getUrlImagen());

        return producto;
    }
    public Producto toEntity(CreateProductoDTO createProductoDTO) {
        if (createProductoDTO == null) {
            return null;
        }

        Producto producto = new Producto();
        producto.setTitulo(createProductoDTO.getTitulo());
        producto.setMarca(createProductoDTO.getMarca());
        producto.setCantGramos(createProductoDTO.getCantGramos());
        producto.setKcal(createProductoDTO.getKcal());
        producto.setGrasas(createProductoDTO.getGrasas());
        producto.setCarbohidratos(createProductoDTO.getCarbohidratos());
        producto.setProteinas(createProductoDTO.getProteinas());
        producto.setUrlimagen(createProductoDTO.getUrlImagen());

        return producto;
    }
}
