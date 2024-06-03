package com.example.tfg.Dieta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.AdaptadorProductos;
import com.example.tfg.R;
import com.example.tfg.domain.Producto;

import java.util.ArrayList;
import java.util.List;

public class VerProducto extends AppCompatActivity {
    private List<Producto> listadoProductosGenerales;

    private ListView listViewProductos;
    private AdaptadorProductos adaptadorProductos;
    private int comidaId;
    String marca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);

        comidaId = getIntent().getIntExtra("comida_id", -1);
        marca = getIntent().getStringExtra("categoria"); // Obtener la categoría desde el intent

        // Inicializa la lista de productos
        listViewProductos = findViewById(R.id.listViewProductos);
        listadoProductosGenerales = new ArrayList<>();
        adaptadorProductos = new AdaptadorProductos(this, R.layout.item_producto, listadoProductosGenerales, comidaId);
        listViewProductos.setAdapter(adaptadorProductos);

        listViewProductos.setOnItemClickListener((parent, view, position, id) -> {
            // Obtiene el producto seleccionado basado en la posición
            Producto productoSeleccionado = listadoProductosGenerales.get(position);

            // Crea un intent para iniciar la nueva actividad
            Intent intent = new Intent(VerProducto.this, InformacionProducto.class); // Reemplaza `NuevaActividad.class` con la clase de tu nueva actividad
            // Pasa la ID del producto como un extra
            intent.putExtra("producto_id", productoSeleccionado.getId());
            intent.putExtra("comida_id", comidaId);
            // Inicia la nueva actividad
            startActivity(intent);
        });
        // Llama a la API para obtener productos por la marca
        obtenerProductosPorMarca(marca);


    }

    // Función para obtener productos por la marca
    private void obtenerProductosPorMarca(String marca) {
        API.getProductoPorMarca(marca, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    Log.d("mislogs", "Marca: " + marca);
                    Log.d("mislogs", "Respuesta JSON: " + r.content);
                    List<Producto> productosObtenidos = UtilJSONParser.parseProductos(r.content);
                    for (Producto producto : productosObtenidos) {
                        Log.d("mislogs", "Producto ID: " + producto.getId() + ", Título: " + producto.getTitulo() + ", URL de imagen: " + producto.getUrlImagen());
                    }

                    listadoProductosGenerales = UtilJSONParser.parseProductos(r.content);
                    adaptadorProductos.clear();
                    adaptadorProductos.addAll(listadoProductosGenerales);
                    adaptadorProductos.notifyDataSetChanged();
                    Log.d("mislogs", "Productos descargados: " + listadoProductosGenerales.size());
                } catch (Exception e) {
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerProductosPorMarca(marca); // Descarga los productos de nuevo cuando la actividad se reanuda
    }

}
