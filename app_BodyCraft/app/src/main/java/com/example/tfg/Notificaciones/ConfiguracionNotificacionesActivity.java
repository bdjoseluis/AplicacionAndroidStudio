package com.example.tfg.Notificaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.tfg.R;
import com.example.tfg.fragments.AjustesFragment;

public class ConfiguracionNotificacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_notificaciones);

        // Verifica si hay un estado guardado (savedInstanceState)
        // Si no, inicia el fragmento AjustesFragment
        if (savedInstanceState == null) {
            // Crear una nueva instancia de AjustesFragment y cargarla en el contenedor
            Fragment fragment = new AjustesFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.configuracion_notificaciones_container, fragment)
                    .commit();
        }
    }
}