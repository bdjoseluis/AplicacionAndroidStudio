package com.example.tfg.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.example.tfg.R;
import com.example.tfg.domain.DiasSemana;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorRutinaDias extends ArrayAdapter<DiasSemana> {
    private Context context;
    private int resource;
    private List<DiasSemana> diasSemana;
    private List<Integer> drawablesDisponibles;

    public AdaptadorRutinaDias(@NonNull Context context, int resource, @NonNull List<DiasSemana> diasSemana) {
        super(context, resource, diasSemana);
        this.context = context;
        this.resource = resource;
        this.diasSemana = diasSemana;

        // Inicializa la lista de drawables disponibles
        inicializarDrawablesDisponibles();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflador = LayoutInflater.from(this.getContext());

        View mifila = inflador.inflate(resource, parent, false);

        ImageView imagenDia = mifila.findViewById(R.id.imagenDiaSemanaRutina);
        TextView diaSemana = mifila.findViewById(R.id.diaSemanaPro);

        diaSemana.setText(diasSemana.get(position).getTitulo());

        // Establece la imagen para el día de la semana
        imagenDia.setImageResource(getDrawableParaDia(position));

        return mifila;
    }

    private void inicializarDrawablesDisponibles() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String userGenero = preferences.getString("usergenero", "");
        drawablesDisponibles = new ArrayList<>();
        if (userGenero.equals("Hombre")){
            drawablesDisponibles.add(R.drawable.lunes);
            drawablesDisponibles.add(R.drawable.martes);
            drawablesDisponibles.add(R.drawable.viernes);
            drawablesDisponibles.add(R.drawable.jueves);
            drawablesDisponibles.add(R.drawable.parecidasparaeditar4);
            drawablesDisponibles.add(R.drawable.parecidasparaeditar10);
            drawablesDisponibles.add(R.drawable.parecidasparaeditar11);
            drawablesDisponibles.add(R.drawable.bodybuilderhombre);
            drawablesDisponibles.add(R.drawable.bodybuilderhombre2);
            drawablesDisponibles.add(R.drawable.bodybuilderhombre3);
        }
        if (userGenero.equals("Mujer")){
            drawablesDisponibles.add(R.drawable.sabado);
            drawablesDisponibles.add(R.drawable.bodybuildermujer4);
            drawablesDisponibles.add(R.drawable.bodybuildermujer5);
            drawablesDisponibles.add(R.drawable.bodybuildermujer6);
            drawablesDisponibles.add(R.drawable.bodybuildermujer7);
            drawablesDisponibles.add(R.drawable.bodybuildermuje);
            drawablesDisponibles.add(R.drawable.bodybuildermuje2);
            drawablesDisponibles.add(R.drawable.bodybuildermuje3);
            drawablesDisponibles.add(R.drawable.bodybuildermuje5);


        }

    }

    private int getDrawableParaDia(int position) {
        // Verifica si quedan drawables disponibles
        if (drawablesDisponibles.isEmpty()) {
            // Si no hay drawables disponibles, restablece la lista
            inicializarDrawablesDisponibles();
        }

        // Elige un drawable aleatorio de los disponibles
        int randomIndex = (int) (Math.random() * drawablesDisponibles.size());
        int drawableElegido = drawablesDisponibles.get(randomIndex);

        // Elimina el drawable elegido de la lista de disponibles
        drawablesDisponibles.remove(randomIndex);

        return drawableElegido;
    }
}
