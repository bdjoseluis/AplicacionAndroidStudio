package com.example.tfg.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RutinaDefecto {
    private String nombre;
    private int imagen;
    private HashMap<Integer, List<String>> ejerciciosPorDia;

    public RutinaDefecto(String nombre, int imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.ejerciciosPorDia = new HashMap<>();
    }

    public RutinaDefecto(String nombre, int imagen, HashMap<Integer, List<String>> ejerciciosPorDia) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.ejerciciosPorDia = ejerciciosPorDia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public HashMap<Integer, List<String>> getEjerciciosPorDia() {
        return ejerciciosPorDia;
    }

    public void setEjerciciosPorDia(HashMap<Integer, List<String>> ejerciciosPorDia) {
        this.ejerciciosPorDia = ejerciciosPorDia;
    }

    public void agregarEjercicio(int diaSemana, String nombreEjercicio) {
        // Verifica si ya hay una lista de ejercicios para este día de la semana
        if (!ejerciciosPorDia.containsKey(diaSemana)) {
            ejerciciosPorDia.put(diaSemana, new ArrayList<>());
        }
        // Agrega el ejercicio a la lista correspondiente al día de la semana
        ejerciciosPorDia.get(diaSemana).add(nombreEjercicio);
    }

    public List<String> obtenerEjercicios(int diaSemana) {
        // Obtiene la lista de ejercicios para el día de la semana especificado
        return ejerciciosPorDia.getOrDefault(diaSemana, new ArrayList<>());
    }
}
