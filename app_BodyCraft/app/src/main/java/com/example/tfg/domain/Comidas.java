package com.example.tfg.domain;

import java.util.ArrayList;
import java.util.List;

public class Comidas {
    private int id;
    private String titulo;
    private String pic;
    private double kcal;
    private double proteinas;
    private double gramos;
    private double carbohidratos;
    private List<Producto> productoList;
    private int diaSemana;


    public Comidas(String title, String pic, List<Producto> productoList) {
        this.titulo = title;
        this.pic = pic;
        this.productoList = new ArrayList<>();
    }

    public Comidas(int id, String titulo, String pic, double kcal, double proteinas, double gramos, double carbohidratos, List<Producto> productoList) {
        this.id = id;
        this.titulo = titulo;
        this.pic = pic;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.gramos = gramos;
        this.carbohidratos = carbohidratos;
        this.productoList = productoList;
    }

    public Comidas(int id, String titulo, String pic, double kcal, double proteinas, double gramos, double carbohidratos) {
        this.id = id;
        this.titulo = titulo;
        this.pic = pic;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.gramos = gramos;
        this.carbohidratos = carbohidratos;
    }

    public Comidas(String titulo, String pic, double kcal, double proteinas, double gramos, double carbohidratos, List<Producto> productos) {
        this.titulo = titulo;
        this.pic = pic;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.gramos = gramos;
        this.carbohidratos = carbohidratos;
        this.productoList = productos;
    }

    public Comidas(int id, String titulo, String pic, double kcal, double proteinas, double gramos, double carbohidratos, List<Producto> productoList, int diaSemana) {
        this.id = id;
        this.titulo = titulo;
        this.pic = pic;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.gramos = gramos;
        this.carbohidratos = carbohidratos;
        this.productoList = productoList;
        this.diaSemana = diaSemana;
    }

    public Comidas() {
    }

// Getters y setters

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
    }

    public void añadirProducto(Producto producto) {
        if (productoList == null) {
            productoList = new ArrayList<>();
        }
        productoList.add(producto);
    }

    // Nuevo método para gestionar el día de la semana
    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }
}
