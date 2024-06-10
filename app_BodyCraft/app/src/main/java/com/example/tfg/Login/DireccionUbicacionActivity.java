package com.example.tfg.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Comidas;
import com.example.tfg.domain.DiasSemana;
import com.example.tfg.domain.Ejercicio;
import com.example.tfg.domain.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DireccionUbicacionActivity extends AppCompatActivity {

    private EditText pesoInput;
    private EditText diasRutinaInput;
    private EditText numComidasInput;
    private Button nextButton;
    private Usuario usuario;
    private String genero;
    int diasRutina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direccion_ubicacion);

        // Inicializar los campos de entrada y el botón
        pesoInput = findViewById(R.id.peso_input);
        diasRutinaInput = findViewById(R.id.diasrutina_input);
        numComidasInput = findViewById(R.id.numcomidas_input);
        nextButton = findViewById(R.id.next_button);

        // Obtener datos del intent
        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        int age = intent.getIntExtra("age", -1);
        String name = intent.getStringExtra("name");
        genero = intent.getStringExtra("gender");

        // Configurar el listener para el botón "next"
        nextButton.setOnClickListener(v -> {
            try {
                double peso = Double.parseDouble(pesoInput.getText().toString().trim());
                diasRutina = Integer.parseInt(diasRutinaInput.getText().toString().trim());
                int numComidas = Integer.parseInt(numComidasInput.getText().toString().trim());

                if (diasRutina <= 0 || numComidas <= 0 || peso <= 0) {
                    showCustomToast("Ingrese valores válidos para peso, días de rutina y número de comidas");
                    return;
                }

                // Crear un objeto Usuario con los datos obtenidos
                usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setPassword(password);
                usuario.setAge(age);
                usuario.setName(name);
                usuario.setPeso(peso);
                usuario.setDiasquevoy(diasRutina);
                usuario.setComidas(numComidas);
                usuario.setGenero(genero);
                usuario.setDiasSemanaIds(new ArrayList<>());

                // Crear los días de la semana para el usuario
                crearDiasSemanaParaUsuario(userId, new UtilREST.OnResponseListener() {
                    @Override
                    public void onSuccess(UtilREST.Response response) {
                        // Después de crear los días de la semana, obtén los IDs de los días de la semana creados
                        List<Integer> diasSemanaIds = usuario.getDiasSemanaIds();
                        Log.d("DireccionUbicacion", "diasSemanaIds: " + diasSemanaIds);

                        // Actualiza el usuario con la lista de IDs de los días de la semana
                        usuario.setDiasSemanaIds(diasSemanaIds);

                        // Llamar a la API para actualizar los datos del usuario
                        API.updateUser(userId, usuario, new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response response) {
                                Log.d("DireccionUbicacion", "Usuario actualizado con éxito: " + response.content);

                                // Guardar el ID del usuario en las preferencias compartidas
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DireccionUbicacionActivity.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("userId", userId);
                                editor.putInt("userpeso", (int) peso);
                                editor.putString("usergenero", usuario.getGenero());
                                editor.putInt("useredad", age);
                                editor.putString("username", usuario.getName());
                                editor.apply();

                                // Crear las comidas para cada día
                                crearComidasParaSemana(userId, numComidas, diasSemanaIds);

                                Intent intent = new Intent(DireccionUbicacionActivity.this, ConfirmarCuenta.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }

                            @Override
                            public void onError(UtilREST.Response response) {
                                Log.e("DireccionUbicacion", "Error al actualizar el usuario: " + response.content);
                            }
                        });
                    }

                    @Override
                    public void onError(UtilREST.Response response) {
                        Log.e("DiasSemana", "Error al crear los días de la semana: " + response.content);
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(DireccionUbicacionActivity.this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para crear los días de la semana asociados al usuario
    private void crearDiasSemanaParaUsuario(int userId, UtilREST.OnResponseListener listener) {
        // Determinar los días de la semana que se deben crear según el número de días de rutina especificados por el usuario
        String[] diasSemana;

        switch (diasRutina) {
            case 1:
                diasSemana = new String[] {"Miércoles"};
                break;
            case 2:
                diasSemana = new String[] {"Lunes", "Jueves"};
                break;
            case 3:
                diasSemana = new String[] {"Lunes", "Miércoles", "Viernes"};
                break;
            case 4:
                diasSemana = new String[] {"Lunes", "Martes", "Miércoles", "Viernes"};
                break;
            case 5:
                diasSemana = new String[] {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
                break;
            case 6:
                diasSemana = new String[] {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
                break;
            case 7:
                diasSemana = new String[] {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
                break;
            default:
                diasSemana = new String[] {}; // Manejar el caso en que el número de días de rutina no sea válido
                break;
        }

        // Lista de IDs de los días de la semana creados
        List<Integer> diasSemanaIds = new ArrayList<>();

        // Iterar a través de cada día de la semana que se debe crear
        for (String dia : diasSemana) {
            // Crear listas vacías de ejercicios y comidas
            List<Ejercicio> ejerciciosVacios = new ArrayList<>();
            List<Comidas> comidasVacias = new ArrayList<>();

            // Crear un objeto DiasSemana con listas vacías de ejercicios y comidas
            DiasSemana nuevoDia = new DiasSemana(dia, "ruta_a_la_imagen", comidasVacias, ejerciciosVacios, userId);

            // Realizar la llamada a la API para crear el día de la semana y asociarlo al usuario
            API.crearDiaSemana(nuevoDia, userId, new UtilREST.OnResponseListener() {
                @Override
                public void onSuccess(UtilREST.Response response) {
                    Log.d("DiasSemana", "Día de la semana creado con éxito: " + response.content);
                    int diaId = UtilJSONParser.parseDiaIdFromResponse(response.content);
                    diasSemanaIds.add(diaId);

                    // Si se ha creado todos los días de la semana especificados, llamar al listener
                    if (diasSemanaIds.size() == diasRutina) {
                        usuario.setDiasSemanaIds(diasSemanaIds);
                        listener.onSuccess(response);
                    }
                }

                @Override
                public void onError(UtilREST.Response response) {
                    Log.e("DiasSemana", "Error al crear el día de la semana: " + response.content);
                }
            });
        }
    }


    // Método para crear las comidas según el número de comidas especificado por el usuario para cada día de la semana
    private void crearComidasParaSemana(int userId, int numComidas, List<Integer> diasIds) {
        // Lista de los nombres de las comidas según el número de comidas especificado
        List<List<String>> comidasPorCantidad = new ArrayList<>();
        comidasPorCantidad.add(Arrays.asList("Comida")); // Si numComidas es 1
        comidasPorCantidad.add(Arrays.asList("Comida", "Cena")); // Si numComidas es 2
        comidasPorCantidad.add(Arrays.asList("Desayuno", "Comida", "Cena")); // Si numComidas es 3
        comidasPorCantidad.add(Arrays.asList("Desayuno", "Comida", "Merienda", "Cena")); // Si numComidas es 4
        comidasPorCantidad.add(Arrays.asList("Desayuno", "Almuerzo", "Comida", "Merienda", "Cena")); // Si numComidas es 5
        comidasPorCantidad.add(Arrays.asList("Desayuno", "Almuerzo", "Comida", "Merienda", "Cena", "Recena")); // Si numComidas es 6

        // Asegúrate de que numComidas sea un número válido (entre 1 y 6)
        if (numComidas < 1 || numComidas > 6) {
            Toast.makeText(DireccionUbicacionActivity.this, "Número de comidas no válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtén la lista de nombres de comidas para el número de comidas especificado
        List<String> nombresComidas = comidasPorCantidad.get(numComidas - 1);

        // Iterar a través de cada día de la semana
        for (int diaId : diasIds) {
            // Crear las comidas según el número de comidas especificado para cada día
            for (int i = 0; i < numComidas; i++) {
                // Crear un objeto Comidas
                Comidas comida = new Comidas();
                comida.setDiaSemana(diaId);

                // Asignar propiedades de la comida
                // Asigna el título de la comida desde la lista de nombres de comidas
                comida.setTitulo(nombresComidas.get(i));

                // Puedes ajustar la imagen según tus necesidades
                comida.setPic("ruta_a_la_imagen");

                // Realizar la llamada a la API para crear la comida
                API.crearComidas(Arrays.asList(comida), diaId, new UtilREST.OnResponseListener() {
                    @Override
                    public void onSuccess(UtilREST.Response response) {
                        Log.d("Comida", "Comida creada con éxito: " + response.content);
                    }

                    @Override
                    public void onError(UtilREST.Response response) {
                        Log.e("Comida", "Error al crear la comida: " + response.content);
                    }
                });
            }
        }
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
