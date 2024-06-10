package com.example.tfg.Login;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilREST;
import com.example.tfg.CalendarActivity;
import com.example.tfg.Comunidad;
import com.example.tfg.GalleryActivity;
import com.example.tfg.Inicio;
import com.example.tfg.Notificaciones.ConfiguracionNotificacionesActivity;
import com.example.tfg.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Perfil extends AppCompatActivity {

    private ConstraintLayout layoutconfigNotificaciones;
    private ConstraintLayout calendariolayout;
    private ConstraintLayout constraintLayoutatrasperfil;
    private ConstraintLayout cerrarSesion;
    private ConstraintLayout galeriainicio;
    private ConstraintLayout playlistperfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pefil);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int userId = preferences.getInt("userId", -1); // Devuelve -1 si no se encuentra la userId

        constraintLayoutatrasperfil = findViewById(R.id.constraintLayoutatrasperfil);
        constraintLayoutatrasperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Perfil.this, Inicio.class);
                startActivity(intent);
            }
        });

        cerrarSesion = findViewById(R.id.cerrarSesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Perfil.this, TienesCuenta.class);
                startActivity(intent);
            }
        });
        layoutconfigNotificaciones = findViewById(R.id.layoutconfigNotificaciones);
        layoutconfigNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, ConfiguracionNotificacionesActivity.class);
                startActivity(intent);
            }
        });

        galeriainicio = findViewById(R.id.galeriainicio);
        galeriainicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, GalleryActivity.class);
                startActivity(intent);
            }
        });

        playlistperfil = findViewById(R.id.playlistperfil);
        playlistperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, Comunidad.class);
                startActivity(intent);
            }
        });

        calendariolayout = findViewById(R.id.calendariolayout);
        calendariolayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        // Referencia a los componentes de la interfaz de usuario
        TextView nombreTextView = findViewById(R.id.textoDelNombre);
        ImageView imagenPerfilImageView = findViewById(R.id.imagenPerfil);

        API.getUserById(userId, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response response) {
                try {
                    // Parsear la respuesta de la API para obtener los datos del usuario
                    JSONObject jsonResponse = new JSONObject(response.content);
                    String userName = jsonResponse.optString("name", "");
                    nombreTextView.setText("Hola " + userName.toString());

                    // Obtener la URL de la imagen de perfil del usuario
                    String userProfileImageUrl = jsonResponse.optString("image", "");
                    Log.d(TAG, "Cargando imagen de perfil desde URL: " + userProfileImageUrl);

                    // Cargar la imagen de perfil con Glide
                    RequestOptions requestOptions = new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.profile);

                    Glide.with(Perfil.this)
                            .load(userProfileImageUrl)
                            .apply(requestOptions)
                            .circleCrop()
                            .into(imagenPerfilImageView);



                } catch (JSONException e) {
                    Log.e(TAG, "Error al parsear la respuesta de la API", e);
                }
            }

            @Override
            public void onError(UtilREST.Response response) {
                Log.e(TAG, "Error al obtener el usuario por ID: " + response.content);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Inicio.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}