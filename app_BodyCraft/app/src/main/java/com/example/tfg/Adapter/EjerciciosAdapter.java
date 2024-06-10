package com.example.tfg.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Ejercicio;

import java.util.List;

public class EjerciciosAdapter extends RecyclerView.Adapter<EjerciciosAdapter.ViewHolder> {
    private final Context context;
    private List<Ejercicio> ejercicios;
    private final int diaSeleccionado;

    public EjerciciosAdapter(Context context, List<Ejercicio> ejercicios, int diaSeleccionado) {
        this.context = context;
        this.ejercicios = ejercicios;
        this.diaSeleccionado = diaSeleccionado;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla el diseño del elemento
        View view = LayoutInflater.from(context).inflate(R.layout.item_ejercicio2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Obtén el ejercicio actual
        Ejercicio ejercicio = ejercicios.get(position);

        // Configura el VideoView
        if (ejercicio.getUrlVideo() != null) {
            Uri videoUri = Uri.parse(ejercicio.getUrlVideo());
            holder.videoView.setVideoURI(videoUri);
            holder.videoView.setOnPreparedListener(mp -> {
                // Establece el volumen a cero para que el video no tenga sonido
                mp.setVolume(0f, 0f);
                holder.videoView.start();
            });
        }

        // Configura el nombre y grupo muscular
        holder.nombreTextView.setText(ejercicio.getNombre());

        // Configura el botón de acción para agregar el ejercicio al día de la semana
        holder.actionButton.setOnClickListener(v -> {
            API.addEjercicioToDiaSemana(diaSeleccionado, ejercicio.getNombre(), new UtilREST.OnResponseListener() {
                @Override
                public void onSuccess(UtilREST.Response response) {
                }

                @Override
                public void onError(UtilREST.Response response) {
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return ejercicios.size();
    }

    // ViewHolder para mantener referencias a las vistas
    static class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView nombreTextView;
        Button actionButton;

        public ViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            actionButton = itemView.findViewById(R.id.agregarButton);
        }
    }

    // Método para actualizar los datos del adaptador
    public void setData(List<Ejercicio> ejercicios) {
        this.ejercicios = ejercicios;
        notifyDataSetChanged();
    }
}
