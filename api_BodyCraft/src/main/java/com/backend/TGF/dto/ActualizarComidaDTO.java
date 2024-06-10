package com.backend.TGF.dto;

public class ActualizarComidaDTO {
    private double totalKcal;
    private double totalProteinas;
    private double totalGramos;
    private double totalCarbohidratos;

    // Getters y setters
    public double getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(double totalKcal) {
        this.totalKcal = totalKcal;
    }

    public double getTotalProteinas() {
        return totalProteinas;
    }

    public void setTotalProteinas(double totalProteinas) {
        this.totalProteinas = totalProteinas;
    }

    public double getTotalGramos() {
        return totalGramos;
    }

    public void setTotalGramos(double totalGramos) {
        this.totalGramos = totalGramos;
    }

    public double getTotalCarbohidratos() {
        return totalCarbohidratos;
    }

    public void setTotalCarbohidratos(double totalCarbohidratos) {
        this.totalCarbohidratos = totalCarbohidratos;
    }


}
