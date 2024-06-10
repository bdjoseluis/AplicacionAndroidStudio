package com.backend.TGF.dto;

public class EjercicioDTO {
    private Long id;
    private String nombre;
    private String grupoMuscular;
    private String urlVideo;

    public EjercicioDTO(Long id, String nombre, String grupoMuscular, String urlVideo) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.urlVideo = urlVideo;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
