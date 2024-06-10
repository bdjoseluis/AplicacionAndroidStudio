package com.example.tfg.Dieta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Producto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InformacionProducto extends AppCompatActivity {

    private TextView textViewNombre;
    private TextView textViewMarca;
    private TextView textViewCantGramos;
    private TextView textViewKcal;
    private TextView textViewGrasas;
    private TextView textViewCarbohidratos;
    private TextView textViewProteinas;
    private ImageView imageViewProducto;
    private Button addToCartButton;
    private Button deleteProductButton;

    private int productoId;
    private int comidaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_producto);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

// Lee la preferencia "userId" del objeto de preferencias compartidas
        int userId = preferences.getInt("userId", -1);
        Log.d("VerProducto", "userId obtenido de las preferencias: " + userId);
        deleteProductButton = findViewById(R.id.deleteProductButton);

        // Inicializa las vistas
        textViewNombre = findViewById(R.id.productTitle);
        textViewMarca = findViewById(R.id.productBrand);
        textViewCantGramos = findViewById(R.id.productCantGramos);
        textViewKcal = findViewById(R.id.productKcal);
        textViewGrasas = findViewById(R.id.productGrasas);
        textViewCarbohidratos = findViewById(R.id.productCarbohidratos);
        textViewProteinas = findViewById(R.id.productProteinas);
        imageViewProducto = findViewById(R.id.productImage);

        // Obtiene el ID del producto del Intent
        productoId = getIntent().getIntExtra("producto_id", -1);
        comidaId = getIntent().getIntExtra("comida_id", -1);


        if (productoId != -1) {
            // Llama a la API para obtener el producto por su ID
            obtenerProductoPorId(productoId);
        } else {
        }
        addToCartButton = findViewById(R.id.addToCartButton);
        addToCartButton.setOnClickListener(v -> {
            if (productoId != -1) {
                // Llama a la API para asociar el producto a la comida
                API.asociarProductoAComida( comidaId, productoId, new UtilREST.OnResponseListener() {
                    @Override
                    public void onSuccess(UtilREST.Response response) {
                        showCustomToast("Producto agregado a la lista correctamente");
                        finish();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }

                    @Override
                    public void onError(UtilREST.Response response) {
                        showCustomToast("Ya existe este producto en tu lista");
                    }
                });
            }
        });
        deleteProductButton.setOnClickListener(v -> {
            // Llama a la API para borrar el producto por su ID
            API.deleteProductoPorId(productoId, new UtilREST.OnResponseListener() {
                @Override
                public void onSuccess(UtilREST.Response response) {
                    showCustomToast("Producto borrado correctamente");
                    finish();
                }

                @Override
                public void onError(UtilREST.Response response) {
                    showCustomToast("Error al borrar producto");
                }
            });
        });
        API.getUserById(userId, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response response) {
                try {
                    // Analizar la respuesta JSON para obtener los datos del usuario
                    JSONObject jsonResponse = new JSONObject(response.content);

                    // Obtener el `role_id` del JSON
                    JSONArray rolesArray = jsonResponse.getJSONArray("roles");
                    JSONObject roleObject = rolesArray.getJSONObject(0);
                    int roleId = roleObject.getInt("id");

                    // Mostrar el `role_id` en un Log para verificar que se ha recogido correctamente
                    Log.d("VerProducto", "role_id obtenido: " + roleId);

                    // Verificar si el rol del usuario es "Usuario"
                    if (roleId == 1) {
                        // Mostrar el botón de "Borrar producto"
                        deleteProductButton.setVisibility(View.VISIBLE);
                    } else {
                        // Ocultar el botón de "Borrar producto"
                        deleteProductButton.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    Log.e("VerProducto", "Error al analizar la respuesta de la API", e);
                }
            }

            @Override
            public void onError(UtilREST.Response response) {
                Log.e("VerProducto", "Error al obtener el usuario por ID: " + response.content);
            }
        });
    }

    // Función para obtener un producto por su ID
    private void obtenerProductoPorId(long id) {
        API.getProductoPorId(id, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    // Analiza la respuesta JSON para obtener un objeto Producto
                    Producto producto = UtilJSONParser.parseProducto(r.content);
                    Toast.makeText(InformacionProducto.this, producto.getTitulo(), Toast.LENGTH_SHORT).show();

                    // Actualiza las vistas con la información del producto
                    textViewNombre.setText(producto.getTitulo());
                    textViewMarca.setText(producto.getMarca());
                    textViewCantGramos.setText(String.format("Cantidad de gramos: %d", producto.getCantGramos()));
                    textViewKcal.setText(String.format("Calorías: %.2f kcal", producto.getKcal()));
                    textViewGrasas.setText(String.format("Grasas: %.2f g", producto.getGrasas()));
                    textViewCarbohidratos.setText(String.format("Carbohidratos: %.2f g", producto.getCarbohidratos()));
                    textViewProteinas.setText(String.format("Proteínas: %.2f g", producto.getProteinas()));

                    // Carga la imagen del producto si la URL de imagen no es nula
                    if (producto.getUrlImagen() != null && !producto.getUrlImagen().isEmpty()) {
                        Glide.with(InformacionProducto.this)
                                .load(producto.getUrlImagen())
                                .into(imageViewProducto);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(UtilREST.Response response) {
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
