package com.backend.TGF.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comprable")
public class Comprable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;
    @Column
    private String pic;
    @Column
    private String imagen;
    @Column
    private double precio; // Nuevo atributo


    public Comprable(String titulo, String pic, String imagen, double precio) { // Modifica el constructor
        this.titulo = titulo;
        this.pic = pic;
        this.imagen = imagen;
        this.precio = precio;
    }

    public Comprable(String titulo, String pic, double precio) {
        this.titulo = titulo;
        this.pic = pic;
        this.precio = precio;
    }

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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
