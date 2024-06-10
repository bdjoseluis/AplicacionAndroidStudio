package com.example.tfg;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView textViewSelectedDate;
    private ListView listViewEvents;
    private Button buttonAddEvent, buttonRemoveEvent;
    private ArrayAdapter<String> eventsAdapter;
    private List<String> eventsList;
    private Map<String, List<String>> eventsMap;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Referencias a los elementos de la interfaz
        calendarView = findViewById(R.id.calendarView);
        textViewSelectedDate = findViewById(R.id.textViewSelectedDate);
        listViewEvents = findViewById(R.id.listViewEvents);
        buttonAddEvent = findViewById(R.id.buttonAddEvent);
        buttonRemoveEvent = findViewById(R.id.buttonRemoveEvent);

        // Inicializa el formato de fecha
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Inicializa el mapa de eventos
        eventsMap = new HashMap<>();

        // Inicializa la lista de eventos y el adaptador para el ListView
        eventsList = new ArrayList<>();
        eventsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventsList);
        listViewEvents.setAdapter(eventsAdapter);

        // Configura el CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Convierte la fecha seleccionada a un objeto Date
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            Date selectedDate = calendar.getTime();
            String formattedDate = dateFormat.format(selectedDate);

            // Muestra la fecha seleccionada en el TextView
            textViewSelectedDate.setText("Fecha seleccionada: " + formattedDate);

            // Actualiza los eventos para la fecha seleccionada
            updateEventsList(formattedDate);
        });

        // Configura el botón de agregar eventos
        buttonAddEvent.setOnClickListener(v -> {
            // Obtiene la fecha seleccionada
            String selectedDateText = textViewSelectedDate.getText().toString();

// Luego, aplica el método replace en la cadena
            String selectedDate = selectedDateText.replace("Fecha seleccionada: ", "");

            // Solicita al usuario que ingrese el nombre del evento
            EditText input = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Agregar evento");
            builder.setMessage("Ingrese el nombre del evento:");
            builder.setView(input);
            builder.setPositiveButton("Agregar", (dialog, which) -> {
                String eventName = input.getText().toString().trim();

                // Agrega el evento a la lista
                addEvent(selectedDate, eventName);
            });
            builder.setNegativeButton("Cancelar", null);
            builder.show();
        });

        // Configura el botón de eliminar eventos
        buttonRemoveEvent.setOnClickListener(v -> {
            // Obtiene la fecha seleccionada
            // Obtener el texto de textViewSelectedDate como una cadena de texto
            String selectedDateText = textViewSelectedDate.getText().toString();

// Eliminar el prefijo "Fecha seleccionada: " para obtener solo la fecha
            String selectedDate = selectedDateText.replace("Fecha seleccionada: ", "");

            // Verifica si hay eventos para la fecha seleccionada
            if (eventsMap.containsKey(selectedDate)) {
                // Muestra un cuadro de diálogo para seleccionar el evento a eliminar
                List<String> events = eventsMap.get(selectedDate);
                CharSequence[] eventNames = events.toArray(new CharSequence[0]);
                boolean[] checkedItems = new boolean[eventNames.length];

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Selecciona los eventos a eliminar:");
                builder.setMultiChoiceItems(eventNames, checkedItems, (dialog, which, isChecked) -> {
                    // Marca los eventos seleccionados
                    checkedItems[which] = isChecked;
                });

                builder.setPositiveButton("Eliminar", (dialog, which) -> {
                    // Elimina los eventos seleccionados
                    List<String> eventsToRemove = new ArrayList<>();
                    for (int i = 0; i < checkedItems.length; i++) {
                        if (checkedItems[i]) {
                            eventsToRemove.add(events.get(i));
                        }
                    }

                    removeEvents(selectedDate, eventsToRemove);
                });

                builder.setNegativeButton("Cancelar", null);
                builder.show();
            } else {
                Toast.makeText(this, "No hay eventos para eliminar.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para agregar un evento
    private void addEvent(String date, String eventName) {
        // Agrega el evento a la lista de eventos para la fecha
        List<String> events = eventsMap.getOrDefault(date, new ArrayList<>());
        events.add(eventName);
        eventsMap.put(date, events);

        // Actualiza la lista de eventos para la fecha seleccionada
        updateEventsList(date);
    }

    // Método para eliminar eventos
    private void removeEvents(String date, List<String> eventsToRemove) {
        // Elimina los eventos seleccionados de la lista de eventos para la fecha
        List<String> events = eventsMap.getOrDefault(date, new ArrayList<>());
        events.removeAll(eventsToRemove);
        eventsMap.put(date, events);

        // Actualiza la lista de eventos para la fecha seleccionada
        updateEventsList(date);
    }

    // Método para actualizar la lista de eventos para la fecha seleccionada
    private void updateEventsList(String date) {
        // Actualiza la lista de eventos para la fecha seleccionada
        eventsList.clear();
        if (eventsMap.containsKey(date)) {
            eventsList.addAll(eventsMap.get(date));
        }

        eventsAdapter.notifyDataSetChanged();
    }
}
