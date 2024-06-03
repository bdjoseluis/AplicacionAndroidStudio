package com.example.tfg.API;

import android.util.Log;

import com.example.tfg.domain.Comidas;
import com.example.tfg.domain.DiasSemana;
import com.example.tfg.domain.MessageDTO;
import com.example.tfg.domain.Producto;
import com.example.tfg.domain.Role;
import com.example.tfg.domain.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class API {
    private static final String BASE_URL = "http://192.168.0.240:5000/api/";
    private static final String BASE_URLUser = "http://192.168.0.240:5000/auth/";

    // Registro de usuario
    public static void registerUser(Usuario newUser, UtilREST.OnResponseListener listener) {
        if (newUser == null) {
            Log.e("API", "El objeto Usuario es nulo.");
            throw new IllegalArgumentException("El objeto Usuario no puede ser nulo.");
        }

        String url = BASE_URLUser + "signup";
        Log.d("API", "URL para registrar usuario: " + url);

        // Convierte el objeto Usuario a JSONObject
        JSONObject jsonUser = convertirUsuarioAJSONObject(newUser);

        // Registrar el contenido de jsonUser como string
        Log.d("API", "Datos del nuevo usuario: " + jsonUser.toString());

        // Ejecutar la consulta
        UtilREST.runQuery(UtilREST.QueryType.POST, url, jsonUser.toString(), listener);
    }

    // Función para convertir un objeto Usuario a JSONObject
    private static JSONObject convertirUsuarioAJSONObject(Usuario usuario) {
        JSONObject jsonUsuario = new JSONObject();
        try {
            jsonUsuario.put("password", usuario.getPassword());
            jsonUsuario.put("name", usuario.getName());
            jsonUsuario.put("email", usuario.getEmail());
            jsonUsuario.put("genero", usuario.getGenero());
            jsonUsuario.put("age", usuario.getAge());
            jsonUsuario.put("peso", usuario.getPeso());
            jsonUsuario.put("diasquevoy", usuario.getDiasquevoy());
            jsonUsuario.put("comidas", usuario.getComidas());
            jsonUsuario.put("image", usuario.getImage());

            // Convertir roles a lista de IDs
            List<Integer> rolIds = new ArrayList<>();
            List<Role> roles = usuario.getRoles();
            if (roles != null) {
                for (Role role : roles) {
                    // Dependiendo del tipo de rol, asigna el ID correcto
                    switch (role) {
                        case ADMIN:
                            rolIds.add(1);
                            break;
                        case USUARIO:
                            rolIds.add(2);
                            break;
                        case REGISTRADO:
                            rolIds.add(3);
                            break;
                    }
                }
            }

            // Crear un nuevo JSONArray para los IDs de rol y agregar los IDs
            JSONArray roleIdsArray = new JSONArray(rolIds);
            jsonUsuario.put("rolIds", roleIdsArray);
            jsonUsuario.put("role_id", roleIdsArray);

        } catch (JSONException e) {
            Log.e("API", "Error al crear JSON para el usuario: " + usuario, e);
            throw new IllegalArgumentException("Error al crear JSON para el usuario: " + usuario, e);
        }
        return jsonUsuario;
    }

    public static void iniciarSesion(String email, String password, UtilREST.OnResponseListener listener) {
        // Verifica si el email o la contraseña están vacíos
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            Log.e("API", "El correo electrónico o la contraseña están vacíos.");
            throw new IllegalArgumentException("El correo electrónico y la contraseña no pueden estar vacíos.");
        }

        // URL de la API de inicio de sesión
        String url = BASE_URLUser + "login";

        // Crear un objeto JSON para enviar el email y la contraseña
        JSONObject jsonLogin = new JSONObject();
        try {
            jsonLogin.put("email", email);
            jsonLogin.put("password", password);
            jsonLogin.put("username", email);

        } catch (JSONException e) {
            Log.e("API", "Error al crear JSON para iniciar sesión: " + e.getMessage());
            throw new IllegalArgumentException("Error al crear JSON para iniciar sesión.");
        }

        // Realizar la solicitud POST a la API de inicio de sesión
        UtilREST.runQuery(UtilREST.QueryType.POST, url, jsonLogin.toString(), listener);
    }

    public static void updateUser(int userId, Usuario updatedUser, UtilREST.OnResponseListener listener) {
        // Verificar si el objeto actualizado es nulo
        if (updatedUser == null) {
            Log.e("API", "El objeto Usuario actualizado es nulo.");
            throw new IllegalArgumentException("El objeto Usuario actualizado no puede ser nulo.");
        }

        // Construir la URL para la solicitud de actualización utilizando la base URL y el ID del usuario
        String url = BASE_URLUser + "updateUser";
        Log.d("API", "URL para actualizar usuario: " + url);

        // Crear un objeto UpdateUserDTO para enviar los datos del usuario actualizado
        JSONObject jsonUpdateUser = convertirUsuarioAUpdateUserDTO(updatedUser, userId);

        // Registrar el contenido de jsonUpdateUser como string
        Log.d("API", "Datos del usuario actualizado: " + jsonUpdateUser.toString());

        // Ejecutar la consulta PUT para actualizar el usuario
        UtilREST.runQuery(UtilREST.QueryType.PUT, url, jsonUpdateUser.toString(), listener);
    }

    // Función para convertir un objeto Usuario a JSONObject para UpdateUserDTO
    private static JSONObject convertirUsuarioAUpdateUserDTO(Usuario usuario, int userId) {
        // Crear un objeto JSON para UpdateUserDTO
        JSONObject jsonUpdateUser = new JSONObject();
        try {
            // Agregar el ID del usuario
            jsonUpdateUser.put("id", userId);
            // Agregar los campos que se encuentran en UpdateUserDTO
            jsonUpdateUser.put("password", usuario.getPassword());
            jsonUpdateUser.put("name", usuario.getName());
            jsonUpdateUser.put("email", usuario.getEmail());
            jsonUpdateUser.put("genero", usuario.getGenero());
            jsonUpdateUser.put("age", usuario.getAge());
            jsonUpdateUser.put("peso", usuario.getPeso());
            jsonUpdateUser.put("diasquevoy", usuario.getDiasquevoy());
            jsonUpdateUser.put("comidas", usuario.getComidas());
            jsonUpdateUser.put("image", usuario.getImage());
            // Crear un JSONArray para almacenar los IDs de los días de la semana
            JSONArray diasSemanaIdsArray = new JSONArray();
            // Recorrer la lista de DiasSemana y agregar solo los IDs
            for (Integer dia : usuario.getDiasSemanaIds()) {
                diasSemanaIdsArray.put(dia);
            }
            // Agregar el JSONArray de IDs de días de la semana al objeto JSON principal
           // jsonUpdateUser.put("diasSemanaIds", diasSemanaIdsArray);

        } catch (JSONException e) {
            Log.e("API", "Error al crear JSON para UpdateUserDTO: " + usuario, e);
            throw new IllegalArgumentException("Error al crear JSON para UpdateUserDTO: " + usuario, e);
        }

        // Retornar el objeto JSON creado
        return jsonUpdateUser;
    }

    public static void getUserById(int userId, UtilREST.OnResponseListener listener) {
        // Verificar si el userId es menor o igual a 0, que indicaría un ID no válido
        if (userId <= 0) {
            Log.e("API", "El ID de usuario no es válido.");
            throw new IllegalArgumentException("El ID de usuario debe ser un número positivo.");
        }

        // Construir la URL para obtener el usuario por ID
        String url = BASE_URLUser + "userById/" + userId; // Ajusta la URL según tu backend
        Log.d("API", "URL para obtener usuario por ID: " + url);

        // Ejecutar la consulta GET para obtener el usuario por ID
        UtilREST.runQuery(UtilREST.QueryType.GET, url, null, listener);
    }

    public static void getUserIdByEmail(String email, UtilREST.OnResponseListener listener) {
        // Verificar si el email es nulo o vacío
        if (email == null || email.isEmpty()) {
            Log.e("API", "El correo electrónico es nulo o está vacío.");
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo o estar vacío.");
        }

        // Construir la URL para obtener el usuario por correo electrónico
        String url = BASE_URLUser + "userByEmail/" + email; // Ajusta la URL según tu backend
        Log.d("API", "URL para obtener usuario por email: " + url);

        // Ejecutar la consulta GET para obtener el usuario por correo electrónico
        UtilREST.runQuery(UtilREST.QueryType.GET, url, null, listener);
    }

    public static void getProductoPorId(long id, UtilREST.OnResponseListener listener) {
        // Construir la URL para obtener el producto por su ID
        String url = BASE_URL + "productosporid/" + id;
        Log.d("API", "URL para obtener producto por ID: " + url);

        // Ejecutar la consulta GET para obtener el producto con el ID especificado
        UtilREST.runQuery(UtilREST.QueryType.GET, url, null, listener);
    }


    // Obtener días de la semana
    public static void getDiasSemanas(UtilREST.OnResponseListener listener) {
        UtilREST.runQuery(UtilREST.QueryType.GET, BASE_URL + "diasSemana", listener);
    }

    public static void getAllMessages(UtilREST.OnResponseListener listener){
        UtilREST.runQuery(UtilREST.QueryType.GET, BASE_URL + "messages", listener);
    }
    public static void sendMessage(MessageDTO messageDTO, UtilREST.OnResponseListener listener) {
        try {
            // Convertir el objeto MessageDTO a JSON
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("sender", messageDTO.getSender());
            jsonMessage.put("content", messageDTO.getContent());


            // Ejecutar la consulta POST al servidor
            UtilREST.runQuery(UtilREST.QueryType.POST, BASE_URL + "sendMessage", jsonMessage.toString(), listener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void getEjerciciosPorGrupoMuscular(String grupoMuscular, UtilREST.OnResponseListener listener) {
        if (grupoMuscular == null || grupoMuscular.isEmpty()) {
            throw new IllegalArgumentException("El grupo muscular no puede estar vacío.");
        }

        // Convierte los espacios en el nombre del grupo muscular a guiones bajos o guiones
        // Esto facilita el uso de nombres URL-friendly y evita problemas con %20
        String grupoMuscularFormatted = grupoMuscular.replaceAll(" ", "_").toLowerCase();

        // Construye la URL para la consulta de ejercicios por grupo muscular
        String url = BASE_URL + "ejerciciosmusculo/" + grupoMuscularFormatted;

        // Ejecuta la consulta usando UtilREST.runQuery
        UtilREST.runQuery(UtilREST.QueryType.GET, url, listener);
    }

    public static void crearProducto(Producto nuevoProducto, UtilREST.OnResponseListener listener) {
        // Verifica si el objeto Producto es nulo
        if (nuevoProducto == null) {
            throw new IllegalArgumentException("El objeto Producto no puede ser nulo.");
        }

        // Construye la URL para crear el producto
        String url = BASE_URL + "productocrear";

        JSONObject productoJson = convertirProductoAJSONObject(nuevoProducto);

        // Realiza la consulta POST a la API para crear el producto
        UtilREST.runQuery(UtilREST.QueryType.POST, url, productoJson.toString(), listener);
    }

    private static JSONObject convertirProductoAJSONObject(Producto productoDTO) {
        JSONObject jsonProducto = new JSONObject();

        try {
            // Convierte las propiedades de CreateProductoDTO a valores JSON
            jsonProducto.put("titulo", productoDTO.getTitulo());
            jsonProducto.put("marca", productoDTO.getMarca());
            jsonProducto.put("cantGramos", productoDTO.getCantGramos());
            jsonProducto.put("kcal", productoDTO.getKcal());
            jsonProducto.put("grasas", productoDTO.getGrasas());
            jsonProducto.put("carbohidratos", productoDTO.getCarbohidratos());
            jsonProducto.put("proteinas", productoDTO.getProteinas());
            jsonProducto.put("urlImagen", productoDTO.getUrlImagen());

            // Añade otros campos si es necesario

        } catch (JSONException e) {
            Log.e("API", "Error al convertir CreateProductoDTO a JSON: " + productoDTO, e);
            throw new IllegalArgumentException("Error al convertir CreateProductoDTO a JSON: " + productoDTO, e);
        }

        return jsonProducto;
    }
    public static void crearComidas(List<Comidas> comidas, int diaSemanaId, UtilREST.OnResponseListener listener) {
        // Validar entradas
        if (comidas == null || comidas.isEmpty()) {
            throw new IllegalArgumentException("La lista de comidas no puede estar vacía.");
        }

        if (diaSemanaId <= 0) {
            throw new IllegalArgumentException("El ID del día de la semana debe ser un valor positivo.");
        }

        // URL de la API
        String url = BASE_URL + "crearComidas";

        // Convertir la lista de comidas a un JSONArray
        JSONArray comidasArray = new JSONArray();
        try {
            for (Comidas comida : comidas) {
                // Crear un objeto JSON para cada comida
                JSONObject comidaJson = new JSONObject();
                comidaJson.put("titulo", comida.getTitulo());
                comidaJson.put("pic", comida.getPic());
                comidaJson.put("kcal", comida.getKcal());
                comidaJson.put("proteinas", comida.getProteinas());
                comidaJson.put("gramos", comida.getGramos());
                comidaJson.put("carbohidratos", comida.getCarbohidratos());
                comidaJson.put("diaSemanaId", diaSemanaId);

                // Añadir el objeto JSON a la lista
                comidasArray.put(comidaJson);
            }
        } catch (JSONException e) {
            Log.e("API", "Error al crear JSON para las comidas: " + e.getMessage());
            throw new IllegalArgumentException("Error al crear JSON para las comidas.");
        }

        // Realizar la solicitud POST
        UtilREST.runQuery(UtilREST.QueryType.POST, url, comidasArray.toString(), listener);
    }
    public static void crearDiaSemana(DiasSemana diaSemana, int userId, UtilREST.OnResponseListener listener) {
        // Verificar si el objeto DiasSemana es nulo
        if (diaSemana == null) {
            Log.e("API", "El objeto DiasSemana es nulo.");
            throw new IllegalArgumentException("El objeto DiasSemana no puede ser nulo.");
        }

        // Construir la URL para crear un nuevo día de la semana
        String url = BASE_URL + "addDiaSemana";

        // Convertir el objeto DiasSemana a JSON
        JSONObject diaSemanaJSON = new JSONObject();
        try {
            diaSemanaJSON.put("titulo", diaSemana.getTitulo());
            diaSemanaJSON.put("pic", diaSemana.getPic());
            JSONArray ejerciciosJSON = new JSONArray();
            JSONArray comidasJSON = new JSONArray();
            diaSemanaJSON.put("ejercicios", ejerciciosJSON);
            diaSemanaJSON.put("comidas", comidasJSON);
            diaSemanaJSON.put("usuarioId", userId);

        } catch (JSONException e) {
            Log.e("API", "Error al convertir el objeto DiasSemana a JSON: " + e.getMessage());
        }

        // Registrar la URL que se está utilizando para crear el día de la semana
        Log.d("API", "URL para crear día de la semana: " + url);

        // Realizar la consulta POST para crear el día de la semana
        UtilREST.runQuery(UtilREST.QueryType.POST, url, diaSemanaJSON.toString(), listener);
    }


    // Obtener ejercicios de un día de la semana
    public static void getEjerciciosSemana(int diaSeleccionado, UtilREST.OnResponseListener listener) {
        if (diaSeleccionado == -1) {
            throw new IllegalArgumentException("El día seleccionado no puede estar vacío.");
        }

        UtilREST.runQuery(UtilREST.QueryType.GET, BASE_URL + "diasSemana/" + diaSeleccionado + "/ejercicios", listener);
    }

    // Buscar comprable por nombre
    public static void getComprableByName(String nombreComprable, UtilREST.OnResponseListener listener) {
        if (nombreComprable == null || nombreComprable.isEmpty()) {
            throw new IllegalArgumentException("El nombre del comprable no puede estar vacío.");
        }

        String url = BASE_URL + "comprables/search?nombre=" + nombreComprable;
        UtilREST.runQuery(UtilREST.QueryType.GET, url, listener);
    }
    // Método para obtener ejercicios por nombre parcial
    public static void getEjerciciosPorNombreParcial(String nombreParcial, UtilREST.OnResponseListener listener) {
        if (nombreParcial == null || nombreParcial.isEmpty()) {
            throw new IllegalArgumentException("El nombre parcial no puede estar vacío.");
        }

        // Construye la URL para la consulta de ejercicios por nombre parcial
        String url = BASE_URL + "ejercicios/search?nombre=" + nombreParcial;

        // Ejecuta la consulta usando UtilREST.runQuery
        UtilREST.runQuery(UtilREST.QueryType.GET, url, listener);
    }
    // Añadir ejercicio a un día de la semana
    public static void addEjercicioToDiaSemana(int diaSeleccionado, String nombreEjercicio, UtilREST.OnResponseListener listener) {
        // Verifica que los parámetros no sean nulos o vacíos
        if (diaSeleccionado == -1 || nombreEjercicio == null || nombreEjercicio.isEmpty()) {
            listener.onError(new UtilREST.Response());
            return;
        }

        // Obtén el ID del ejercicio seleccionado utilizando el método getEjercicioId
        getEjercicioId(nombreEjercicio, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response response) {
                try {
                    // Extrae el ID del ejercicio de la respuesta
                    JSONObject jsonResponse = new JSONObject(response.content);
                    int ejercicioId = jsonResponse.getInt("id");

                    Log.d(getClass().getName(), "dia Semana " + diaSeleccionado+" ejercicio"+ejercicioId);
                    // Construye la URL para agregar el ejercicio al día de la semana
                    String url = BASE_URL + "diasSemana/" + diaSeleccionado + "/ejercicio/" + ejercicioId;
                    // Realiza la consulta POST para agregar el ejercicio al día de la semana
                    UtilREST.runQuery(UtilREST.QueryType.POST, url, null, new UtilREST.OnResponseListener() {
                        @Override
                        public void onSuccess(UtilREST.Response response) {
                            // Una vez que se ha añadido el ejercicio correctamente, llama al método para obtener la lista de ejercicios actualizada
                            getEjerciciosSemana(diaSeleccionado, listener);
                        }

                        @Override
                        public void onError(UtilREST.Response response) {
                            listener.onError(response);
                        }
                    });
                } catch (JSONException e) {
                    // Maneja la excepción de JSON que pueda ocurrir
                    listener.onError(new UtilREST.Response());
                }
            }

            @Override
            public void onError(UtilREST.Response response) {
                // Pasa el error al listener
                listener.onError(response);
            }
        });
    }


    // Obtener ID de ejercicio por nombre
    public static void getEjercicioId(String ejercicioSeleccionado, UtilREST.OnResponseListener listener) {
        if (ejercicioSeleccionado == null || ejercicioSeleccionado.isEmpty()) {
            throw new IllegalArgumentException("El nombre del ejercicio no puede estar vacío.");
        }

        String url = BASE_URL + "ejercicios/" + ejercicioSeleccionado;
        UtilREST.runQuery(UtilREST.QueryType.GET, url, listener);
    }

    // Eliminar ejercicio de un día de la semana
    public static void removeEjercicioFromDiaSemana(int diaSeleccionado, int ejercicioId, UtilREST.OnResponseListener listener) {
        // Verifica que los parámetros sean válidos
        if (diaSeleccionado == -1 || ejercicioId <= 0) {
            throw new IllegalArgumentException("El día seleccionado o ID de ejercicio no pueden estar vacíos.");
        }
        Log.d(API.class.getName(), "Parámetros válidos: diaSeleccionado=" + diaSeleccionado + ", ejercicioId=" + ejercicioId);

        Log.d(API.class.getName(), "Día de la semana convertido a número: " + diaSeleccionado);

        // Construye la URL para desasociar el ejercicio del día de la semana
        String url = BASE_URL + "diasSemana/" + diaSeleccionado + "/desasociarEjercicio/" + ejercicioId;
        Log.d(API.class.getName(), "URL construida: " + url);

        // Realiza la consulta DELETE para eliminar la asociación
        UtilREST.runQuery(UtilREST.QueryType.DELETE, url, listener);
        Log.d(API.class.getName(), "Consulta DELETE enviada para eliminar ejercicio de un día de la semana.");

    }

    // Obtener comidas de un día de la semana
    public static void getComidasDeDiaSemana(int diaSeleccionado, UtilREST.OnResponseListener listener) {

        UtilREST.runQuery(UtilREST.QueryType.GET, BASE_URL + "diasSemana/" + diaSeleccionado + "/comidas", listener);
    }
    public static void getProductosPorNombreParcial(String nombreParcial, UtilREST.OnResponseListener listener) {
        if (nombreParcial == null || nombreParcial.isEmpty()) {
            throw new IllegalArgumentException("El nombre parcial no puede estar vacío.");
        }

        // Construye la URL para la consulta de productos por nombre parcial
        String url = BASE_URL + "productos/search?nombre=" + nombreParcial;

        // Ejecuta la consulta usando UtilREST.runQuery
        UtilREST.runQuery(UtilREST.QueryType.GET, url, listener);
    }
    // Obtener lista de comprables
    public static void getComprables(UtilREST.OnResponseListener listener) {
        String url = BASE_URL + "comprables";
        UtilREST.runQuery(UtilREST.QueryType.GET, url, listener);
    }

    // Eliminar la asociación de un ejercicio de un día de la semana
    public static void asociarProductoAComida(int comidaId, int productoId, UtilREST.OnResponseListener listener) {
        // Verifica que los IDs de comida y producto sean válidos
        if (comidaId <= 0 || productoId <= 0) {
            Log.d("AdaptadorProductos", "Comida ID: " + comidaId + ", Producto ID: " + productoId);
            throw new IllegalArgumentException("ID de comida o producto no válidos.");
        }

        // Construye la URL para asociar el producto a la comida
        String url = BASE_URL + "comidas/" + comidaId + "/productos/" + productoId;

        // Realiza la consulta POST para asociar el producto a la comida
        UtilREST.runQuery(UtilREST.QueryType.POST, url, null, listener);
    }


    public static void getProductosDeComida(int comidaId, UtilREST.OnResponseListener listener) {
        // Verifica que el ID de la comida sea válido
        if (comidaId <= 0) {
            throw new IllegalArgumentException("ID de comida no válido.");
        }

        // Construye la URL para obtener los productos de la comida
        String url = BASE_URL + "comidas/" + comidaId + "/productos";

        // Realiza la consulta GET para obtener los productos de la comida
        UtilREST.runQuery(UtilREST.QueryType.GET, url, listener);
    }

    public static void getProductoPorMarca(String marca, UtilREST.OnResponseListener listener) {
        // Verificar si la marca está vacía o es nula
        if (marca == null || marca.isEmpty()) {
            Log.e("API", "La marca está vacía o es nula.");
            throw new IllegalArgumentException("La marca no puede estar vacía o ser nula.");
        }

        // Construir la URL para obtener productos por la marca
        String url = BASE_URL + "productos/" + marca;
        Log.d("API", "URL para obtener productos por marca: " + url);

        // Ejecutar la consulta GET para obtener los productos por la marca especificada
        UtilREST.runQuery(UtilREST.QueryType.GET, url, null, listener);
    }

    public static void desasociarProductoDeComida(int comidaId, int productoId, UtilREST.OnResponseListener listener) {
        // Verifica que los IDs de comida y producto sean válidos
        if (comidaId <= 0 || productoId <= 0) {
            throw new IllegalArgumentException("ID de comida o producto no válidos.");
        }

        // Construye la URL para desasociar el producto de la comida
        String url = BASE_URL + "comidas/" + comidaId + "/productos/" + productoId;

        // Realiza la consulta DELETE para desasociar el producto de la comida
        UtilREST.runQuery(UtilREST.QueryType.DELETE, url, null, listener);
    }

    public static void actualizarComida(int comidaId, double totalKcal, double totalProteinas, double totalGramos, double totalCarbohidratos, UtilREST.OnResponseListener listener) {
        // Verifica que el ID de la comida sea válido
        if (comidaId <= 0) {
            Log.e("API", "ID de comida no válido: " + comidaId);
            throw new IllegalArgumentException("ID de comida no válido.");
        }

        // Construye la URL para actualizar la comida
        String url = BASE_URL + "comidaUpdate/" + comidaId;
        Log.d("API", "URL de actualización de comida: " + url);

        // Crea un objeto JSON con los datos de la comida actualizada
        JSONObject jsonComidaActualizada = new JSONObject();
        try {
            jsonComidaActualizada.put("totalKcal", totalKcal);
            jsonComidaActualizada.put("totalProteinas", totalProteinas);
            jsonComidaActualizada.put("totalGramos", totalGramos);
            jsonComidaActualizada.put("totalCarbohidratos", totalCarbohidratos);
            Log.d("API", "Datos JSON de comida actualizada: " + jsonComidaActualizada.toString());
        } catch (JSONException e) {
            Log.e("API", "Error al crear JSON para la comida actualizada: " + e.getMessage());
            throw new IllegalArgumentException("Error al crear JSON para la comida actualizada.");
        }
        Log.d("API", "Iniciando solicitud PUT para actualizar comida...");
        // Realiza la consulta PUT para actualizar la comida
        UtilREST.runQuery(UtilREST.QueryType.PUT, url, jsonComidaActualizada.toString(), listener);
    }

    public static void deleteProductoPorId(int productoId, UtilREST.OnResponseListener listener) {
        // Verifica que el ID del producto sea válido
        if (productoId <= 0) {
            Log.e("API", "ID de producto no válido: " + productoId);
            throw new IllegalArgumentException("ID de producto no válido.");
        }

        // Construye la URL para eliminar el producto por su ID
        String url = BASE_URL + "productodelete/" + productoId;

        // Realiza la consulta DELETE para eliminar el producto por su ID
        UtilREST.runQuery(UtilREST.QueryType.DELETE, url, null, listener);
    }
}
