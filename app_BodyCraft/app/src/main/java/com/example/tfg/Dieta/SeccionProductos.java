package com.example.tfg.Dieta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.tfg.R;

public class SeccionProductos extends AppCompatActivity {

    private int comidaId;
    private static final int REQUEST_CODE_ADD_PRODUCTOS = 100; // Un código único para identificar la actividad de añadir productos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_productos);

        // Obtener la ID de la comida desde el intent
        comidaId = getIntent().getIntExtra("comida_id", -1);

        // Configurar CardViews para iniciar VerProducto
        configurarCardView(R.id.bebidasCard, "Bebidas");
        configurarCardView(R.id.suplementosCard, "Suplementos");
        configurarCardView(R.id.snacksCard, "Frutas, Verduras y Snacks");
        configurarCardView(R.id.lacteosCard, "Lácteos y Derivados");
        configurarCardView(R.id.congeladosCard, "Carnes y Pescados");
        configurarCardView(R.id.conservasCard, "Granos y Cereales");
    }

    private void configurarCardView(int cardViewId, String categoria) {
        CardView cardView = findViewById(cardViewId);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeccionProductos.this, VerProducto.class);
                intent.putExtra("comida_id", comidaId);
                intent.putExtra("categoria", categoria); // Pasa la categoría a la actividad VerProducto
                startActivity(intent);
            }
        });
    }

}
