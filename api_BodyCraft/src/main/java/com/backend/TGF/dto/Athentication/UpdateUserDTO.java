package com.backend.TGF.dto.Athentication;


import java.util.List;

public class UpdateUserDTO {
    private Long id; // Identificador único del usuario
    private String name; // Nombre del usuario
    private int diasquevoy; // Días que el usuario va
    private int comidas; // Comidas que el usuario ha tomado
    private double peso; // Peso del usuario
    private String genero; // Género del usuario
    private int age; // Edad del usuario
   // private List<Long> diasSemanaIds;

    // Constructor vacío
    public UpdateUserDTO() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiasquevoy() {
        return diasquevoy;
    }

    public void setDiasquevoy(int diasquevoy) {
        this.diasquevoy = diasquevoy;
    }

    public int getComidas() {
        return comidas;
    }

    public void setComidas(int comidas) {
        this.comidas = comidas;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

   /* public List<Long> getDiasSemanaIds() {
        return diasSemanaIds;
    }

    public void setDiasSemanaIds(List<Long> diasSemanaIds) {
        this.diasSemanaIds = diasSemanaIds;
    }
*/
    public UpdateUserDTO(Long id, String name, int diasquevoy, int comidas, double peso, String genero, int age, List<Long> diasSemanaIds) {
        this.id = id;
        this.name = name;
        this.diasquevoy = diasquevoy;
        this.comidas = comidas;
        this.peso = peso;
        this.genero = genero;
        this.age = age;
       // this.diasSemanaIds = diasSemanaIds;
    }
}

