package com.example.tfg.domain;

public enum Role {
    ADMIN("Admin"),
    USUARIO("Usuario"),
    REGISTRADO("Registrado");

    // Campo para almacenar el nombre del rol
    private final String name;

    // Constructor privado para inicializar cada constante enum con su nombre
    Role(String name) {
        this.name = name;
    }

    // Método para obtener el nombre del rol
    public String getName() {
        return name;
    }

    // Método toString para proporcionar una representación de cadena de cada constante enum
    @Override
    public String toString() {
        return name;
    }
}
