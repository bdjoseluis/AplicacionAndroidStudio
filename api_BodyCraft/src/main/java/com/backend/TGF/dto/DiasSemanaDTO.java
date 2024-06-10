package com.backend.TGF.dto;

import java.util.List;

import java.util.List;

public class DiasSemanaDTO {
    private Long id;
    private String titulo;
    private String pic;
    private List<EjercicioDTO> ejercicios;
    private List<ComidaDTO> comidas; // Agregamos esta propiedad para la lista de ComidaDTOs
    private Long usuarioId;

    public DiasSemanaDTO() {
    }

    public DiasSemanaDTO(Long id, String titulo, String pic, List<EjercicioDTO> ejercicios, List<ComidaDTO> comidas, Long usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.pic = pic;
        this.ejercicios = ejercicios;
        this.comidas = comidas;
        this.usuarioId = usuarioId;
    }

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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<EjercicioDTO> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<EjercicioDTO> ejerciciosIds) {
        this.ejercicios = ejerciciosIds;
    }

    public List<ComidaDTO> getComidas() {
        return comidas;
    }

    public void setComidas(List<ComidaDTO> comidas) {
        this.comidas = comidas;
    }
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
