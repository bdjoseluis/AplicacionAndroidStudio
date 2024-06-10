package com.example.tfg.Dieta;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Producto;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class NuevoProductoActivity extends AppCompatActivity {

    private EditText inputTitulo;
    private EditText inputMarca;
    private EditText inputCantGramos;
    private EditText inputKcal;
    private EditText inputGrasas;
    private EditText inputCarbohidratos;
    private EditText inputProteinas;
    private EditText inputUrlImagen;
    private Button botonGuardarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);

        // Inicializar las vistas
        inputTitulo = findViewById(R.id.inputTitulo);
        inputMarca = findViewById(R.id.inputMarca);
        inputCantGramos = findViewById(R.id.inputCantGramos);
        inputKcal = findViewById(R.id.inputKcal);
        inputGrasas = findViewById(R.id.inputGrasas);
        inputCarbohidratos = findViewById(R.id.inputCarbohidratos);
        inputProteinas = findViewById(R.id.inputProteinas);
        inputUrlImagen = findViewById(R.id.inputUrlImagen);
        botonGuardarProducto = findViewById(R.id.botonGuardarProducto);
        KonfettiView konfettiView = findViewById(R.id.viewKonfetti);

        // Establecer listener para el botón de guardar producto
        botonGuardarProducto.setOnClickListener(v -> {
            // Obtener los valores ingresados por el usuario
            String titulo = inputTitulo.getText().toString().trim();
            String marca = inputMarca.getText().toString().trim();
            Integer cantGramos = Integer.parseInt(inputCantGramos.getText().toString().trim());
            double kcal = Double.parseDouble(inputKcal.getText().toString().trim());
            double grasas = Double.parseDouble(inputGrasas.getText().toString().trim());
            double carbohidratos = Double.parseDouble(inputCarbohidratos.getText().toString().trim());
            double proteinas = Double.parseDouble(inputProteinas.getText().toString().trim());
            String urlImagen = inputUrlImagen.getText().toString().trim();

            // Crear un objeto Producto con los datos ingresados
            Producto nuevoProducto = new Producto();
            nuevoProducto.setTitulo(titulo);
            nuevoProducto.setMarca(marca);
            nuevoProducto.setCantGramos(cantGramos);
            nuevoProducto.setKcal(kcal);
            nuevoProducto.setGrasas(grasas);
            nuevoProducto.setCarbohidratos(carbohidratos);
            nuevoProducto.setProteinas(proteinas);
            nuevoProducto.setUrlImagen(urlImagen);

            // Llamar a la API para crear el producto
            API.crearProducto(nuevoProducto, new UtilREST.OnResponseListener() {
                @Override
                public void onSuccess(UtilREST.Response response) {
                    // Mostrar animación de desvanecimiento
                    launchConfetti();

                    mostrarAnimacionDesvanecimiento(inputTitulo);
                    // Mostrar un mensaje de éxito
                    showSuccessDialog();
                    new Handler().postDelayed(() -> {
                        finish(); // Finaliza la actividad después de 5 segundos
                    }, 5000);
                }

                @Override
                public void onError(UtilREST.Response response) {
                    // Mostrar un mensaje de error
                    showCustomToast("Error al crear el producto");
                }
            });
        });
    }

    // Método para mostrar una animación de desvanecimiento
    private void mostrarAnimacionDesvanecimiento(View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(1000); // Duración en milisegundos
        view.startAnimation(fadeOut);
    }

    // Método para mostrar el cuadro de diálogo de éxito
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customLayout = getLayoutInflater().inflate(R.layout.dialog_success, null);
        builder.setView(customLayout);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void launchConfetti() {
        KonfettiView konfettiView = findViewById(R.id.viewKonfetti);
        konfettiView.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.BLUE)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1500L)
                .addShapes(Shape.CIRCLE, Shape.RECT)
                .addSizes(new Size(12, 5), new Size(16, 6))
                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, konfettiView.getHeight() + 50f)
                .streamFor(300, 2000L);
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
