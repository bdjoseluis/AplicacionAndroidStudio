package com.example.tfg.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.Rutina.Rutina;
import com.example.tfg.domain.RutinaDefecto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RutinasAdapter extends RecyclerView.Adapter<RutinasAdapter.RutinaViewHolder> {

    private Context context;
    private List<RutinaDefecto> listaRutinas;

    public RutinasAdapter(Context context, List<RutinaDefecto> listaRutinas) {
        this.context = context;
        this.listaRutinas = listaRutinas;
    }

    @NonNull
    @Override
    public RutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rutina, parent, false);
        return new RutinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RutinaViewHolder holder, int position) {
        RutinaDefecto rutina = listaRutinas.get(position);
        holder.nombreRutina.setText(rutina.getNombre());
        holder.imagenRutina.setImageResource(rutina.getImagen());
    }

    @Override
    public int getItemCount() {
        return listaRutinas.size();
    }

    public class RutinaViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenRutina;
        TextView nombreRutina;

        public RutinaViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenRutina = itemView.findViewById(R.id.imagenRutina);
            nombreRutina = itemView.findViewById(R.id.nombreRutina);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Implementa la lógica para manejar el evento de clic en la rutina seleccionada
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Obtiene la rutina seleccionada
                        RutinaDefecto rutinaSeleccionada = listaRutinas.get(position);
                        // Agrega los ejercicios a los días de la semana
                        addEjerciciosToDiaSemana(rutinaSeleccionada);
                        Intent intent = new Intent(context, Rutina.class);
                        context.startActivity(intent);
                        ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    }
                }
            });
        }

        private void addEjerciciosToDiaSemana(RutinaDefecto rutina) {
            // Itera sobre el HashMap de ejercicios por día y los agrega a los días de la semana
            HashMap<Integer, List<String>> ejerciciosPorDia = rutina.getEjerciciosPorDia();
            for (Map.Entry<Integer, List<String>> entry : ejerciciosPorDia.entrySet()) {
                int diaSemana = entry.getKey();
                List<String> ejercicios = entry.getValue();

                Log.d("RutinasAdapter", "Día de la semana: " + diaSemana);
                Log.d("RutinasAdapter", "Ejercicios: " + ejercicios.toString());

                // Llama al método API para añadir cada ejercicio al día de la semana correspondiente
                for (String ejercicio : ejercicios) {
                    Log.d("RutinasAdapter", "Agregando ejercicio '" + ejercicio + "' al día de la semana " + diaSemana);
                    API.addEjercicioToDiaSemana(diaSemana, ejercicio, new UtilREST.OnResponseListener() {
                        @Override
                        public void onSuccess(UtilREST.Response r) {
                            // Maneja la respuesta exitosa
                            // Puedes actualizar la lista de ejercicios o mostrar un mensaje de éxito
                        }

                        @Override
                        public void onError(UtilREST.Response r) {
                            // Maneja el error
                            // Puedes mostrar un mensaje de error al usuario
                        }
                    });
                }
            }
        }
    }
}
