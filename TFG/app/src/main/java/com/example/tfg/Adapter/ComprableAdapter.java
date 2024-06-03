package com.example.tfg.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tfg.R;
import com.example.tfg.domain.Comprable;

import java.util.ArrayList;

public class ComprableAdapter extends ArrayAdapter<Comprable> {
    private Context context;
    private ArrayList<Comprable> comprables;
    private SQLiteDatabase db; // Agrega una referencia a la base de datos

    public ComprableAdapter(Context context, ArrayList<Comprable> comprables, SQLiteDatabase db) {
        super(context, 0, comprables);
        this.context = context;
        this.comprables = comprables;
        this.db = db; // Asigna la referencia a la base de datos
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comprable, parent, false);
        }

        Comprable currentComprable = getItem(position);

        TextView titulo = convertView.findViewById(R.id.titulo);
        titulo.setText(currentComprable.getTitulo());

        TextView precio = convertView.findViewById(R.id.precio);
        precio.setText(String.valueOf(currentComprable.getPrecio()));

        TextView categoria = convertView.findViewById(R.id.categoria);
        categoria.setText(currentComprable.getPic());

        ImageView imagen = convertView.findViewById(R.id.imagen);
        // Asigna la imagen correspondiente al ImageView. Por ejemplo:
        //imagen.setImageResource(Integer.parseInt(currentComprable.getImagen()));

        Button btnEliminar = convertView.findViewById(R.id.btnEliminarPro);
        // Configura el evento click del botón eliminar, por ejemplo:

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarElemento(currentComprable);            }
        });


        return convertView;
    }
    // Método para eliminar un elemento de la lista
    public void eliminarElemento(Comprable comprable) {
        comprables.remove(comprable);
        notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado

        // Eliminar el elemento de la base de datos
        db.delete("carrito", "titulo = ?", new String[]{comprable.getTitulo()});
        Log.d("ComprableAdapter", "Elemento eliminado de la base de datos");

        // Log para verificar que se está llamando correctamente al método eliminarElemento
        Log.d("ComprableAdapter", "Elemento eliminado de la lista");
        Log.d("ComprableAdapter", "Título del elemento a eliminar: " + comprable.getTitulo());
    }

}
