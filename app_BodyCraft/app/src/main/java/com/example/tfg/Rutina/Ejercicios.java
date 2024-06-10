package com.example.tfg.Rutina;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Ejercicio;

import java.util.ArrayList;
import java.util.List;

public class Ejercicios extends AppCompatActivity {

    private static List<Ejercicio> listaEjerciciosSemana;
    private int diaSeleccionado;
    private String diaNombre;
    private EditText searchEditText;
    private EjerciciosAdapter ejerciciosAdapter;
    private Handler handler;
    private boolean datosCargados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);
        ListView listView = findViewById(R.id.lista_ejercicios_semana);
        listView.setVisibility(View.GONE);
        listaEjerciciosSemana = new ArrayList<>();
        searchEditText = findViewById(R.id.search_edit_text);
        TextView diaDeLaSemanaTextView = findViewById(R.id.diadelasemanaseleccionado);

        // Obtener el día seleccionado desde el Intent
        diaSeleccionado = getIntent().getIntExtra("diarutinaseleccionado", -1);
        diaNombre = getIntent().getStringExtra("diaNombre");

        // Validar el día seleccionado
        if (diaSeleccionado == -1) {
            finish();
            return;
        }

        diaDeLaSemanaTextView.setText(diaNombre);

        // Configura el EditText de búsqueda con un TextWatcher
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No se necesita hacer nada aquí.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Realizar la búsqueda cuando el texto cambie
                buscarEjerciciosPorNombreParcial(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No se necesita hacer nada aquí.
            }
        });

        // Configura el botón de agregar ejercicio
        Button agregarEjercicioBtn = findViewById(R.id.agregar_ejercicio_btn);
        agregarEjercicioBtn.setOnClickListener(v -> {
            // Crea un Intent para iniciar la actividad SeccionEjercicios
            Intent intent = new Intent(Ejercicios.this, SeccionEjercicios.class);
            // Agrega el día seleccionado al Intent
            intent.putExtra("dia", diaSeleccionado);
            startActivity(intent);
        });

        // Configura el handler y muestra la animación de carga
        handler = new Handler();
        mostrarAnimacionCarga();

        // Inicia el temporizador para ocultar la animación de carga después de un mínimo de 2 segundos
        handler.postDelayed(() -> {
            if (datosCargados) {
                ocultarAnimacionCarga();
            }
        }, 2000);

        // Descarga la lista de ejercicios asociados al día de la semana seleccionado
        descargarEjerciciosSemana(diaSeleccionado);
    }

    // Método para mostrar la animación de carga
    private void mostrarAnimacionCarga() {
        findViewById(R.id.loading_container).setVisibility(View.VISIBLE);
        // También asegúrate de que el TextView de "Cargando..." esté visible.
    }

    // Método para ocultar la animación de carga
    private void ocultarAnimacionCarga() {
        findViewById(R.id.loading_container).setVisibility(View.GONE);
        findViewById(R.id.lista_ejercicios_semana).setVisibility(View.VISIBLE);
    }

    // Método para descargar los ejercicios asociados al día de la semana seleccionado desde la API
    private void descargarEjerciciosSemana(int diaSeleccionado) {
        API.getEjerciciosSemana(diaSeleccionado, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    listaEjerciciosSemana = UtilJSONParser.parseEjercicios(r.content);
                    mostrarListaEjerciciosSemana(R.id.lista_ejercicios_semana, listaEjerciciosSemana);
                } catch (Exception e) {
                } finally {
                    // Marcar los datos como cargados
                    datosCargados = true;

                    // Verificar si el temporizador ya ha terminado, si es así, ocultar la animación de carga
                    handler.post(() -> {
                        if (!handler.hasMessages(0)) {
                            ocultarAnimacionCarga();
                        }
                    });
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
                // Marcar los datos como cargados
                datosCargados = true;

                // Verificar si el temporizador ya ha terminado, si es así, ocultar la animación de carga
                handler.post(() -> {
                    if (!handler.hasMessages(0)) {
                        ocultarAnimacionCarga();
                    }
                });
            }
        });
    }

    // Método para mostrar la lista de ejercicios del día de la semana en un ListView
    private void mostrarListaEjerciciosSemana(int idListView, List<Ejercicio> listaEjercicios) {
        ListView listView = findViewById(idListView);
        ejerciciosAdapter = new EjerciciosAdapter(this, listaEjercicios, diaSeleccionado);
        listView.setAdapter(ejerciciosAdapter);
    }

    // Método para buscar ejercicios por nombre parcial y mostrar los resultados en el ListView
    private void buscarEjerciciosPorNombreParcial(String nombreParcial) {
        // Verifica si el nombre parcial es vacío
        if (nombreParcial == null || nombreParcial.trim().isEmpty()) {
            // Si está vacío, muestra la lista de ejercicios de la semana
            mostrarListaEjerciciosSemana(R.id.lista_ejercicios_semana, listaEjerciciosSemana);
            return;
        }

        // Mostrar la animación de carga
        mostrarAnimacionCarga();

        API.getEjerciciosPorNombreParcial(nombreParcial, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    List<Ejercicio> resultados = UtilJSONParser.parseEjercicios(r.content);
                    mostrarListaEjerciciosSemana(R.id.lista_ejercicios_semana, resultados);
                } catch (Exception e) {
                } finally {
                    // Ocultar la animación de carga
                    datosCargados = true;
                    ocultarAnimacionCarga();
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
                showCustomToast("No se ha encontrado ese ejercicio");
                // Ocultar la animación de carga
                ocultarAnimacionCarga();
            }
        });
    }

    // Adaptador personalizado para la lista de ejercicios
    private static class EjerciciosAdapter extends ArrayAdapter<Ejercicio> {
        private final int diaSeleccionado;

        public EjerciciosAdapter(Ejercicios context, List<Ejercicio> ejercicios, int diaSeleccionado) {
            super(context, 0, ejercicios);
            this.diaSeleccionado = diaSeleccionado;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            // Si la vista no se ha creado, infla el diseño y crea un ViewHolder
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ejercicio, parent, false);
                holder = new ViewHolder();
                holder.nombreTextView = convertView.findViewById(R.id.nombreTextView);
                holder.grupoMuscularTextView = convertView.findViewById(R.id.grupoMuscularTextView);
                holder.actionButton = convertView.findViewById(R.id.agregarButton);
                holder.videoView = convertView.findViewById(R.id.videoView);

                // Configura el VideoView
                MediaController mediaController = new MediaController(getContext());
                mediaController.setAnchorView(holder.videoView);
                holder.videoView.setMediaController(mediaController);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Obtén el ejercicio actual
            Ejercicio ejercicio = getItem(position);

            // Establece el nombre y el grupo muscular del ejercicio
            if (ejercicio != null) {
                holder.nombreTextView.setText(ejercicio.getNombre());
                holder.grupoMuscularTextView.setText(ejercicio.getGrupoMuscular());

                // Configura el VideoView
                holder.videoView.setVisibility(View.GONE); // Inicialmente oculta el VideoView

                holder.nombreTextView.setOnClickListener(v -> {
                    // Muestra el VideoView y comienza la reproducción del video
                    holder.videoView.setVisibility(View.VISIBLE);
                    Uri videoUri = Uri.parse(ejercicio.getUrlVideo());
                    holder.videoView.setVideoURI(videoUri);

                    holder.videoView.setOnPreparedListener(mp -> {
                        // Configura el volumen a cero y activa el bucle para la reproducción
                        mp.setVolume(0f, 0f);
                        mp.setLooping(true);
                        mp.start();
                    });

                    holder.videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
                        return true;
                    });
                });

                // Verifica si el ejercicio está en la lista de ejercicios de la semana
                boolean isEjercicioEnSemana = listaEjerciciosSemana.contains(ejercicio);

                // Configura el botón de acción según si el ejercicio está o no en la lista de ejercicios de la semana
                if (isEjercicioEnSemana) {
                    holder.actionButton.setText("Quitar");
                    holder.actionButton.setOnClickListener(v -> {
                        API.removeEjercicioFromDiaSemana(diaSeleccionado, ejercicio.getId(), new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response r) {
                                listaEjerciciosSemana.remove(ejercicio);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(UtilREST.Response r) {
                            }
                        });
                    });
                } else {
                    holder.actionButton.setText("Añadir");
                    holder.actionButton.setOnClickListener(v -> {
                        API.addEjercicioToDiaSemana(diaSeleccionado, ejercicio.getNombre(), new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response r) {
                                listaEjerciciosSemana.add(ejercicio);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(UtilREST.Response r) {
                            }
                        });
                    });
                }
            }

            return convertView;
        }

        // ViewHolder pattern para mejorar el rendimiento
        private static class ViewHolder {
            TextView nombreTextView;
            TextView grupoMuscularTextView;
            Button actionButton;
            VideoView videoView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        descargarEjerciciosSemana(diaSeleccionado);
    }
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_text));

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
