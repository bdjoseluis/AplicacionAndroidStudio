package com.example.tfg.Rutina;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.AdaptadorRutinaDias;
import com.example.tfg.Ajustes;
import com.example.tfg.Compra.Compra;
import com.example.tfg.Dieta.Dieta;
import com.example.tfg.Inicio;
import com.example.tfg.R;
import com.example.tfg.domain.DiasSemana;

import java.util.ArrayList;
import java.util.List;

public class Rutina extends AppCompatActivity {
    private ListView lista;
    private AdaptadorRutinaDias adaptador;
    private List<DiasSemana> diasSemanaList;
    private Button generadoractividadbtn;
    private LinearLayout inicio, dieta, rutina, ajustes, compras;
    private View backgroundView;
    private ImageView cloudImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstTime = preferences.getBoolean("isFirstTimeRutina", true);

        lista = findViewById(R.id.comidasDiasSemana);
        diasSemanaList = new ArrayList<>();
        generadoractividadbtn = findViewById(R.id.generadoractividadbtn);

        adaptador = new AdaptadorRutinaDias(this, R.layout.itemlistdiasrutina, diasSemanaList);
        lista.setAdapter(adaptador);
        descargarDiasSemana();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtiene el día de la semana seleccionado
                DiasSemana diaSeleccionado = diasSemanaList.get(position);

                // Crea un intent para iniciar la actividad Ejercicios
                Intent intent = new Intent(Rutina.this, Ejercicios.class);

                // Pasa el título del día seleccionado como un dato extra
                intent.putExtra("diarutinaseleccionado", diaSeleccionado.getId());
                intent.putExtra("diaNombre", diaSeleccionado.getTitulo());

                // Inicia la actividad Ejercicios
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        generadoractividadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Rutina.this, GeneradorRutina.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        backgroundView = findViewById(R.id.backgroundView);
        cloudImageView = findViewById(R.id.cloudImageView);

        if (isFirstTime) {
            mostrarMensajeInicial();
        }
        configurarBotonesNavegacion();
    }
    private void configurarBotonesNavegacion() {
        inicio = findViewById(R.id.botoninicio);
        dieta = findViewById(R.id.botondieta);
        rutina = findViewById(R.id.botonrutina);
        ajustes = findViewById(R.id.botonajustes);
        compras = findViewById(R.id.botontienda);

        inicio.setOnClickListener(v -> {
            Intent intent = new Intent(Rutina.this, Inicio.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        dieta.setOnClickListener(v -> {
            Intent intent = new Intent(Rutina.this, Dieta.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        ajustes.setOnClickListener(v -> {
            Intent intent = new Intent(Rutina.this, Ajustes.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        compras.setOnClickListener(v -> {
            Intent intent = new Intent(Rutina.this, Compra.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
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

                    // Actualizar el adaptador con la lista filtrada de días de la semana
                    adaptador.clear();
                    adaptador.addAll(diasFiltrados);
                    adaptador.notifyDataSetChanged();

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
    @Override
    public void onBackPressed() {
        // Finaliza la actividad actual y cierra la aplicación
        super.onBackPressed();
        finishAffinity();
    }
    private void mostrarMensajeInicial() {
        final TextView textViewFirstTime = findViewById(R.id.textViewFirstTime);
        final LottieAnimationView lotiRutina = findViewById(R.id.toast_animationrutina);
        final ImageView cloudImageView = findViewById(R.id.cloudImageView);
        backgroundView.setVisibility(View.VISIBLE);
        textViewFirstTime.setVisibility(View.VISIBLE);
        lotiRutina.setVisibility(View.VISIBLE);
        cloudImageView.setVisibility(View.VISIBLE);
        findViewById(android.R.id.content).setBackgroundColor(Color.BLACK);

        backgroundView.setOnClickListener(v -> {
            textViewFirstTime.setVisibility(View.GONE);
            lotiRutina.setVisibility(View.GONE);
            cloudImageView.setVisibility(View.GONE);
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#ADD8E6"));
            backgroundView.setVisibility(View.GONE);

            // Marcar que ya se ha mostrado el mensaje inicial
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Rutina.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTimeRutina", false);
            editor.apply();

            // Descargar los días de la semana después de mostrar el mensaje inicial
            descargarDiasSemana();
        });
    }
}
