package com.example.tfg.Dieta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.AdaptadorComidaListView;
import com.example.tfg.Adapter.MiAdaptadorDiasSemana;
import com.example.tfg.Ajustes;
import com.example.tfg.Compra.Compra;
import com.example.tfg.Inicio;
import com.example.tfg.R;
import com.example.tfg.Rutina.Rutina;
import com.example.tfg.domain.Comidas;
import com.example.tfg.domain.DiasSemana;

import java.util.ArrayList;
import java.util.List;

public class Dieta extends AppCompatActivity {

    private AdaptadorComidaListView adaptadorComida;
    private MiAdaptadorDiasSemana adaptadorDiasSemana;
    private RecyclerView recyclerViewDiasSemana;
    private List<DiasSemana> listaDiasSemana;

    private LinearLayout btnInicio, btnDieta, btnRutina, btnAjustes, btnCompras;
    private int diaSeleccionado;
    private int userId;

    private TextView textViewKcal, textViewProteina, textViewGrasas, textViewCarbohidratos, textViewMaxKcal;

    private int primerDiaSemana;
    private int peso;
    private String genero;
    private ImageView cloudImageView;
    private View backgroundView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt("userId", -1);
        peso = preferences.getInt("userpeso", -1);
        genero = preferences.getString("usergenero", "Hombre");
        boolean isFirstTime = preferences.getBoolean("isFirstTimeDieta", true);
        backgroundView = findViewById(R.id.backgroundView);
        cloudImageView = findViewById(R.id.cloudImageView);
        inicializarInterfazUsuario();
        descargarDiasSemana();



