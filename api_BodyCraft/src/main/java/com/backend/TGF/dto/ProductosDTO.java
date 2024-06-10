package com.backend.TGF.dto;

import java.math.BigDecimal;

public class ProductosDTO {
    private Long id;
    private String titulo;
    private String marca;
    private Integer cantGramos;
    private BigDecimal kcal;
    private BigDecimal grasas;
    private BigDecimal carbohidratos;
    private BigDecimal proteinas;
    private String urlImagen;

    // Constructor sin argumentos
    public ProductosDTO() {}

    // Constructor con argumentos
    public ProductosDTO(Long id, String titulo, String marca, Integer cantGramos, BigDecimal kcal, BigDecimal grasas,
                       BigDecimal carbohidratos, BigDecimal proteinas, String urlImagen) {
        this.id = id;
        this.titulo = titulo;
        this.marca = marca;
        this.cantGramos = cantGramos;
        this.kcal = kcal;
        this.grasas = grasas;
        this.carbohidratos = carbohidratos;
        this.proteinas = proteinas;
        this.urlImagen = urlImagen;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getCantGramos() {
        return cantGramos;
    }

    public void setCantGramos(Integer cantGramos) {
        this.cantGramos = cantGramos;
    }

    public BigDecimal getKcal() {
        return kcal;
    }

    public void setKcal(BigDecimal kcal) {
        this.kcal = kcal;
    }

    public BigDecimal getGrasas() {
        return grasas;
    }

    public void setGrasas(BigDecimal grasas) {
        this.grasas = grasas;
    }

    public BigDecimal getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(BigDecimal carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public BigDecimal getProteinas() {
        return proteinas;
    }

    public void setProteinas(BigDecimal proteinas) {
        this.proteinas = proteinas;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
