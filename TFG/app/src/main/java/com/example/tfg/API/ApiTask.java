package com.example.tfg.API;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tfg.domain.Comidas;
import com.example.tfg.domain.DiasSemana;
import com.example.tfg.domain.Producto;
import com.example.tfg.domain.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiTask<T> extends AsyncTask<String, Void, List<T>> {

    private static final String TAG = "ApiTask";
    private ApiTaskListener<T> listener;
    private Class<T> clazz;

    public ApiTask(Class<T> clazz, ApiTaskListener<T> listener) {
        this.clazz = clazz;
        this.listener = listener;
    }

    @Override
    protected List<T> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        StringBuilder resultado = new StringBuilder();
        List<T> itemList = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String linea;

            while ((linea = bufferedReader.readLine()) != null) {
                resultado.append(linea).append("\n");
            }

            try {
                JSONArray jsonArray = new JSONArray(resultado.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject itemJson = jsonArray.getJSONObject(i);
                    T item = parseJsonToItem(itemJson, clazz);
                    itemList.add(item);
                }
            } catch (Exception exception) {
                Log.e(TAG, "Error al procesar los datos del API: " + exception.getMessage());
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return itemList;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onPostExecute(List<T> result) {
        if (listener != null) {
            listener.onTaskCompleted(result);
        }
    }

    private T parseJsonToItem(JSONObject json, Class<T> clazz) {
        // Aquí debes implementar la lógica para convertir el JSON a la clase correspondiente
        // Por ejemplo, podrías utilizar Gson o Jackson para realizar esta tarea.
        // Aquí se presenta una implementación simple solo para fines de demostración:
        T item = null;
        try {
            if (clazz == Usuario.class) {
                String nombre = json.getString("name");
                String ciudad = json.getJSONObject("address").getString("city");
                item = clazz.getDeclaredConstructor(String.class, String.class).newInstance(nombre, ciudad);
            } else if (clazz == Producto.class) {
                // Lógica para convertir el JSON a Producto

            } else if (clazz == Comidas.class) {
                // Lógica para convertir el JSON a Comida
            } else if (clazz == DiasSemana.class) {
                String nombre = json.getString("nombre");
                String icono = json.getString("icono");
                item = clazz.getDeclaredConstructor(String.class, String.class).newInstance(nombre, icono);
            }
            // Agrega más condiciones según sea necesario para otros tipos de objetos
        } catch (Exception e) {
            Log.e(TAG, "Error al convertir JSON a objeto: " + e.getMessage());
        }
        return item;
    }

    public interface ApiTaskListener<T> {
        void onTaskCompleted(List<T> result);

        void onTaskCompleted(String messageJson);
    }
}
