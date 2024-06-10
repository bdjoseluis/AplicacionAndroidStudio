package com.example.tfg.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.R;
import com.example.tfg.domain.Comidas;
import com.example.tfg.domain.Producto;

import java.util.List;

public class AdaptadorComidaListView extends ArrayAdapter<Comidas> {
    private Context context;
    private int resource;
    private List<Comidas> miscomidas;
    private OnAddItemClickListener addItemClickListener;
    private OnUpdateTotalsListener updateTotalsListener;

    public AdaptadorComidaListView(@NonNull Context context, int resource, @NonNull List<Comidas> miscomidas) {
        super(context, resource, miscomidas);
        this.context = context;
        this.resource = resource;
        this.miscomidas = miscomidas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflador = LayoutInflater.from(context);
        View mifila = inflador.inflate(resource, parent, false);

        TextView titulo = mifila.findViewById(R.id.productonombre);
        TextView kcal = mifila.findViewById(R.id.marca);
        TextView proteinas = mifila.findViewById(R.id.Proteinas);
        TextView gramos = mifila.findViewById(R.id.gramos);
        TextView carbo = mifila.findViewById(R.id.Carbo);
        ImageView compartirDia = mifila.findViewById(R.id.compartirDia);
        ImageView añadirProducto = mifila.findViewById(R.id.addProducto);

        titulo.setText(miscomidas.get(position).getTitulo());
        kcal.setText(String.valueOf((int) miscomidas.get(position).getKcal()) + " Kcal");
        proteinas.setText(String.valueOf((int) miscomidas.get(position).getProteinas()) + " P");
        gramos.setText(String.valueOf((int) miscomidas.get(position).getGramos()) + " G");
        carbo.setText(String.valueOf((int) miscomidas.get(position).getCarbohidratos()) + " CBH");

        compartirDia.setImageResource(R.drawable.like);
        añadirProducto.setImageResource(R.drawable.plus2);

        final int posicionActual = position;
        añadirProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comidas comidaSeleccionada = miscomidas.get(posicionActual);
                int idComidaSeleccionada = comidaSeleccionada.getId();

                // Si se estableció un listener para el clic del ítem, notificarlo
                if (addItemClickListener != null) {
                    addItemClickListener.onAddItemClick(idComidaSeleccionada, comidaSeleccionada);
                }
            }
        });

        compartirDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un menú contextual
                PopupMenu popupMenu = new PopupMenu(context, compartirDia);
                popupMenu.getMenuInflater().inflate(R.menu.contextmenu, popupMenu.getMenu());

                // Manejar eventos del menú
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.compartirDiaDieta) {
                            Comidas comidaSeleccionada = miscomidas.get(posicionActual);
                            int idComidaSeleccionada = comidaSeleccionada.getId();
                            API.getProductosDeComida(idComidaSeleccionada, new UtilREST.OnResponseListener() {
                                @Override
                                public void onSuccess(UtilREST.Response response) {
                                    List<Producto> productos = UtilJSONParser.parseProductos(response.content);

                                    // Crear un intent para compartir el texto
                                    Intent intentCompartir = new Intent(Intent.ACTION_SEND);
                                    intentCompartir.setType("text/plain");

                                    // Establecer el texto que se compartirá (todos los productos de la comida)
                                    StringBuilder textoCompartir = new StringBuilder("Productos de la comida:\n");
                                    for (Producto producto : productos) {
                                        textoCompartir.append(producto).append("\n");
                                    }
                                    intentCompartir.putExtra(Intent.EXTRA_TEXT, textoCompartir.toString());

                                    // Mostrar el menú de compartir a través de aplicaciones disponibles
                                    startActivityWithIntent(Intent.createChooser(intentCompartir, "Compartir a través de"));
                                }

                                @Override
                                public void onError(UtilREST.Response response) {
                                    // Manejar el error si no se pueden obtener los productos
                                }
                            });
                            return true;
                        } else if (item.getItemId() == R.id.borrarDia) {
                            Log.d("AdaptadorComidaListView", "Se seleccionó borrarDia");
                            // Lógica para borrar el día aquí
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

                // Mostrar el menú contextual
                popupMenu.show();
            }
        });

        // Llama al método para calcular y notificar los totales
        calcularYNotificarTotales();

        return mifila;
    }

    // Método para calcular los totales de los valores nutricionales
    public void calcularYNotificarTotales() {
        int totalKcal = 0;
        int totalP = 0;
        int totalG = 0;
        int totalCBH = 0;

        for (Comidas comida : miscomidas) {
            totalKcal += comida.getKcal();
            totalP += comida.getProteinas();
            totalG += comida.getGramos();
            totalCBH += comida.getCarbohidratos();
        }

        // Si se estableció un listener para la actualización de totales, notificarlo
        if (updateTotalsListener != null) {
            updateTotalsListener.onUpdateTotals(totalKcal, totalP, totalG, totalCBH);
        }
    }

    public void startActivityWithIntent(Intent intent) {
        if (context instanceof Activity) {
            ((Activity) context).startActivity(intent);
        } else {
            throw new IllegalStateException("No se puede iniciar la actividad desde un contexto que no es una instancia de Activity");
        }
    }

    // Método para establecer el listener del clic en el botón "Añadir Producto"
    public void setOnAddItemClickListener(OnAddItemClickListener listener) {
        this.addItemClickListener = listener;
    }

    // Interfaz para el listener del clic en el botón "Añadir Producto"
    public interface OnAddItemClickListener {
        void onAddItemClick(int position, Comidas comida);
    }

    // Método para establecer el listener para la actualización de totales
    public void setOnUpdateTotalsListener(OnUpdateTotalsListener listener) {
        this.updateTotalsListener = listener;
    }

    // Interfaz para el listener de actualización de totales
    public interface OnUpdateTotalsListener {
        void onUpdateTotals(int totalKcal, int totalP, int totalG, int totalCBH);
    }
}

