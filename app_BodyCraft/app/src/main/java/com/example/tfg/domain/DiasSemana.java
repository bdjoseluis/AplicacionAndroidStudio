package com.example.tfg.domain;

import java.util.List;

public class DiasSemana {
    int id;
    private String titulo;
    private String pic;
    private List<Comidas> comidas;
    private List<Ejercicio> ejercicios;
    private int usuarioId;

    public DiasSemana(int id, String titulo, String pic, List<Comidas> comidas, List<Ejercicio> ejercicios, int usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.pic = pic;
        this.comidas = comidas;
        this.ejercicios = ejercicios;
        this.usuarioId = usuarioId;
    }

    public DiasSemana(String titulo, String pic, List<Comidas> comidas, List<Ejercicio> ejercicios, int usuarioId) {
        this.titulo = titulo;
        this.pic = pic;
        this.comidas = comidas;
        this.ejercicios = ejercicios;
        this.usuarioId = usuarioId;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<Comidas> getComidas() {
        return comidas;
    }

    public void setComidas(List<Comidas> comidas) {
        this.comidas = comidas;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
}
