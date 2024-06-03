package com.example.tfg.API;

import android.util.Log;

import com.example.tfg.domain.Comidas;
import com.example.tfg.domain.Comprable;
import com.example.tfg.domain.DiasSemana;
import com.example.tfg.domain.Ejercicio;
import com.example.tfg.domain.MessageDTO;
import com.example.tfg.domain.Producto;
import com.example.tfg.domain.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UtilJSONParser {

    // Análisis de una cadena JSON que representa una lista de DiasSemana
    public static List<DiasSemana> parseArrayDiasSemana(String strJson) {
        List<DiasSemana> list = new ArrayList<>();
        try {
            Log.d("UtilJSONParser", "JSON recibido para parseArrayDiasSemana: " + strJson);
            JSONArray arrayDiasSemana = new JSONArray(strJson);

            // Iterar sobre cada objeto en el array
            for (int i = 0; i < arrayDiasSemana.length(); i++) {
                JSONObject diaSemanaJson = arrayDiasSemana.getJSONObject(i);
                DiasSemana diaSemana = parseDiasSemana(diaSemanaJson.toString());
                list.add(diaSemana);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error al analizar el JSON de DiasSemana: " + strJson, e);
        }
        return list;
    }

    // Análisis de una cadena JSON que representa un objeto DiasSemana
    public static DiasSemana parseDiasSemana(String strJson) {
        try {
            JSONObject jsonObject = new JSONObject(strJson);

            // Leer el campo id del JSON
            int id = jsonObject.optInt("id", -1);

            // Leer otros campos del JSON
            String titulo = jsonObject.optString("titulo", "");
            String pic = jsonObject.optString("pic", "");

            // Verificar si los campos comidas y ejercicios existen y no son nulos
            JSONArray comidasJsonArray = jsonObject.optJSONArray("comidas");
            List<Comidas> comidas = comidasJsonArray != null ? parseComidas(comidasJsonArray.toString()) : null;

            JSONArray ejerciciosJsonArray = jsonObject.optJSONArray("ejercicios");
            List<Ejercicio> ejercicios = ejerciciosJsonArray != null ? parseEjercicios(ejerciciosJsonArray.toString()) : null;

            // Extraer usuarioId del JSON si existe
            int usuarioId = jsonObject.optInt("usuarioId", -1);

            // Crear una instancia de DiasSemana utilizando el constructor que incluye todos los campos
            return new DiasSemana(id, titulo, pic, comidas, ejercicios, usuarioId);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Error al analizar el JSON de DiasSemana: " + strJson, e);
        }
    }

    // Análisis de una cadena JSON que representa una lista de Comidas
    public static List<Comidas> parseComidas(String strJson) {
        List<Comidas> listaComidas = new ArrayList<>();
        try {
            JSONArray comidasArray = new JSONArray(strJson);
            for (int i = 0; i < comidasArray.length(); i++) {
                JSONObject comidaJson = comidasArray.getJSONObject(i);
                // Leer el campo id del JSON
                int id = comidaJson.optInt("id");
                String titulo = comidaJson.optString("titulo");
                String pic = comidaJson.optString("pic");
                double kcal = comidaJson.optDouble("kcal");
                double proteinas = comidaJson.optDouble("proteinas");
                double gramos = comidaJson.optDouble("gramos");
                double carbohidratos = comidaJson.optDouble("carbohidratos");

                // Crear la lista de productos
                JSONArray productosJsonArray = comidaJson.optJSONArray("productos");
                List<Producto> productos = new ArrayList<>();

                if (productosJsonArray != null) {
                    productos = parseProductos(productosJsonArray.toString());
                }

                // Crear un objeto Comidas con los datos extraídos, incluyendo el campo id
                Comidas comida = new Comidas(id, titulo, pic, kcal, proteinas, gramos, carbohidratos, productos);

                // Agregar la comida a la lista de comidas
                listaComidas.add(comida);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Considera manejar el error de análisis JSON según sea necesario.
        }
        return listaComidas;
    }

    // Análisis de una lista de productos a partir de un JSONArray
    public static Comidas parseComida(JSONObject comidaJson) throws JSONException {
        String titulo = comidaJson.optString("titulo");
        String pic = comidaJson.optString("pic");
        double kcal = comidaJson.optDouble("kcal");
        double proteinas = comidaJson.optDouble("proteinas");
        double gramos = comidaJson.optDouble("gramos");
        double carbohidratos = comidaJson.optDouble("carbohidratos");

        JSONArray productosArray = comidaJson.optJSONArray("productos");
        List<Producto> productos = parseProductos(productosArray.toString());

        return new Comidas(titulo, pic, kcal, proteinas, gramos, carbohidratos, productos);
    }

    // Análisis de una cadena JSON que representa una lista de productos
    public static List<Producto> parseProductos(String strJson) {
        List<Producto> listaProductos = new ArrayList<>();
        try {
            // Convierte la cadena JSON en un JSONArray
            JSONArray productosArray = new JSONArray(strJson);

            // Itera a través de cada objeto de producto en el array
            for (int i = 0; i < productosArray.length(); i++) {
                JSONObject productoJson = productosArray.getJSONObject(i);
                Producto producto = parseProducto(productoJson);
                listaProductos.add(producto);
            }
        } catch (JSONException e) {
            handleJSONException("Error al analizar JSON de productos", e);
        }
        return listaProductos;
    }
    public static Producto parseProducto(String strJson) {
        try {
            // Convierte la cadena JSON en un objeto JSONObject
            JSONObject productoJson = new JSONObject(strJson);

            // Extrae los campos del producto desde el objeto JSON
            int id = productoJson.optInt("id");
            String titulo = productoJson.optString("titulo");
            String marca = productoJson.optString("marca");
            Integer cantGramos = productoJson.optInt("cantGramos");
            double kcal = productoJson.optDouble("kcal");
            double grasas = productoJson.optDouble("grasas");
            double carbohidratos = productoJson.optDouble("carbohidratos");
            double proteinas = productoJson.optDouble("proteinas");

            // Extrae la URL de la imagen
            String urlImagen = productoJson.optString("urlImagen", "");

            // Crea un objeto Producto con los datos extraídos
            return new Producto(id, titulo, marca, cantGramos, kcal, grasas, carbohidratos, proteinas, urlImagen);
        } catch (JSONException e) {
            // Maneja errores de análisis JSON
            e.printStackTrace();
            throw new IllegalArgumentException("Error al analizar JSON de producto: " + strJson, e);
        }
    }
    // Análisis de un objeto JSON que representa un Producto
    public static Producto parseProducto(JSONObject productoJson) throws JSONException {
        int id = productoJson.optInt("id");
        String titulo = productoJson.optString("titulo");
        String marca = productoJson.optString("marca");
        Integer cantGramos = productoJson.optInt("cantGramos");
        double kcal = productoJson.optDouble("kcal");
        double grasas = productoJson.optDouble("grasas");
        double carbohidratos = productoJson.optDouble("carbohidratos");
        double proteinas = productoJson.optDouble("proteinas");

        // Obteniendo el campo urlImagen
        String urlImagen = productoJson.optString("urlImagen", "");

        // Creando un objeto Producto con los datos obtenidos
        return new Producto(id, titulo, marca, cantGramos, kcal, grasas, carbohidratos, proteinas, urlImagen);
    }

    // Manejo de errores de JSON
    private static void handleJSONException(String message, JSONException e) {
        e.printStackTrace();
        throw new IllegalArgumentException(message, e);
    }


    // Análisis de una cadena JSON que representa una lista de Ejercicios
    // Análisis de una cadena JSON que representa una lista de Ejercicios
    public static List<Ejercicio> parseEjercicios(String json) {
        List<Ejercicio> listaEjercicios = new ArrayList<>();
        try {
            // Convierte la cadena JSON en un JSONArray
            JSONArray ejerciciosArray = new JSONArray(json);

            // Iterar a través de cada objeto de ejercicio en el array
            for (int i = 0; i < ejerciciosArray.length(); i++) {
                JSONObject ejercicioJson = ejerciciosArray.getJSONObject(i);

                // Extrae los detalles de cada ejercicio
                int id = ejercicioJson.optInt("id", -1); // Leer el campo id si existe
                String nombre = ejercicioJson.optString("nombre", "");
                String grupoMuscular = ejercicioJson.optString("grupoMuscular", "");
                String urlVideo = ejercicioJson.optString("urlVideo", "");

                // Crea una instancia de Ejercicio con los datos extraídos, incluyendo el campo id
                Ejercicio ejercicio = new Ejercicio(id, nombre, grupoMuscular, urlVideo);

                // Agrega el ejercicio a la lista de ejercicios
                listaEjercicios.add(ejercicio);
            }
        } catch (JSONException e) {
            // Maneja cualquier excepción durante el análisis JSON
            handleJSONException("Error al analizar JSON de ejercicios", e);
        }
        return listaEjercicios;
    }

    // Método para crear un objeto JSON que representa un post
    public static JSONObject createDiasSemana(int id, String title, String pic) {
        JSONObject jsonPost = new JSONObject();
        try {
            jsonPost.put("titulo", title);
            jsonPost.put("pic", pic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonPost;
    }

    public static JSONObject createUserJSON(Usuario user) {
        // Crea un objeto JSON vacío
        JSONObject jsonUser = new JSONObject();

        try {
            if (user != null) {
                // Agrega los campos del objeto Usuario al objeto JSON
                jsonUser.put("password", user.getPassword());
                jsonUser.put("name", user.getName());
                jsonUser.put("email", user.getEmail());
                jsonUser.put("genero", user.getGenero());
                jsonUser.put("age", user.getAge());
                jsonUser.put("peso", user.getPeso());
                jsonUser.put("diasquevoy", user.getDiasquevoy());
                jsonUser.put("comidas", user.getComidas());
                jsonUser.put("image", user.getImage());
            }

          /*  // Si el usuario tiene roles, conviértelos en una lista de cadenas y añádelos al JSON
            if (user.getRoles() != null) {
                List<String> roleNames = user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toList());
                jsonUser.put("roles", roleNames);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error al crear JSON para el usuario: " + user, e);
        }

        return jsonUser;
    }


    public static List<Comprable> parseComprables(String strJson) {
        List<Comprable> listaComprables = new ArrayList<>();
        try {
            // Convierte la cadena JSON en un JSONArray
            JSONArray comprablesArray = new JSONArray(strJson);

            // Itera a través de cada objeto de comprable en el array
            for (int i = 0; i < comprablesArray.length(); i++) {
                JSONObject comprableJson = comprablesArray.getJSONObject(i);

                // Extrae los detalles de cada comprable
                String titulo = comprableJson.optString("titulo");
                String pic = comprableJson.optString("pic");
                String imagen = comprableJson.optString("imagen");
                double precio = comprableJson.optDouble("precio");

                // Crea un objeto Comprable con los datos extraídos
                Comprable comprable = new Comprable(titulo, pic, imagen, precio);

                // Agrega el comprable a la lista de comprables
                listaComprables.add(comprable);
            }
        } catch (JSONException e) {
            // Maneja errores de análisis JSON
            e.printStackTrace();
            throw new IllegalArgumentException("Error al analizar JSON de comprables", e);
        }
        return listaComprables;
    }
    // Constructor privado para prevenir la instanciación
    private UtilJSONParser() {
        throw new AssertionError();
    }

    public static Ejercicio parseEjercicio(String content) {
        Ejercicio ejercicio = null;
        try {
            // Convierte la cadena JSON en un objeto JSONObject
            JSONObject ejercicioJson = new JSONObject(content);

            // Extrae los detalles del ejercicio del JSON
            int id = ejercicioJson.optInt("id", -1); // Leer el campo id si existe
            String nombre = ejercicioJson.optString("nombre", "");
            String grupoMuscular = ejercicioJson.optString("grupoMuscular", "");
            String urlVideo = ejercicioJson.optString("urlVideo", "");

            // Crea una instancia de Ejercicio con los datos extraídos
            ejercicio = new Ejercicio(id, nombre, grupoMuscular, urlVideo);
        } catch (JSONException e) {
            // Maneja cualquier excepción durante el análisis JSON
            handleJSONException("Error al analizar JSON de ejercicio", e);
        }
        return ejercicio;
    }

    public static int parseDiaIdFromResponse(String content) {
        try {
            // Convierte la cadena JSON en un objeto JSONObject
            JSONObject jsonResponse = new JSONObject(content);

            // Extrae el ID del día de la semana
            int diaId = jsonResponse.optInt("id", -1); // Usamos -1 como valor predeterminado si el campo `id` no existe

            // Retorna el ID extraído
            return diaId;
        } catch (JSONException e) {
            // Maneja cualquier excepción durante el análisis JSON
            handleJSONException("Error al analizar JSON para extraer ID del día de la semana", e);
            return -1; // Retorna -1 en caso de error
        }
    }

    public static List<MessageDTO> parseArrayMensajes(String content) {
        List<MessageDTO> messageList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(content);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MessageDTO message = new MessageDTO();
                message.setId(jsonObject.getInt("id"));
                message.setContent(jsonObject.getString("content"));
                message.setSender(jsonObject.getString("sender"));
                messageList.add(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return messageList;
    }
}
