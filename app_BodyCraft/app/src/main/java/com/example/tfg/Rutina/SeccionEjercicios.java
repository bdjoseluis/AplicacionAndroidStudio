package com.example.tfg.Rutina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.tfg.R;

public class SeccionEjercicios extends AppCompatActivity {

    private int dia; // Puedes usar un identificador para el día de la semana si es necesario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_ejercicios);

        // Obtener el día de la semana desde el intent
        dia = getIntent().getIntExtra("dia", -1);

        // Configurar `CardView` para iniciar `VerEjercicio`
        configurarCardView(R.id.brazosCard, "Brazos");
        configurarCardView(R.id.pechoCard, "Pecho");
        configurarCardView(R.id.espaldaCard, "Espalda");
        configurarCardView(R.id.piernasCard, "Piernas");
        configurarCardView(R.id.hombrosCard, "Hombros");
        configurarCardView(R.id.abdomenCard, "Abdomen");
    }

    private void configurarCardView(int cardViewId, String categoria) {
        CardView cardView = findViewById(cardViewId);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la actividad `VerEjercicio`
                Intent intent = new Intent(SeccionEjercicios.this, VerEjercicio.class);
                // Pasar el día de la semana y la categoría de ejercicios a la actividad `VerEjercicio`
                intent.putExtra("dia", dia);
                intent.putExtra("categoria", categoria);
                startActivity(intent);
            }
        });
    }
}
