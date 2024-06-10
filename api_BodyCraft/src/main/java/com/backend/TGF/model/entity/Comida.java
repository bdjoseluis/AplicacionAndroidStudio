package com.backend.TGF.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comida")
public class Comida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column
    private String pic;

    @Column
    private double kcal;

    @Column
    private double proteinas;

    @Column
    private double gramos;

    @Column
    private double carbohidratos;

    @JsonIgnoreProperties("comida")
    @ManyToOne
    @JoinColumn(name = "dias_semana_id")
    private DiasSemana diaSemana;

    @ManyToMany
    @JoinTable(
            name = "comida_producto",
            joinColumns = @JoinColumn(name = "comida_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos;

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getGramos() {
        return gramos;
    }

    public void setGramos(double gramos) {
        this.gramos = gramos;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public DiasSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiasSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Comida{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", pic='" + pic + '\'' +
                ", kcal=" + kcal +
                ", proteinas=" + proteinas +
                ", gramos=" + gramos +
                ", carbohidratos=" + carbohidratos +
                ", diaSemana=" + diaSemana +
                ", productos=" + productos +
                '}';
    }


}
