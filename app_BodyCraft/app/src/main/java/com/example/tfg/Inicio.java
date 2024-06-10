package com.example.tfg;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.TrendAdaptador;
import com.example.tfg.Compra.Compra;
import com.example.tfg.Dieta.Dieta;
import com.example.tfg.Login.Perfil;
import com.example.tfg.Notificaciones.DailyNotificationWork;
import com.example.tfg.Rutina.Rutina;
import com.example.tfg.domain.trending;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Inicio extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout inicio, dieta, rutina, ajustes, compras;
    private ImageView imagenPerfil;
    private LinearLayout layoutaccesoSpotify;
    private LinearLayout chatgptinicio;
    private LinearLayout ultimaactividad;
    private LottieAnimationView inicioNotificaciones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        // Obtener las preferencias compartidas y el correo electrónico del usuario
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1);

        Log.d("MisLogs", "onCreate: userId from preferences: " + userId);
        inicioNotificaciones = findViewById(R.id.inicioNotificaciones);
        // Referencia a la imagen de perfil en la interfaz de usuario
        imagenPerfil = findViewById(R.id.imagenInicioPerfil);

        layoutaccesoSpotify = findViewById(R.id.layoutaccesoSpotify);
        layoutaccesoSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, Spotify.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        chatgptinicio = findViewById(R.id.chatgptinicio);
        chatgptinicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, ChatGpt.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ultimaactividad = findViewById(R.id.ultimaactividad);
        ultimaactividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Inicio.this, UltimaActividad.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Realiza la consulta para obtener el usuario por ID
        API.getUserById(userId, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response response) {
                try {
                    // Parsear la respuesta de la API para obtener los datos del usuario
                    JSONObject jsonResponse = new JSONObject(response.content);
                    String userName = jsonResponse.optString("name", "");
                    TextView nombre = findViewById(R.id.tituloinicioperfil);
                    nombre.setText("Hola " + userName.toString());

                    // Obtener la URL de la imagen de perfil del usuario
                    String userProfileImageUrl = jsonResponse.optString("image", "");
                    Log.d(TAG, "Cargando imagen de perfil desde URL: " + userProfileImageUrl);

                    // Cargar la imagen de perfil con Glide
                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.profile);

                    Glide.with(Inicio.this)
                            .load(userProfileImageUrl)
                            .apply(requestOptions)
                            .circleCrop()
                            .into(imagenPerfil);



                } catch (JSONException e) {
                    Log.e(TAG, "Error al parsear la respuesta de la API", e);
                }
            }

            @Override
            public void onError(UtilREST.Response response) {
                Log.e(TAG, "Error al obtener el usuario por ID: " + response.content);
            }
        });


        inicioNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el estado actual de las notificaciones desde las preferencias
                boolean notificacionesActivas = preferences.getBoolean("notificaciones", false);

                // Invierte el estado de las notificaciones
                boolean nuevoEstadoNotificaciones = !notificacionesActivas;

                // Almacena el nuevo estado en las preferencias compartidas
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("notificaciones", nuevoEstadoNotificaciones);
                editor.apply();

                // Muestra un mensaje de confirmación indicando si las notificaciones se activaron o desactivaron
                if (nuevoEstadoNotificaciones) {
                    showCustomToast("Notificaciones activadas");

                    inicioNotificaciones.playAnimation();
                } else {
                    showCustomToast("Notificaciones desactivadas");

                    inicioNotificaciones.pauseAnimation();
                }
            }
        });
        iniciarRecyclerView();

        setupListeners();

        imagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad de Perfil
                Intent intent = new Intent(Inicio.this, Perfil.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        scheduleDailyNotificationWork();

    }

    private void iniciarRecyclerView() {
        ArrayList<trending> items = new ArrayList<>();

        items.add(new trending("Prueba nuestra IA", "Nueva IA implementada en la aplicacion", "trends"));
        items.add(new trending("Nuestra nueva aplicacion", "Acceso anticipado", "trends2"));
        items.add(new trending("Future in AI", "The National", "trends"));

        recyclerView= findViewById(R.id.reciclerprincipal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapter = new TrendAdaptador(items);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        // Inicializa los botones
        inicio = findViewById(R.id.botoninicio);
        dieta = findViewById(R.id.botondieta);
        rutina = findViewById(R.id.botonrutina);
        ajustes = findViewById(R.id.botonajustes);
        compras = findViewById(R.id.botontienda);

        // Configura listeners para los botones
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        dieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad de Dieta
                Intent intent = new Intent(Inicio.this, Dieta.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        rutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad de Rutina
                Intent intent = new Intent(Inicio.this, Rutina.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad de Ajustes
                Intent intent = new Intent(Inicio.this, Ajustes.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inicia la actividad de Compra
                Intent intent = new Intent(Inicio.this, Compra.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    public void scheduleDailyNotificationWork() {
        // Crear una solicitud de trabajo periódico para ejecutarse diariamente
        PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(DailyNotificationWork.class, 1, TimeUnit.DAYS)
                .build();

        // Programar el trabajo diario utilizando WorkManager
        WorkManager workManager = WorkManager.getInstance(this);
        workManager.enqueue(dailyWorkRequest);
    }
    @Override
    public void onBackPressed() {
        // Finaliza la actividad actual y cierra la aplicación
        super.onBackPressed();
        finishAffinity();
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