        configurarBotonesNavegacion();
        if (isFirstTime) {
            mostrarMensajeInicial();
        }
    }

    private void inicializarInterfazUsuario() {
        configurarRecyclerViewDiasSemana();
        configurarListViewComidas();
        inicializarTextViewsTotales();
    }

    private void configurarBotonesNavegacion() {
        btnInicio = findViewById(R.id.botoninicio);
        btnDieta = findViewById(R.id.botondieta);
        btnRutina = findViewById(R.id.botonrutina);
        btnAjustes = findViewById(R.id.botonajustes);
        btnCompras = findViewById(R.id.botontienda);

        btnInicio.setOnClickListener(v -> navegarAInicio());
        btnDieta.setOnClickListener(v -> mostrarMensajeEstarEnDieta());
        btnRutina.setOnClickListener(v -> navegarARutina());
        btnAjustes.setOnClickListener(v -> navegarAAjustes());
        btnCompras.setOnClickListener(v -> navegarACompra());
    }

    private void navegarAInicio() {
        startActivity(new Intent(Dieta.this, Inicio.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void mostrarMensajeEstarEnDieta() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void navegarARutina() {
        startActivity(new Intent(Dieta.this, Rutina.class)
        );
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void navegarAAjustes() {
        startActivity(new Intent(Dieta.this, Ajustes.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void navegarACompra() {
        startActivity(new Intent(Dieta.this, Compra.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void configurarListViewComidas() {
        ListView listViewComidas = findViewById(R.id.comidasDiasSemana);
        adaptadorComida = new AdaptadorComidaListView(this, R.layout.itemlistview, new ArrayList<>());
        listViewComidas.setAdapter(adaptadorComida);

        adaptadorComida.setOnAddItemClickListener((position, comida) -> {
            Intent intent = new Intent(Dieta.this, addProductos.class);
            intent.putExtra("comida_id", comida.getId());
            intent.putExtra("dia_Semana", diaSeleccionado);
            intent.putExtra("comida_nombre", comida.getTitulo());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void configurarRecyclerViewDiasSemana() {
        recyclerViewDiasSemana = findViewById(R.id.recyclerViewdecomprables);
        recyclerViewDiasSemana.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        listaDiasSemana = new ArrayList<>();
        adaptadorDiasSemana = new MiAdaptadorDiasSemana(listaDiasSemana);

        recyclerViewDiasSemana.setAdapter(adaptadorDiasSemana);

        adaptadorDiasSemana.setOnItemClickListener(nombreDia -> {
            descargarComidas(nombreDia);
        });
    }

    private void inicializarTextViewsTotales() {
        textViewKcal = findViewById(R.id.textViewKcal);
        textViewProteina = findViewById(R.id.textViewProteinas);
        textViewGrasas = findViewById(R.id.textViewGrasas);
        textViewCarbohidratos = findViewById(R.id.textViewCarbohidratos);
        ProgressBar progressBar = findViewById(R.id.progressBarValoresNutricionales);
        textViewMaxKcal = findViewById(R.id.textViewMaxKcal);

        int maxProgress;

        // Establecer el máximo de calorías según el peso del usuario
        if (peso > 80) {
            maxProgress = 2000;
        } else if (peso > 70) {
            maxProgress = 2200;
        } else {
            maxProgress = 2500;
        }

        // Ajustar el máximo del ProgressBar según el género
        if (genero.equalsIgnoreCase("Hombre")) {
            maxProgress += 200;
        }

        // Ajustar el máximo del ProgressBar según la edad
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int edad = preferences.getInt("useredad", -1);

        if (edad != -1 && edad < 18) {
            maxProgress -= 200;
        }

        // Ajustar el máximo de calorías según el objetivo del usuario
        String objetivo = preferences.getString("objetivos", "Perder Peso");  // Predeterminado a "Perder Peso"
        if (objetivo.equals("Perder Peso")) {
            maxProgress -= 100;  // Restar 100 calorías para perder peso
        } else if (objetivo.equals("Ganar Peso")) {
            maxProgress += 200;  // Sumar 200 calorías para ganar peso
        }

        // Configurar el máximo del ProgressBar
        progressBar.setMax(maxProgress);
        textViewMaxKcal.setText(String.valueOf(maxProgress));

        // Configurar el listener para actualizar los totales
        adaptadorComida.setOnUpdateTotalsListener((totalKcal, totalProteinas, totalGrasas, totalCarbohidratos) -> {
            progressBar.setProgress(totalKcal);
            textViewKcal.setText("Kcal: " + totalKcal);
            textViewProteina.setText("P: " + totalProteinas);
            textViewGrasas.setText("G: " + totalGrasas);
            textViewCarbohidratos.setText("CBH: " + totalCarbohidratos);
        });
    }

    private void descargarDiasSemana() {

        API.getDiasSemanas(new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    List<DiasSemana> diasSemanaList = UtilJSONParser.parseArrayDiasSemana(r.content);

                    List<DiasSemana> diasFiltrados = new ArrayList<>();
                    for (DiasSemana dia : diasSemanaList) {
                        if (dia.getUsuarioId() == userId) {
                            diasFiltrados.add(dia);
                        }
                    }

                    primerDiaSemana = diasFiltrados.get(0).getId();

                    adaptadorDiasSemana.clear();
                    adaptadorDiasSemana.addAll(diasFiltrados);
                    adaptadorDiasSemana.notifyDataSetChanged();


                    if (diasFiltrados.size() > 0) {
                        descargarComidas(primerDiaSemana);
                    } else {
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

    private void descargarComidas(int diaDeLaSemana) {
        API.getComidasDeDiaSemana(diaDeLaSemana, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    diaSeleccionado = diaDeLaSemana;
                    List<Comidas> listaComidas = UtilJSONParser.parseComidas(r.content);

                    adaptadorComida.clear();
                    adaptadorComida.addAll(listaComidas);
                    adaptadorComida.notifyDataSetChanged();
                    adaptadorComida.calcularYNotificarTotales();

                } catch (Exception e) {
                    Log.e(getClass().getName(), "Error al analizar el contenido JSON de las comidas", e);
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
                Log.e(getClass().getName(), "Error al descargar las comidas para el día de la semana " + diaDeLaSemana + ": " + r.exception);
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
        final LottieAnimationView lotidieta = findViewById(R.id.toast_animationdieta);
        final ImageView cloudImageView = findViewById(R.id.cloudImageView);
        backgroundView.setVisibility(View.VISIBLE);
        textViewFirstTime.setVisibility(View.VISIBLE);
        lotidieta.setVisibility(View.VISIBLE);
        cloudImageView.setVisibility(View.VISIBLE);
        findViewById(android.R.id.content).setBackgroundColor(Color.BLACK);

        backgroundView.setOnClickListener(v -> {
            backgroundView.setVisibility(View.GONE);
            textViewFirstTime.setVisibility(View.GONE);
            lotidieta.setVisibility(View.GONE);
            cloudImageView.setVisibility(View.GONE);
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#ADD8E6"));

            // Marcar que ya se ha mostrado el mensaje inicial
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Dieta.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isFirstTimeDieta", false);
            editor.apply();

            // Descargar los días de la semana después de mostrar el mensaje inicial
            descargarDiasSemana();
        });
    }
}
