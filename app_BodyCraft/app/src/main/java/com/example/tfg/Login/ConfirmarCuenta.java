package com.example.tfg.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.tfg.Inicio;
import com.example.tfg.R;

public class ConfirmarCuenta extends AppCompatActivity {

    private TextView userDataText;
    private ImageView userImage;
    private Button continueButton;
    private String userGenero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_cuenta);

        // Inicializar vistas
        userDataText = findViewById(R.id.user_summary_text);
        userImage = findViewById(R.id.imagengenero);
        continueButton = findViewById(R.id.progress_button);

        // Obtener datos del usuario de las preferencias compartidas
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1);
        int userPeso = preferences.getInt("userpeso", -1);
        userGenero = preferences.getString("usergenero", "");
        int userEdad = preferences.getInt("useredad", -1);
        String userName = preferences.getString("username", "");

        // Mostrar los datos del usuario
        Log.d("genero: ", userGenero);
        String userData = getString(R.string.user_data_template, userName, userEdad, userPeso, userGenero);
        userDataText.setText(userData);

        // Establecer la imagen del usuario según el género
        if (userGenero.equalsIgnoreCase("Hombre")) {
            userImage.setImageResource(R.drawable.hombre);
        } else if (userGenero.equalsIgnoreCase("Mujer")) {
            userImage.setImageResource(R.drawable.mujer);
        }

        // Configurar OnClickListener para el botón "Continuar"
        continueButton.setOnClickListener(v -> {
            // Iniciar la actividad "Inicio"
            Intent intent = new Intent(ConfirmarCuenta.this, Inicio.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}
