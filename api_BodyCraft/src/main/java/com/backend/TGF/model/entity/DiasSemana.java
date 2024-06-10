package com.backend.TGF.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dias_semana")
public class DiasSemana {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column
    private String pic;



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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<Comida> getComidas() {
        return comidas;
    }

    public void setComidas(List<Comida> comidas) {
        this.comidas = comidas;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("diasSemana")
    private User user;

    @OneToMany(mappedBy = "diaSemana", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("diaSemana")
    private List<Comida> comidas;

    @ManyToMany
    @JoinTable(
            name = "dias_semana_ejercicio", // Nombre de la tabla de unión
            joinColumns = @JoinColumn(name = "dias_semana_id"), // Clave foránea en la tabla de unión que se refiere a DiasSemana
            inverseJoinColumns = @JoinColumn(name = "ejercicio_id") // Clave foránea en la tabla de unión que se refiere a Ejercicio
    )
    private List<Ejercicio> ejercicios;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiasSemana)) return false;
        DiasSemana that = (DiasSemana) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
