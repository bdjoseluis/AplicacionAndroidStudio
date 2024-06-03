package com.example.tfg.Compra;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tfg.Adapter.ComprableAdapter;
import com.example.tfg.R;
import com.example.tfg.domain.Comprable;

import java.util.ArrayList;

public class carrito extends AppCompatActivity {
    private SQLiteDatabase db;
    private ListView listView;
    private ComprableAdapter adapter;
    private TextView textViewTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        // Abrir la base de datos
        db = openOrCreateDatabase("MisComprables", MODE_PRIVATE, null);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Inicializar la vista y el adaptador
        listView = findViewById(R.id.listacarrito);
        textViewTotal = findViewById(R.id.textViewTotal);

        // Obtener productos y configurar la lista
        ArrayList<Comprable> productos = obtenerProductos();
        adapter = new ComprableAdapter(this, productos, db);
        listView.setAdapter(adapter);

        // Configurar el listener para eliminar elementos
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eliminarElemento(position);
            }
        });

        // Configurar el botón de pago
        Button botonPagar = findViewById(R.id.botonPagar);
        botonPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procederAlPago();
            }
        });

        // Mostrar el total inicial
        mostrarTotal();
    }


    private void procederAlPago() {
        // Lógica para el pago
        showCustomToast("Iniciando proceso de pago...");
        // Puedes abrir una nueva actividad o realizar el pago aquí
        // Por ejemplo, iniciar una nueva actividad para el pago:
        Intent intent = new Intent(this, PagosActivity.class);
        startActivity(intent);
    }

    // Método para calcular y mostrar el total de la cuenta
    private void mostrarTotal() {
        double total = 0.0;
        for (int i = 0; i < adapter.getCount(); i++) {
            Comprable comprable = adapter.getItem(i);
            if (comprable != null) {
                total += comprable.getPrecio();
            }
        }
        textViewTotal.setText(String.format("Total: $%.2f", total));
    }

    // Método para obtener todos los productos de la base de datos
    private ArrayList<Comprable> obtenerProductos() {
        ArrayList<Comprable> productos = new ArrayList<>();

        // Realizar una consulta a la base de datos para obtener todos los productos
        Cursor cursor = db.rawQuery("SELECT * FROM carrito", null);

        // Iterar sobre el cursor y agregar los productos a la lista
        if (cursor.moveToFirst()) {
            do {
                String titulo = cursor.getString(0);
                String pic = cursor.getString(1);
                String imagen = cursor.getString(2);
                double precio = cursor.getDouble(3);
                productos.add(new Comprable(titulo, pic, imagen, precio));
            } while (cursor.moveToNext());
        }

        // Cerrar el cursor
        cursor.close();

        return productos;
    }


    // Método para eliminar un elemento de la lista y de la base de datos
    private void eliminarElemento(int position) {
        Comprable comprable = adapter.getItem(position);
        if (comprable != null) {
            String titulo = comprable.getTitulo();
            Log.d("Eliminar", "Título del elemento a eliminar: " + titulo);

            // Eliminar el elemento de la base de datos
            try {
                db.delete("carrito", "titulo = ?", new String[]{titulo});
                Log.d("Eliminar", "Elemento eliminado de la base de datos");
            } catch (Exception e) {
                Log.e("Eliminar", "Error al eliminar de la base de datos: " + e.getMessage());
            }

            // Eliminar el elemento de la lista
            adapter.remove(comprable);
            // Mostrar mensaje de confirmación
            String text="Producto eliminado: " + titulo;
            showCustomToast(text);
        }

        // Actualizar el total de la cuenta
        mostrarTotal();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Volver a la actividad anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
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