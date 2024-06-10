package com.example.tfg.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Producto;

import java.util.List;

public class AdaptadorProductos extends ArrayAdapter<Producto> {
    private final Context context;
    private final int resource;
    private final List<Producto> productos;
    private final long comidaId;

    public AdaptadorProductos(@NonNull Context context, int resource, @NonNull List<Producto> productos, long comidaId) {
        super(context, resource, productos);
        this.context = context;
        this.resource = resource;
        this.productos = productos;
        this.comidaId = comidaId;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        }

        Producto producto = productos.get(position);

        // Inicializa todas las vistas
        ImageView imagenProducto = convertView.findViewById(R.id.imagenProductogeneral);
        TextView titulo = convertView.findViewById(R.id.productonombre);
        TextView marca = convertView.findViewById(R.id.marca);
        ImageView btnAgregarProducto = convertView.findViewById(R.id.addProducto);


        Log.d("AdaptadorProductos", "URL de la imagen: " + producto.getUrlImagen());
        // Configura los valores de las vistas
        if (imagenProducto != null) {
            Glide.with(context)
                    .load(producto.getUrlImagen())
                    .error("https://drive.google.com/uc?export=download&id=1ft2qgzDbpC91ZBCHTenn_RliKI1SdQkf")
                    .into(imagenProducto);
        }

        if (titulo != null) {
            titulo.setText(producto.getTitulo());
        }

        if (marca != null) {
            marca.setText(producto.getMarca());
        }


        // Maneja el evento de clic en el botón de agregar producto
        if (btnAgregarProducto != null) {
            btnAgregarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Llama a la API para asociar el producto a la comida
                    API.asociarProductoAComida((int) comidaId, (int) producto.getId(), new UtilREST.OnResponseListener() {
                        @Override
                        public void onSuccess(UtilREST.Response response) {
                        }

                        @Override
                        public void onError(UtilREST.Response response) {
                        }
                    });
                }
            });
        }

        return convertView;
    }
}