package com.example.tfg.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Inicio;
import com.example.tfg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TienesCuenta extends AppCompatActivity {

    private static final int IMAGE_PICK_REQUEST_CODE = 20;
    private EditText usernameInput;
    private EditText passwordInput;
    private TextView olvidasteContraseña;
    private Button inicioSesion;
    private TextView crearCuentaLogin;
    private ImageView userImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienes_cuenta);

        // Inicializar los campos de entrada y el botón
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        olvidasteContraseña = findViewById(R.id.forgot_password);
        inicioSesion = findViewById(R.id.login_btn);
        crearCuentaLogin = findViewById(R.id.create_account_login);
        userImageView = findViewById(R.id.registration_image);

        crearCuentaLogin.setOnClickListener(v -> {
            Intent intent = new Intent(TienesCuenta.this, InicioSesion.class);
            startActivity(intent);
        });
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
        // Configurar el listener para el TextView "olvidasteContraseña"
        olvidasteContraseña.setOnClickListener(v -> {
            // Obtener el correo electrónico del usuario desde el TextView
            String userEmail = usernameInput.getText().toString().trim();

            // Verificar si el correo electrónico está vacío
            if (userEmail.isEmpty()) {
                Toast.makeText(TienesCuenta.this, "Por favor, ingresa un correo electrónico", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el ID del usuario mediante el correo electrónico
            API.getUserIdByEmail(userEmail, new UtilREST.OnResponseListener() {
                @Override
                public void onSuccess(UtilREST.Response response) {
                    try {
                        // Parsear la respuesta para obtener el ID del usuario
                        JSONObject jsonResponse = new JSONObject(response.content);
                        int userId = jsonResponse.getInt("id");

                        // Obtener los detalles del usuario (incluida su contraseña) mediante su ID
                        API.getUserById(userId, new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response response) {
                                try {
                                    // Parsear la respuesta para obtener la contraseña del usuario
                                    JSONObject jsonUser = new JSONObject(response.content);
                                    String userPassword = jsonUser.getString("password");

                                    // Enviar un correo electrónico con la contraseña al usuario
                                    enviarCorreoElectronico(userEmail, userPassword);
                                } catch (JSONException e) {
                                    Log.e("API", "Error al parsear la respuesta de usuario por ID: " + e.getMessage());
                                    showCustomToast("Credenciales Incorrectas");
                                }
                            }

                            @Override
                            public void onError(UtilREST.Response response) {
                                Log.e("API", "Error al obtener el usuario por ID: " + response.content);
                                showCustomToast("Credenciales Incorrectas");
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("API", "Error al parsear la respuesta de ID de usuario por email: " + e.getMessage());
                        showCustomToast("Credenciales Incorrectas");
                    }
                }

                @Override
                public void onError(UtilREST.Response response) {
                    Log.e("API", "Error al obtener el ID del usuario por email: " + response.content);
                    showCustomToast("Credenciales Incorrectas");
                }
            });
        });


        inicioSesion.setOnClickListener(v -> {
            // Obtener el correo electrónico y la contraseña ingresados por el usuario
            String email = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Verificar si el correo electrónico o la contraseña están vacíos
            if (email.isEmpty() || password.isEmpty()) {
                showCustomToast("No puede haber campos vacios");
                return;
            }

            // Llamar al método iniciarSesion de la clase API
            API.iniciarSesion(email, password, new UtilREST.OnResponseListener() {
                @Override
                public void onSuccess(UtilREST.Response response) {
                    // Manejar la respuesta de la API
                    try {
                        // Obtener el contenido de la respuesta
                        String responseContent = response.content;

                        // Dado que la respuesta es un token JWT, guárdalo como string
                        String jwtToken = responseContent;

                        // Puedes guardar el token JWT para usarlo posteriormente
                        // Por ejemplo, en SharedPreferences para futuras solicitudes
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("jwtToken", jwtToken);
                        editor.apply();

                        // Luego puedes usar el token JWT según sea necesario
                        // Por ejemplo, para autenticación en otras solicitudes API

                        // Inicia la actividad Inicio
                        Intent intent = new Intent(TienesCuenta.this, Inicio.class);
                        startActivity(intent);

                    } catch (Exception e) {
                        Log.e("API", "Error al manejar la respuesta de inicio de sesión: " + e.getMessage());
                        showCustomToast("Credenciales Incorrectas");
                    }
                }

                @Override
                public void onError(UtilREST.Response response) {
                    // Manejar errores en la solicitud de inicio de sesión
                    Log.e("API", "Error en la solicitud de inicio de sesión: " + response.content);
                    showCustomToast("Credenciales Incorrectas");
                }
            });

        });
    }

    public void openYoutube(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
        startActivity(intent);
    }

    public void openGithub(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/"));
        startActivity(intent);
    }
    // Función para enviar un correo electrónico
    @SuppressLint("StaticFieldLeak")
    private void enviarCorreoElectronico(String toEmail, String password) {
        // Usa AsyncTask para realizar la operación de red en segundo plano
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    // Configurar propiedades de correo
                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");

                    // Crear una sesión con autenticación
                    Session session = Session.getInstance(props, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            // Cambia por tus credenciales (email y contraseña) de Gmail
                            return new PasswordAuthentication("comando1.yt@gmail.com", "dgki oqev aqxs vrce");
                        }
                    });

                    // Crear un mensaje
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("comando1.yt@gmail.com")); // Cambia por tu dirección de correo
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                    message.setSubject("Recuperación de contraseña");
                    message.setText("Tu contraseña es: " + password);

                    // Enviar el mensaje
                    Transport.send(message);
                    return true;
                } catch (MessagingException e) {
                    // Maneja errores al enviar el correo
                    Log.e("Email", "Error al enviar el correo electrónico: " + e.getMessage());
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success) {
                // Esto se ejecuta en el hilo principal después de la operación
                if (success) {
                    showCustomToast("Correo electrónico enviado");
                } else {
                    showCustomToast("Error al enviar el correo electrónico");
                }
            }
        }.execute();
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
