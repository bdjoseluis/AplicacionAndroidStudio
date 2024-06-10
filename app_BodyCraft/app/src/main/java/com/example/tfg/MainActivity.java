package com.example.tfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tfg.Login.InicioSesion;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout startApp;
    private SharedPreferences preferences;
    private LottieAnimationView lottieAnimationView;  // Define LottieAnimationView
    private LottieAnimationView lottieAnimationView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startApp = findViewById(R.id.startButon);
        preferences = getSharedPreferences("com.example.tfg", MODE_PRIVATE);

        // Encuentra LottieAnimationView en el diseño
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        lottieAnimationView5 = findViewById(R.id.lottieAnimationView5);

        // Configura LottieAnimationView (la animación debería comenzar automáticamente si has definido app:lottie_autoPlay="true" en XML)
        // Puedes configurar otras propiedades aquí si es necesario

        // Verifica si es la primera vez que se inicia la aplicación
        boolean isFirstTime = preferences.getBoolean("first_time_user", true);

        startApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (isFirstTime) {
                    // Si es la primera vez que se inicia la aplicación, mostrar Registro
                    intent = new Intent(MainActivity.this, InicioSesion.class);
                } else {
                    // Si ya se ha iniciado la aplicación al menos una vez, mostrar Inicio
                    intent = new Intent(MainActivity.this, Inicio.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        // Establecer OnClickListener al ConstraintLayout para detectar toques en la pantalla
        lottieAnimationView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la animación de LottieAnimationView5
                lottieAnimationView5.playAnimation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar la preferencia después de la primera ejecución
        if (preferences.getBoolean("first_time_user", true)) {
            // Cambiar el valor a false ya que la aplicación ya se inició una vez
            preferences.edit().putBoolean("first_time_user", false).apply();
        }
    }
}