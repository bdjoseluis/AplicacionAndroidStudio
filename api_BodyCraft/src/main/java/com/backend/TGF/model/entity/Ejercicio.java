package com.backend.TGF.model.entity;

import com.backend.TGF.model.entity.DiasSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ejercicio")
public class Ejercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nombre;

    @Column(name = "grupo_muscular")
    private String grupoMuscular;

    @Column
    private String urlVideo;

    // Constructor
    public Ejercicio(String nombre, String grupoMuscular, String urlVideo) {
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.urlVideo = urlVideo;
    }

    // Relación de muchos a muchos con DiasSemana
    @ManyToMany(mappedBy = "ejercicios", fetch = FetchType.EAGER)
    private List<DiasSemana> diasSemana;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public List<DiasSemana> getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(List<DiasSemana> diasSemana) {
        this.diasSemana = diasSemana;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ejercicio)) return false;
        Ejercicio ejercicio = (Ejercicio) o;
        return Objects.equals(getId(), ejercicio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
