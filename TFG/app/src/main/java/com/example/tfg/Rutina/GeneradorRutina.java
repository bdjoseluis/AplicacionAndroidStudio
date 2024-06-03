package com.example.tfg.Rutina;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.RutinasAdapter;
import com.example.tfg.R;
import com.example.tfg.domain.DiasSemana;
import com.example.tfg.domain.RutinaDefecto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneradorRutina extends AppCompatActivity {

    private RecyclerView recyclerViewRutinas;
    private RutinasAdapter rutinasAdapter;
    private List<RutinaDefecto> listaRutinas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generador_rutina);

        recyclerViewRutinas = findViewById(R.id.recyclerViewRutinasgenerador);
        recyclerViewRutinas.setLayoutManager(new LinearLayoutManager(this));

        listaRutinas = cargarRutinas(); // Método para cargar las rutinas disponibles

        rutinasAdapter = new RutinasAdapter(this, listaRutinas);
        recyclerViewRutinas.setAdapter(rutinasAdapter);

        descargarDiasSemana(); // Descargar días de la semana y agregar ejercicios a la Rutina 1
    }

    // Método para cargar las rutinas disponibles (este método debe devolver una lista de objetos Rutina)
    private List<RutinaDefecto> cargarRutinas() {
        List<RutinaDefecto> rutinas = new ArrayList<>();
        // Aquí puedes cargar las rutinas desde una base de datos, archivo, etc.
        // Por ahora, simplemente crea una lista de rutinas predefinidas como ejemplo
        rutinas.add(new RutinaDefecto("FRECUENCIA 1", R.drawable.lunes));
        rutinas.add(new RutinaDefecto("Push-Pull-Legs", R.drawable.dia));
        // Agrega más rutinas según sea necesario
        return rutinas;
    }

    private void descargarDiasSemana() {
        // Obtener el userId del usuario actual desde las preferencias compartidas
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1);

        // Llamada a la API para obtener la lista completa de días de la semana
        API.getDiasSemanas(new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    // Analizar la respuesta JSON para obtener la lista de días de la semana
                    List<DiasSemana> todosLosDiasSemana = UtilJSONParser.parseArrayDiasSemana(r.content);

                    // Filtrar los días de la semana según el userId del usuario actual
                    List<DiasSemana> diasFiltrados = new ArrayList<>();
                    for (DiasSemana dia : todosLosDiasSemana) {
                        if (dia.getUsuarioId() == userId) {
                            diasFiltrados.add(dia);
                        }
                    }

                    Log.d(getClass().getName(), "Días de la semana descargados y filtrados para el usuario: " + diasFiltrados.size());

                    // Verificar si hay suficientes días filtrados para agregar ejercicios
                    if (diasFiltrados.size() >= 5) {
                        // Rutina 1 está en la posición 0 de la lista
                        RutinaDefecto rutina1 = listaRutinas.get(0);

                        // Lista de ejercicios por día
                        List<List<String>> ejerciciosPorDia = new ArrayList<>();

                        // Lista de ejercicios para agregar a cada día de la rutina
                        List<String> ejerciciosDia1 = Arrays.asList("Fondos", "Press de Banca");
                        List<String> ejerciciosDia2 = Arrays.asList("Remo con Barra", "Dominadas");
                        List<String> ejerciciosDia3 = Arrays.asList("Prensa de Piernas", "Sentadillas");
                        List<String> ejerciciosDia4 = Arrays.asList("Elevaciones Laterales", "Pájaros");
                        List<String> ejerciciosDia5 = Arrays.asList("Tríceps en Polea", "Curl de Bíceps", "Crunch");

                        ejerciciosPorDia.add(ejerciciosDia1);
                        ejerciciosPorDia.add(ejerciciosDia2);
                        ejerciciosPorDia.add(ejerciciosDia3);
                        ejerciciosPorDia.add(ejerciciosDia4);
                        ejerciciosPorDia.add(ejerciciosDia5);



                        // Iterar sobre los días filtrados y agregar los ejercicios correspondientes
                        for (int i = 0; i < 6; i++) {
                            int diaId = diasFiltrados.get(i).getId();
                            List<String> ejercicios = ejerciciosPorDia.get(i);
                            for (String ejercicio : ejercicios) {
                                rutina1.agregarEjercicio(diaId, ejercicio);
                            }
                        }
                    } else {
                        Log.d(getClass().getName(), "No hay suficientes días filtrados para agregar ejercicios a la Rutina");
                    }
                } catch (Exception e) {
                    Log.e(getClass().getName(), "Error al analizar el contenido JSON", e);
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
                Log.e(getClass().getName(), "Error al descargar los días de la semana: " + r.exception);
            }
        });
    }

}
