package com.backend.TGF.dto;

import java.util.List;

public class CreateComidasDTO {
    private String titulo;
    private String pic;
    private double kcal;
    private double proteinas;
    private double gramos;
    private double carbohidratos;
    private Long diaSemanaId; // ID del objeto DiasSemana asociado

    // Constructor vacío para la deserialización de JSON
    public CreateComidasDTO() {
    }

    // Constructor con campos
    public CreateComidasDTO(String titulo, String pic, double kcal, double proteinas, double gramos,
                           double carbohidratos, Long diaSemanaId) {
        this.titulo = titulo;
        this.pic = pic;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.gramos = gramos;
        this.carbohidratos = carbohidratos;
        this.diaSemanaId = diaSemanaId;
    }

    // Getters y Setters

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

    public Long getDiaSemanaId() {
        return diaSemanaId;
    }

    public void setDiaSemanaId(Long diaSemanaId) {
        this.diaSemanaId = diaSemanaId;
    }

    // Método toString para depuración, opcional
    @Override
    public String toString() {
        return "CreateComidaDTO{" +
                "titulo='" + titulo + '\'' +
                ", pic='" + pic + '\'' +
                ", kcal=" + kcal +
                ", proteinas=" + proteinas +
                ", gramos=" + gramos +
                ", carbohidratos=" + carbohidratos +
                ", diaSemanaId=" + diaSemanaId +
                '}';
    }
}
