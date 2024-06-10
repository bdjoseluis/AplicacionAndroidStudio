package com.example.tfg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.MessageAdapter2;
import com.example.tfg.domain.MessageDTO;

import java.util.ArrayList;
import java.util.List;

public class Comunidad extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button buttonSend;
    private MessageAdapter2 messageAdapter;
    private List<MessageDTO> messageList;
    String username;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comunidad);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString("username", "");
        Log.d("USERNAME", username);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages4);
        editTextMessage = findViewById(R.id.editTextMessage4);
        buttonSend = findViewById(R.id.buttonSend4);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter2(messageList, username);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        descargarMensajes();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // Enviar el mensaje a través de la API
                    sendMessage(messageText);

                    // Limpiar el EditText después de enviar el mensaje
                    editTextMessage.setText("");
                }
            }
        });
    }

    private void descargarMensajes() {
        API.getAllMessages(new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    List<MessageDTO> messageDTOS = UtilJSONParser.parseArrayMensajes(r.content);
                    messageList.clear();
                    messageList.addAll(messageDTOS);
                    messageAdapter.notifyDataSetChanged();
                    Toast.makeText(Comunidad.this, "Mensajes descargados correctamente.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(getClass().getName(), "Error al analizar el contenido JSON", e);
                    Toast.makeText(Comunidad.this, "Error al analizar el contenido JSON", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
                Log.e(getClass().getName(), "Error al descargar los mensajes: " + r.exception);
                Toast.makeText(Comunidad.this, "Error al descargar los mensajes: " + r.exception, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendMessage(String messageText) {
        API.sendMessage(new MessageDTO(messageText, username), new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                // Éxito al enviar el mensaje, actualiza la lista de mensajes
                descargarMensajes();
            }

            @Override
            public void onError(UtilREST.Response r) {
                // Error al enviar el mensaje, muestra un mensaje de error
                Log.e(getClass().getName(), "Error al enviar el mensaje: " + r.exception);
                Toast.makeText(Comunidad.this, "Error al enviar el mensaje: " + r.exception, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

