package com.example.tfg;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UltimaActividad extends AppCompatActivity {

    private EditText editTextPeso, editTextAltura, editTextEdad;
    private RadioGroup radioGroupSexo;
    private Spinner spinnerActividadFisica;
    private TextView textViewResultadoIMC, textViewResultadoTMB, textViewResultadoRequerimiento;
    private Button buttonCalcularIMC, buttonCalcularTMB, buttonCalcularRequerimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultima_actividad);

        // Referencias a los elementos de la interfaz
        editTextPeso = findViewById(R.id.editTextPeso);
        editTextAltura = findViewById(R.id.editTextAltura);
        editTextEdad = findViewById(R.id.editTextEdad);
        radioGroupSexo = findViewById(R.id.radioGroupSexo);
        spinnerActividadFisica = findViewById(R.id.spinnerActividadFisica);
        textViewResultadoIMC = findViewById(R.id.textViewResultadoIMC);
        textViewResultadoTMB = findViewById(R.id.textViewResultadoTMB);
        textViewResultadoRequerimiento = findViewById(R.id.textViewResultadoRequerimiento);
        buttonCalcularIMC = findViewById(R.id.buttonCalcularIMC);
        buttonCalcularTMB = findViewById(R.id.buttonCalcularTMB);
        buttonCalcularRequerimiento = findViewById(R.id.buttonCalcularRequerimiento);

        // Configura el spinner con las opciones de actividad física
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.actividad_fisica,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActividadFisica.setAdapter(adapter);

        // Configura los botones con sus acciones
        buttonCalcularIMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularIMC();
            }
        });

        buttonCalcularTMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularTMB();
            }
        });

        buttonCalcularRequerimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularRequerimientoCalorico();
            }
        });
    }

    private void calcularIMC() {
        try {
            double peso = Double.parseDouble(editTextPeso.getText().toString());
            double altura = Double.parseDouble(editTextAltura.getText().toString());

            double imc = peso / (altura * altura);
            String resultadoIMC = String.format("IMC: %.2f", imc);
            textViewResultadoIMC.setText(resultadoIMC);
        } catch (NumberFormatException e) {
            textViewResultadoIMC.setText("Por favor, ingrese valores válidos.");
        }
    }

    private void calcularTMB() {
        try {
            double peso = Double.parseDouble(editTextPeso.getText().toString());
            double altura = Double.parseDouble(editTextAltura.getText().toString());
            int edad = Integer.parseInt(editTextEdad.getText().toString());
            int idSexo = radioGroupSexo.getCheckedRadioButtonId();

            double tmb;
            if (idSexo == R.id.radioHombre) {
                tmb = 10 * peso + 6.25 * altura - 5 * edad + 5;
            } else {
                tmb = 10 * peso + 6.25 * altura - 5 * edad - 161;
            }

            String resultadoTMB = String.format("TMB: %.2f", tmb);
            textViewResultadoTMB.setText(resultadoTMB);
        } catch (NumberFormatException e) {
            textViewResultadoTMB.setText("Por favor, ingrese valores válidos.");
        }
    }

    private void calcularRequerimientoCalorico() {
        try {
            double tmb = Double.parseDouble(textViewResultadoTMB.getText().toString().replace("TMB: ", ""));
            int nivelActividadFisica = spinnerActividadFisica.getSelectedItemPosition();

            double factorActividad;
            switch (nivelActividadFisica) {
                case 0:
                    factorActividad = 1.2; // Sedentario
                    break;
                case 1:
                    factorActividad = 1.375; // Ligeramente activo
                    break;
                case 2:
                    factorActividad = 1.55; // Moderadamente activo
                    break;
                case 3:
                    factorActividad = 1.725; // Muy activo
                    break;
                case 4:
                    factorActividad = 1.9; // Extremadamente activo
                    break;
                default:
                    factorActividad = 1.2;
            }

            double requerimientoCalorico = tmb * factorActividad;
            String resultadoRequerimiento = String.format("Requerimiento calórico diario: %.2f kcal", requerimientoCalorico);
            textViewResultadoRequerimiento.setText(resultadoRequerimiento);
        } catch (NumberFormatException e) {
            textViewResultadoRequerimiento.setText("Por favor, asegúrese de haber calculado primero la TMB.");
        }
    }
}