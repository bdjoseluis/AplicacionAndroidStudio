package com.example.tfg.domain;


public class Producto {
    private int id;
    private String titulo;
    private String marca;

    private Integer cantGramos;

    private double kcal;

    private double grasas;

    private double carbohidratos;

    private double proteinas;
    private String urlImagen;

    public Producto() {
    }

    public Producto(int id, String titulo, String marca, Integer cantGramos, double kcal, double grasas, double carbohidratos, double proteinas, String urlImagen) {
        this.id = id;
        this.titulo = titulo;
        this.marca = marca;
        this.cantGramos = cantGramos;
        this.kcal = kcal;
        this.grasas = grasas;
        this.carbohidratos = carbohidratos;
        this.proteinas = proteinas;
        this.urlImagen = urlImagen; // Inicializa el nuevo campo
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getGrasas() {
        return grasas;
    }

    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    // toString method for easy display of the product information


    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @Override
    public String toString() {
        return
                "Título: '" + titulo + '\'' +
                ", Marca: '" + marca + '\'' +
                ", Cantidad en gramos: " + cantGramos +
                ", Kcal: " + kcal +
                ", Grasas: " + grasas +
                ", Carbohidratos: " + carbohidratos +
                ", Proteínas: " + proteinas;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Producto producto = (Producto) obj;
        return id == producto.id; // Compara solo los IDs de los productos
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id); // Utiliza el ID para calcular el hash code
    }
}
