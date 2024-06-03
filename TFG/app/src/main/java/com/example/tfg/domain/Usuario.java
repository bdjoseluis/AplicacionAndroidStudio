package com.example.tfg.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Usuario {

    private String name;
    private String genero; // Añadido según `UpdateUserDTO`
    private String email;
    private int diasquevoy; // Añadido según `UpdateUserDTO`
    private int comidas; // Añadido según `UpdateUserDTO`
    private double peso; // Añadido según `UpdateUserDTO`
    private int age; // Añadido según `UpdateUserDTO`
    private String image;
    private LocalDateTime creationDate;
    private LocalDateTime lastLogin;
    private boolean active = true;
    private List<Role> roles;
    private String username;
    private String password;
    private List<Integer> diasSemanaIds;


    // Constructor parametrizado
    public Usuario(String name, String genero, int diasquevoy, int comidas, double peso, int age,
                   String image, LocalDateTime creationDate, LocalDateTime lastLogin, boolean active, List<Role> roles,
                   String username, String password, List<Integer> diasSemanaIds) {
        this.name = name;
        this.genero = genero;
        this.diasquevoy = diasquevoy;
        this.comidas = comidas;
        this.peso = peso;
        this.age = age;
        this.image = image;
        this.creationDate = creationDate;
        this.lastLogin = lastLogin;
        this.active = active;
        this.roles = roles;
        this.username = username;
        this.password = password;
        this.diasSemanaIds = diasSemanaIds;
    }

    // Constructor por defecto
    public Usuario() {
    }

    // Getters y setters para todos los campos
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<Integer> getDiasSemanaIds() {
        return diasSemanaIds;
    }

    public void setDiasSemanaIds(List<Integer> diasSemanaIds) {
        this.diasSemanaIds = diasSemanaIds;
    }
    @Override
    public String toString() {
        return "Usuario{" +
                "name='" + name + '\'' +
                ", genero='" + genero + '\'' +
                ", diasquevoy=" + diasquevoy +
                ", comidas=" + comidas +
                ", peso=" + peso +
                ", age=" + age +
                ", image='" + image + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", lastLogin='" + lastLogin + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", diasSemanaIds=" + diasSemanaIds +
                '}';
    }


    // Considerar la sobrecarga de los métodos equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return active == usuario.active &&
                diasquevoy == usuario.diasquevoy &&
                comidas == usuario.comidas &&
                Double.compare(usuario.peso, peso) == 0 &&
                age == usuario.age &&
                Objects.equals(name, usuario.name) &&
                Objects.equals(genero, usuario.genero) &&
                Objects.equals(image, usuario.image) &&
                Objects.equals(creationDate, usuario.creationDate) &&
                Objects.equals(lastLogin, usuario.lastLogin) &&
                Objects.equals(roles, usuario.roles) &&
                Objects.equals(username, usuario.username) &&
                Objects.equals(password, usuario.password) &&
                Objects.equals(diasSemanaIds, usuario.diasSemanaIds); // Añadido para comparar listas de IDs
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, genero, diasquevoy, comidas, peso, age, image, creationDate, lastLogin, active, roles, username, password, diasSemanaIds);
    }
}
