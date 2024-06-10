package com.backend.TGF.dto;

import java.util.List;

public class CreateDiasSemanaDTO {
    private String titulo;
    private String pic;
    private List<Long> ejerciciosIds; // Lista de IDs de ejercicios
    private List<Long> comidasIds; // Lista de IDs de comidas
    private Long usuarioId; // ID del usuario asociado

    public CreateDiasSemanaDTO() {
    }

    public CreateDiasSemanaDTO(String titulo, String pic, List<Long> ejerciciosIds, List<Long> comidasIds, Long usuarioId) {
        this.titulo = titulo;
        this.pic = pic;
        this.ejerciciosIds = ejerciciosIds;
        this.comidasIds = comidasIds;
        this.usuarioId = usuarioId;
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

    public List<Long> getEjerciciosIds() {
        return ejerciciosIds;
    }

    public void setEjerciciosIds(List<Long> ejerciciosIds) {
        this.ejerciciosIds = ejerciciosIds;
    }

    public List<Long> getComidasIds() {
        return comidasIds;
    }

    public void setComidasIds(List<Long> comidasIds) {
        this.comidasIds = comidasIds;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
