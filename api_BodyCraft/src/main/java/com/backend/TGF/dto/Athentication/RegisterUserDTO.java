package com.backend.TGF.dto.Athentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {

    private String password;

    private String name;

    private String email;

    private String genero; // Género del usuario

    private int age; // Edad del usuario

    private double peso; // Peso del usuario

    private int diasquevoy; // Días que el usuario asiste a eventos o actividades

    private int comidas; // Número de comidas que el usuario ha tomado

    private String image;

    private List<Long> rolIds;
    private List<Long> diasSemanaIds;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Long> getRolIds() {
        return rolIds;
    }

    public void setRolIds(List<Long> rolIds) {
        this.rolIds = rolIds;
    }

    public List<Long> getDiasSemanaIds() {
        return diasSemanaIds;
    }

    public void setDiasSemanaIds(List<Long> diasSemanaIds) {
        this.diasSemanaIds = diasSemanaIds;
    }
}
