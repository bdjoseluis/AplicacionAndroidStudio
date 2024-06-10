package com.backend.TGF.dto;
import java.util.List;

public class ComidaDTO {
    private Long id;
    private String titulo;
    private String pic;
    private double kcal;
    private double proteinas;
    private double gramos;
    private double carbohidratos;

    public ComidaDTO(Long id, String titulo, String pic, double kcal, double proteinas, double gramos, double carbohidratos) {
        this.id = id;
        this.titulo = titulo;
        this.pic = pic;
        this.kcal = kcal;
        this.proteinas = proteinas;
        this.gramos = gramos;
        this.carbohidratos = carbohidratos;
    }

    public ComidaDTO() {

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
}
