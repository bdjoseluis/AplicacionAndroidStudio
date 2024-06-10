package com.example.tfg.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Producto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdapterProductosDeComida extends RecyclerView.Adapter<AdapterProductosDeComida.ViewHolder> {

    private List<Producto> productos;
    private int comidaId;
    private Context context;
    private OnProductoDeleteListener onProductoDeleteListener;

    // Constructor
    public AdapterProductosDeComida(Context context, List<Producto> productos, int comidaId) {
        this.context = context;
        this.productos = productos;
        this.comidaId = comidaId;
    }

    // Limpia la lista de productos
    public void clear() {
        productos.clear();
        notifyDataSetChanged();
    }

    // Añade una lista de productos a la lista existente
    public void addAll(List<Producto> nuevosProductos) {
        productos.addAll(nuevosProductos);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholderproductos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto producto = productos.get(position);

        // Obtén los productos de la comida
        API.getProductosDeComida(comidaId, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response response) {
                List<Producto> productosDeComida = UtilJSONParser.parseProductos(response.content);

                // Crea un conjunto de IDs de productos de la comida
                Set<Integer> productosDeComidaIds = new HashSet<>();
                for (Producto p : productosDeComida) {
                    productosDeComidaIds.add(p.getId());
                }

                // Verifica si el producto actual está en la lista de productos de la comida
                boolean estaEnLista = productosDeComidaIds.contains(producto.getId());

                // Configura el botón de acción
                if (estaEnLista) {
                    holder.btnProducto.setBackgroundResource(android.R.drawable.ic_menu_delete);
                    holder.btnProducto.setOnClickListener(v -> {
                        // Lógica para quitar el producto de la comida
                        API.desasociarProductoDeComida(comidaId, producto.getId(), new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response response) {
                                productos.remove(producto);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(UtilREST.Response response) {
                            }
                        });
                    });
                } else {
                    holder.btnProducto.setBackgroundResource(android.R.drawable.ic_menu_add);
                    holder.btnProducto.setOnClickListener(v -> {
                        // Lógica para añadir el producto a la comida
                        API.asociarProductoAComida(comidaId, producto.getId(), new UtilREST.OnResponseListener() {
                            @Override
                            public void onSuccess(UtilREST.Response response) {
                                productos.add(producto);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(UtilREST.Response response) {
                            }
                        });
                    });
                }
            }

            @Override
            public void onError(UtilREST.Response response) {
            }
        });

        holder.bind(producto);
    }


    @Override
    public int getItemCount() {
        return productos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewNombre;
        private TextView textViewMarca;
        private TextView textViewCantGramos;
        private TextView textViewKcal;
        private TextView textViewGrasas;
        private TextView textViewCarbohidratos;
        private TextView textViewProteinas;
        private Button btnProducto;
        private ImageView imageViewProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializar vistas
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewMarca = itemView.findViewById(R.id.textViewMarca);
            textViewCantGramos = itemView.findViewById(R.id.textViewCantGramos);
            textViewKcal = itemView.findViewById(R.id.textViewKcal);
            textViewGrasas = itemView.findViewById(R.id.textViewGrasas);
            textViewCarbohidratos = itemView.findViewById(R.id.textViewCarbohidratos);
            textViewProteinas = itemView.findViewById(R.id.textViewProteinas);
            imageViewProducto = itemView.findViewById(R.id.imageViewProducto);
            btnProducto = itemView.findViewById(R.id.btnProducto);
        }

        public void bind(Producto producto) {
            textViewNombre.setText(producto.getTitulo());
            textViewMarca.setText(producto.getMarca());
            textViewCantGramos.setText(String.format("%s gramos", producto.getCantGramos()));
            textViewKcal.setText(String.format("%s kcal", producto.getKcal()));
            textViewGrasas.setText(String.format("%s g", producto.getGrasas()));
            textViewCarbohidratos.setText(String.format("%s cbh", producto.getCarbohidratos()));
            textViewProteinas.setText(String.format("%s p", producto.getProteinas()));
            Glide.with(context)
                    .load(producto.getUrlImagen())
                    .into(imageViewProducto);
        }
    }

    // Interfaz para manejar eventos de eliminación
    public interface OnProductoDeleteListener {
        void onProductoDelete(Producto producto);
    }

    // Establece el listener para los eventos de eliminación de productos
    public void setOnProductoDeleteListener(OnProductoDeleteListener listener) {
        this.onProductoDeleteListener = listener;
    }
}
