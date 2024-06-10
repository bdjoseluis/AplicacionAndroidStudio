package com.backend.TGF.dto.Athentication;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;

    private String genero; // Género del usuario

    private int age; // Edad del usuario

    private double peso; // Peso del usuario

    private int diasquevoy; // Días que el usuario asiste a eventos o actividades

    private int comidas; // Número de comidas que el usuario ha tomado
    private String image;
    private LocalDateTime creationDate;
    private LocalDateTime lastLogin;
    private boolean active;
    private List<String> roles;  // Puedes utilizar String para almacenar los nombres de roles
    private List<Long> diasSemanaIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Long> getDiasSemanaIds() {
        return diasSemanaIds;
    }

    public void setDiasSemanaIds(List<Long> diasSemanaIds) {
        this.diasSemanaIds = diasSemanaIds;
    }
}
