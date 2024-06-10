package com.example.tfg.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.R;

public class NifNombreApellidosActivity extends AppCompatActivity {

    private EditText edadInput;
    private EditText nameInput;
    private Spinner generoSpinner;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nif_nombre_apellidos);

        // Inicializar los campos de entrada y el botón
        edadInput = findViewById(R.id.edad_input);
        nameInput = findViewById(R.id.name_input);
        generoSpinner = findViewById(R.id.genero_spinner);
        nextButton = findViewById(R.id.next_button);

        // Configurar el adaptador para el spinner de género
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generoSpinner.setAdapter(adapter);
        generoSpinner.setSelection(0);

        // Configurar el listener para el botón "next"
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los campos de entrada
                String edadText = edadInput.getText().toString().trim();
                String name = nameInput.getText().toString().trim();
                String genero = (String) generoSpinner.getSelectedItem();

                // Verificar que todos los campos no estén vacíos
                if (name.isEmpty() || genero.equals(getString(R.string.default_gender)) || edadText.isEmpty()) {
                    showCustomToast("Por favor, completa todos los campos y selecciona un género válido");
                    return;
                }

                // Convertir la edad a entero y manejar posibles errores de formato
                int edad;
                try {
                    edad = Integer.parseInt(edadText);
                } catch (NumberFormatException e) {
                    showCustomToast("Formato de edad inválido. Por favor, introduzca un número");
                    return;
                }

                // Obtener la ID del usuario del Intent recibido
                Intent intent = getIntent();
                int userId = intent.getIntExtra("userId", -1);

                // Crear un nuevo Intent para iniciar la siguiente actividad
                Intent nextIntent = new Intent(NifNombreApellidosActivity.this, DireccionUbicacionActivity.class);

                // Añadir la ID del usuario y los campos de entrada como extras al Intent
                nextIntent.putExtra("userId", userId);
                nextIntent.putExtra("age", edad);
                nextIntent.putExtra("name", name);
                nextIntent.putExtra("gender", genero);
                // Iniciar la siguiente actividad
                startActivity(nextIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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
