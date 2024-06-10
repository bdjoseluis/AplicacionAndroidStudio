package com.example.tfg.Login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Role;
import com.example.tfg.domain.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class InicioSesion extends AppCompatActivity {

    private static final int IMAGE_PICK_REQUEST_CODE = 20;
    private EditText usernameInput;
    private EditText passwordInput;
    private Button registerButton;
    private ImageView userImageView;
    private Uri selectedImageUri;

    private TextView yatienescuenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        // Inicializar los campos de entrada y el botón
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        registerButton = findViewById(R.id.login_btn);
        userImageView = findViewById(R.id.imagenRegistro);  // Asumiendo que has agregado un ImageView con este ID
        yatienescuenta = findViewById(R.id.yatienescuenta);
// En tu método onCreate() o onCreateView() o donde sea adecuado
        ImageView youtubeImageView = findViewById(R.id.youtube);
        ImageView githubImageView = findViewById(R.id.github);


        Glide.with(this)
                .load(R.drawable.rocket) // Reemplaza "github_gif" por el nombre de tu GIF en la carpeta res/drawable
                .into(githubImageView);

// Configurar el listener para el ImageView
        userImageView.setOnClickListener(v -> {
            // Crear un intent para seleccionar una imagen de la galería
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_REQUEST_CODE);
        });

        yatienescuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioSesion.this, TienesCuenta.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        // Configurar el listener para el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados por el usuario
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if (!isValidEmail(username)) {
                    // Mostrar error si el correo electrónico no es válido
                    usernameInput.setError("Introduce un correo electrónico válido");
                    return;
                }
                if (!isValidPassword(password)) {
                    // Mostrar error si la contraseña no tiene la longitud mínima requerida
                    passwordInput.setError("La contraseña debe tener al menos 8 caracteres");
                    return;
                }

                // Crear un objeto Usuario con los datos ingresados
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setUsername(username);
                nuevoUsuario.setPassword(password);
                nuevoUsuario.setEmail(username);
                nuevoUsuario.setImage(selectedImageUri != null ? selectedImageUri.toString() : null);
                // Configurar otros datos para el usuario
                configurarDatosUsuario(nuevoUsuario);

                // Registrar el usuario con la API
                API.registerUser(nuevoUsuario, new UtilREST.OnResponseListener() {
                    @Override
                    public void onSuccess(UtilREST.Response response) {

                        // Buscar el usuario por correo electrónico para obtener la ID
                        API.getUserIdByEmail(usernameInput.getText().toString().trim(), new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response response) {
                                // Analizar el contenido de la respuesta
                                try {
                                    JSONObject jsonResponse = new JSONObject(response.content);
                                    int userId = jsonResponse.getInt("id");

                                    // Crear un Intent para iniciar la actividad NifNombreApellidosActivity
                                    Intent intent = new Intent(InicioSesion.this, NifNombreApellidosActivity.class);

                                    // Pasar la ID del usuario a la siguiente actividad
                                    intent.putExtra("userId", userId);

                                    // Iniciar la nueva actividad
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                } catch (JSONException e) {
                                    showCustomToast("Introduce credenciales validas");
                                }
                            }

                            @Override
                            public void onError(UtilREST.Response response) {
                                showCustomToast("Introduce credenciales validas");
                            }
                        });
                    }

                    @Override
                    public void onError(UtilREST.Response response) {
                        showCustomToast("Introduce credenciales validas");
                    }
                });
            }
        });
    }

    // Función para configurar otros datos para el usuario
    private void configurarDatosUsuario(Usuario usuario) {
        usuario.setGenero("");
        usuario.setName("");
        usuario.setComidas(5);
        usuario.setDiasquevoy(5);
        usuario.setPeso(70);
        usuario.setAge(18);

        // Configurar el rol de usuario
        Role userRole = Role.USUARIO;
        usuario.setRoles(Arrays.asList(userRole));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Obtener el URI de la imagen seleccionada
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Asignar la imagen seleccionada al ImageView
                userImageView.setImageURI(selectedImageUri);
                this.selectedImageUri = selectedImageUri;

            } else {
            }
        }
    }
    public void openYoutube(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
        startActivity(intent);
    }

    public void openGithub(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/"));
        startActivity(intent);
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
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}
