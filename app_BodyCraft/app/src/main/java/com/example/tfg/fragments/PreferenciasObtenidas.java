package com.example.tfg.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.Toast;

import java.util.Locale;

public class PreferenciasObtenidas {
    public static final String GENERO = "genero";
    public static final String FECHA_NACIMIENTO = "fecha_nacimiento";
    public static final String ALTURA = "altura";
    public static final String OBJETIVOS = "objetivos";
    public static final String ESQUEMACOMIDASDIARIAS = "Esquema_comidas_horarios";
    public static final String IDIOMA = "idioma";

    public static String Genero;
    public static String FechaNacimiento;
    public static int Altura;
    public static String Objetivos;
    public static String EsquemaComidasHorarios;
    public static String Idioma;

    public static void ObtenerPreferencias(SharedPreferences preferences, Context context) {
        String msjError = "ok";

        // Obtener preferencias y establecer valores predeterminados
        Genero = preferences.getString(GENERO, "Hombre");
        Objetivos = preferences.getString(OBJETIVOS, "Perder Peso");
        Idioma = preferences.getString(IDIOMA, "Español, España");

        // Establecer idioma predeterminado si no existe preferencia
        if (Idioma == null || Idioma.isEmpty()) {
            Idioma = "Español, España";
        }

        // Configurar idioma según la preferencia
        if (Idioma.equals("Español, España")) {
            establecerIdioma(context, "es", "ES");
        } else if (Idioma.equals("Inglés, Inglaterra")) {
            establecerIdioma(context, "en", "GB");
        }

        // Obtener altura
        try {
            Altura = Integer.parseInt(preferences.getString(ALTURA, "160"));
        } catch (NumberFormatException e) {
            msjError = "ALTURA NO VÁLIDA";
        }

        // Mostrar mensaje de error si es necesario
        if (!msjError.equals("ok")) {
            Toast.makeText(context, "Verifique la configuración en " + msjError, Toast.LENGTH_LONG).show();
        }

        // Mostrar un mensaje con las preferencias obtenidas
        String msj = "Género: " + Genero + "\nObjetivos: " + Objetivos;
        Toast.makeText(context, msj, Toast.LENGTH_LONG).show();
    }

    private static void establecerIdioma(Context context, String lenguaje, String pais) {
        Locale locale = new Locale(lenguaje, pais);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
