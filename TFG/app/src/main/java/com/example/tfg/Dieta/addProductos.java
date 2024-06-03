package com.example.tfg.Dieta;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.AdapterProductosDeComida;
import com.example.tfg.R;
import com.example.tfg.domain.Producto;

import java.util.ArrayList;
import java.util.List;

public class addProductos extends AppCompatActivity {

    private int posicion;
    private RecyclerView recyclerViewProductos;
    private List<Producto> listadoProductosComida;
    private AdapterProductosDeComida adapter;
    private Button añadirProductosalalista;
    private Button botonNuevoProducto;
    private EditText searchEditText; // Nuevo EditText para búsquedas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_productos);

        Intent intent = getIntent();
        posicion = intent.getIntExtra("comida_id", -1);
        String diaSemana = intent.getStringExtra("dia_Semana");
        String comidaNombre = intent.getStringExtra("comida_nombre");

        // Configurar el título o vista de información
        TextView textViewDiaDeLaSemana = findViewById(R.id.diadelasemanaseleccionado);
        TextView textViewComidaSeleccionada = findViewById(R.id.comidaseleccionada);
        textViewDiaDeLaSemana.setText(diaSemana);
        textViewComidaSeleccionada.setText(comidaNombre);

        botonNuevoProducto = findViewById(R.id.boton_nuevo_producto);
        botonNuevoProducto.setOnClickListener(v -> {
            // Abre una nueva actividad para agregar un producto
            Intent nuevoProductoIntent = new Intent(addProductos.this, NuevoProductoActivity.class);
            startActivity(nuevoProductoIntent);
        });

        // Inicializa las vistas y el adaptador
        initViews();
        initAdapters();

        // Descarga los productos del día de la semana
        descargarProductosDiaSemana();
    }

    // Inicialización de vistas
    private void initViews() {
        recyclerViewProductos = findViewById(R.id.productosdecomida);
        añadirProductosalalista = findViewById(R.id.añadirProductosalalista);

        añadirProductosalalista.setOnClickListener(view -> {
            Intent intent = new Intent(addProductos.this, SeccionProductos.class);
            intent.putExtra("comida_id", posicion);
            startActivity(intent);
            actualizarAtributosComida();
        });

        // Inicializar `searchEditText`
        searchEditText = findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No hacer nada antes de que el texto cambie
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Realizar la búsqueda cuando cambia el texto
                realizarBusqueda(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No hacer nada después de que el texto cambie
            }
        });

        listadoProductosComida = new ArrayList<>();
    }

    // Inicialización de adaptadores
    private void initAdapters() {
        adapter = new AdapterProductosDeComida(this, listadoProductosComida, posicion);
        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductos.setAdapter(adapter);

        // Establecer listener para eliminar producto
        adapter.setOnProductoDeleteListener(producto -> {
            API.desasociarProductoDeComida(posicion, producto.getId(), new UtilREST.OnResponseListener() {
                @Override
                public void onSuccess(UtilREST.Response response) {
                    descargarProductosDiaSemana(); // Actualizar lista de productos
                    actualizarAtributosComida();
                }

                @Override
                public void onError(UtilREST.Response response) {
                }
            });
        });
    }

    // Realiza una búsqueda de productos basándose en el nombre parcial proporcionado
    private void realizarBusqueda(String nombreParcial) {
        if (nombreParcial.isEmpty()) {
            // Si el campo de búsqueda está vacío, descarga los productos de la comida
            descargarProductosDiaSemana();
        } else {
            // Llama a la API para obtener productos por nombre parcial
            API.getProductosPorNombreParcial(nombreParcial, new UtilREST.OnResponseListener() {
                @Override
                public void onSuccess(UtilREST.Response r) {
                    try {
                        // Parsear los productos de la respuesta y actualizarlos
                        listadoProductosComida = UtilJSONParser.parseProductos(r.content);
                        adapter.clear();
                        adapter.addAll(listadoProductosComida);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onError(UtilREST.Response r) {
                    showCustomToast("No se ha encontrado el producto");
                }
            });
        }
    }

    // Descarga los productos de la comida del día de la semana
    private void descargarProductosDiaSemana() {
        API.getProductosDeComida(posicion, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    listadoProductosComida = UtilJSONParser.parseProductos(r.content);
                    adapter.clear();
                    adapter.addAll(listadoProductosComida);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
            }
        });
    }

    private void actualizarAtributosComida() {
        double totalKcal = 0;
        double totalProteinas = 0;
        double totalGramos = 0;
        double totalCarbohidratos = 0;

        // Calcular los valores a partir de los productos de la lista
        for (Producto producto : listadoProductosComida) {
            totalKcal += producto.getKcal();
            totalProteinas += producto.getProteinas();
            totalGramos += producto.getCantGramos();
            totalCarbohidratos += producto.getCarbohidratos();
        }

        // Llamar a la API para actualizar la comida en la base de datos
        API.actualizarComida(posicion, totalKcal, totalProteinas, totalGramos, totalCarbohidratos, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response response) {
            }

            @Override
            public void onError(UtilREST.Response response) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        descargarProductosDiaSemana();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Dieta.class);
        descargarProductosDiaSemana();
        actualizarAtributosComida();
        startActivity(intent);
        finish();
        super.onBackPressed();
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