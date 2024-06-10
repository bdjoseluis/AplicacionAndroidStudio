package com.example.tfg.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg.R;
import com.example.tfg.domain.Comprable;

import java.util.ArrayList;
import java.util.List;

public class AdapterCompra extends RecyclerView.Adapter<AdapterCompra.ViewHolder> {
    private List<Comprable> comprables;
    private Context context;
    private RecyclerView recyclerView;
    private int centralItemSize;
    private int selectedItemPosition = -1; // Posición del elemento seleccionado

    private OnAddItemClickListener addItemClickListener;

    // Inicializa la lista 'comprables' para evitar el acceso a una lista nula
    public AdapterCompra(List<Comprable> comprables, Context context, RecyclerView recyclerView) {
        this.comprables = comprables != null ? comprables : new ArrayList<>();
        this.context = context;
        this.recyclerView = recyclerView;
        centralItemSize = calculateCentralItemSize();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholdercompra, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comprable comprable = comprables.get(position);
        holder.bind(comprable, position);
        holder.btnAdd.setOnClickListener(v -> {
            if (addItemClickListener != null) {
                addItemClickListener.onAddItemClick(comprable);
            }
        });
    }
    private int calculateCentralItemSize() {
        // Devuelve la mitad del ancho de la pantalla
        return recyclerView.getResources().getDisplayMetrics().widthPixels / 2;
    }
    // Verificación nula en el método getItemCount
    @Override
    public int getItemCount() {
        return comprables != null ? comprables.size() : 0;
    }

    public void setOnAddItemClickListener(OnAddItemClickListener listener) {
        this.addItemClickListener = listener;
    }

    // Verifica que 'comprables' no sea nula y la inicializa si es necesario
    public void clear() {
        if (comprables == null) {
            comprables = new ArrayList<>();
        } else {
            comprables.clear();
        }
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }

    // Verifica que 'comprables' no sea nula y la inicializa si es necesario
    public void addAll(List<Comprable> newComprables) {
        if (comprables == null) {
            comprables = new ArrayList<>();
        }
        comprables.addAll(newComprables);
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
        TextView categoryName;
        TextView precio;
        ImageView categoryPic;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.nombrecategoria);
            precio = itemView.findViewById(R.id.preciocomprable);
            categoryPic = itemView.findViewById(R.id.categoriapic);
            btnAdd = itemView.findViewById(R.id.Añadircompra);
            itemView.setOnTouchListener(this);
        }

        public void bind(Comprable comprable, int position) {
            categoryName.setText(comprable.getTitulo());
            precio.setText(String.format("$%.2f", comprable.getPrecio()));

            Glide.with(context)
                    .load(comprable.getImagen())
                    .into(categoryPic);

            adjustItemSize(position);

        }

        private void adjustItemSize(int position) {
            // Si la posición del elemento coincide con la posición del elemento seleccionado,
            // establece su tamaño al tamaño central
            if (position == selectedItemPosition) {
                categoryPic.getLayoutParams().width = centralItemSize;
                categoryPic.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            } else {
                // Si no coincide, establece un tamaño más pequeño
                int smallerSize = centralItemSize / 2; // Por ejemplo, la mitad del tamaño central
                categoryPic.getLayoutParams().width = smallerSize;
                categoryPic.getLayoutParams().height = smallerSize;
            }
            categoryPic.requestLayout(); // Solicita un nuevo diseño para aplicar los cambios de tamaño
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Obtener la posición del elemento seleccionado
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Actualizar la posición del elemento seleccionado
                    selectedItemPosition = position;
                    // Notificar cambios en los datos para que se vuelva a dibujar la vista
                    notifyDataSetChanged();
                }
            }
            return false;
        }
    }

    // Modificación de updateData para inicializar la lista si es necesario
    public void updateData(ArrayList<Comprable> filteredComprables) {
        if (comprables == null) {
            comprables = new ArrayList<>();
        } else {
            comprables.clear();
        }
        comprables.addAll(filteredComprables);
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
        selectedItemPosition = -1; // Restablece la posición del elemento seleccionado
    }

    public interface OnAddItemClickListener {
        void onAddItemClick(Comprable comprable);
    }

}
