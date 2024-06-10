package com.example.tfg.domain;

public class Ejercicio {
    private int id;
    private String nombre;
    private String grupoMuscular;
    private String urlVideo; // Nuevo campo para la URL del video

    public Ejercicio(String nombre, String grupoMuscular, String urlVideo) {
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.urlVideo = urlVideo;
    }

    public Ejercicio(int id, String nombre, String grupoMuscular, String urlVideo) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.urlVideo = urlVideo;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
