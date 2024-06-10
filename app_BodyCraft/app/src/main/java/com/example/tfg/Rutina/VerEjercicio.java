package com.example.tfg.Rutina;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.EjerciciosAdapter;
import com.example.tfg.R;
import com.example.tfg.domain.Ejercicio;

import java.util.ArrayList;
import java.util.List;

public class VerEjercicio extends AppCompatActivity {

    private List<Ejercicio> listadoEjercicios;
    private EjerciciosAdapter adaptadorEjerciciosArriba;
    private EjerciciosAdapter adaptadorEjerciciosAbajo;
    private int dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ejercicio);

        // Obtener valores del intent
        dia = getIntent().getIntExtra("dia", 1);
        String grupoMuscular = getIntent().getStringExtra("categoria");

        Log.d("erroresapp", "Día seleccionado: " + dia);

        // Inicializar listas de ejercicios
        listadoEjercicios = new ArrayList<>();

        // Inicializar RecyclerView de arriba
        RecyclerView recyclerViewArriba = findViewById(R.id.recyclerViewEjerciciosArriba);
        recyclerViewArriba.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adaptadorEjerciciosArriba = new EjerciciosAdapter(this, new ArrayList<>(), dia);
        recyclerViewArriba.setAdapter(adaptadorEjerciciosArriba);

        // Inicializar RecyclerView de abajo
        RecyclerView recyclerViewAbajo = findViewById(R.id.recyclerViewEjerciciosAbajo);
        recyclerViewAbajo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adaptadorEjerciciosAbajo = new EjerciciosAdapter(this, new ArrayList<>(), dia);
        recyclerViewAbajo.setAdapter(adaptadorEjerciciosAbajo);

        // Obtener ejercicios por grupo muscular de la API
        obtenerEjerciciosPorGrupoMuscular(grupoMuscular);
    }

    // Función para obtener ejercicios por grupo muscular
    private void obtenerEjerciciosPorGrupoMuscular(String grupoMuscular) {
        API.getEjerciciosPorGrupoMuscular(grupoMuscular, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    Log.d("mislogs", "Grupo Muscular: " + grupoMuscular);
                    Log.d("mislogs", "Respuesta JSON: " + r.content);

                    listadoEjercicios = UtilJSONParser.parseEjercicios(r.content);

                    // Dividir la lista de ejercicios en dos mitades
                    int size = listadoEjercicios.size();
                    int mitad = size / 2;

                    // Pasar la primera mitad a adaptadorEjerciciosArriba
                    adaptadorEjerciciosArriba.setData(listadoEjercicios.subList(0, mitad));

                    // Pasar la segunda mitad a adaptadorEjerciciosAbajo
                    adaptadorEjerciciosAbajo.setData(listadoEjercicios.subList(mitad, size));

                    // Mostrar mensaje de éxito opcional
                } catch (Exception e) {
                    Log.e("VerEjercicio", "Error al analizar el JSON: ", e);
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
                Log.e("VerEjercicio", "Error al descargar datos: " + r.exception);
            }
        });
    }
}
