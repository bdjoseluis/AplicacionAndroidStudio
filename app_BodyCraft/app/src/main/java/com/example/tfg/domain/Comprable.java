package com.example.tfg.domain;
public class Comprable {
    private String titulo;
    private String pic;
    private String imagen;
    private double precio; // Nuevo atributo


    public Comprable(String titulo, String pic, String imagen, double precio) { // Modifica el constructor
        this.titulo = titulo;
        this.pic = pic;
        this.imagen = imagen;
        this.precio = precio;
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

    @Override
    public String toString() {
        return "Comprable{" +
                "titulo='" + titulo + '\'' +
                ", pic='" + pic + '\'' +
                ", imagen='" + imagen + '\'' +
                ", precio=" + precio +
                '}';
    }
}