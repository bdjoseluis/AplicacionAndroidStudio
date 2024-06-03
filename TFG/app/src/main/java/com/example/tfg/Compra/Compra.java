package com.example.tfg.Compra;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.API.API;
import com.example.tfg.API.UtilJSONParser;
import com.example.tfg.API.UtilREST;
import com.example.tfg.Adapter.AdapterCompra;
import com.example.tfg.Ajustes;
import com.example.tfg.Dieta.Dieta;
import com.example.tfg.GalleryActivity;
import com.example.tfg.Inicio;
import com.example.tfg.R;
import com.example.tfg.Rutina.Rutina;
import com.example.tfg.domain.Comprable;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Compra extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int codigofoto=1;
    private static final int PermisoCamara = 5;

    private AdapterCompra adapter;
    private RecyclerView recyclerViewCategoryList;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    List<Comprable> comprables;

    private ArrayList<Bitmap> listaFotos = new ArrayList<>();

    private boolean accesoriosSubMenuVisible = false;
    private SubMenu alimentacionSubMenu;
    private SQLiteDatabase db;
    private LinearLayout inicio;
    private LinearLayout dieta;
    private BottomAppBar bottomAppBar;

    private LinearLayout rutina;
    private LinearLayout ajustes;
    private LinearLayout compras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);
        db = openOrCreateDatabase("MisComprables", MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS carrito (titulo Varchar, pic Varchar, imagen Varchar, precio double);");

        configurarBarraHerramientas();
        configurarMenuLateral();
        configurarRecyclerView();
        descargarComprables();
        configurarBotonFlotante();
        initNavigationButtons();

        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // Si el teclado es más del 15% de la altura de la pantalla
                    hideNavigationButtons();
                } else {
                    showNavigationButtons();
                }
            }
        });
        EditText editText = findViewById(R.id.editTextText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Antes de que el texto cambie (puedes dejar esto vacío)
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Lógica de búsqueda
                String query = s.toString();
                performSearch(query);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Después de que el texto cambie (puedes dejar esto vacío)
            }
        });



        adapter.setOnAddItemClickListener(new AdapterCompra.OnAddItemClickListener() {
            @Override
            public void onAddItemClick(Comprable comprable) {
                db.execSQL("INSERT INTO carrito (titulo, pic, imagen, precio) VALUES ('" +
                        comprable.getTitulo() + "', '" +
                        comprable.getPic() + "', '" +
                        comprable.getImagen() + "', " +
                        comprable.getPrecio() + ");");

                // Muestra un Toast con el nombre del elemento seleccionado (opcional)
                String text = "Producto añadido: " + comprable.getTitulo();
                showCustomToast(text);
            }
        });
    }


    private void performSearch(String query) {
        // Verifica si la consulta de búsqueda está vacía
        if (query == null || query.isEmpty()) {
            // Si la consulta está vacía, no realices ninguna búsqueda
            return;
        }

        // Utiliza el método getComprableByName para buscar comprables por nombre
        API.getComprableByName(query, new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    // Analiza la respuesta JSON para obtener una lista de comprables
                    List<Comprable> comprablesFiltrados = UtilJSONParser.parseComprables(r.content);

                    // Actualiza el adaptador del RecyclerView con la lista filtrada
                    adapter.clear();
                    adapter.addAll(comprablesFiltrados);
                    adapter.notifyDataSetChanged();

                    // Muestra un Toast indicando éxito si es necesario
                } catch (Exception e) {
                    // Muestra un mensaje de error si no se puede analizar el contenido JSON
                    Log.e(getClass().getName(), "Error al analizar el contenido JSON", e);
                    showCustomToast("No existe el producto");
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
                // Muestra un mensaje de error si la consulta falla
            }
        });
    }

    private void configurarBarraHerramientas() {
        Toolbar toolbar = findViewById(R.id.toolbarNavegador);
        setSupportActionBar(toolbar);
    }
    private void configurarMenuLateral() {
        drawer = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbarNavegador);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void configurarBotonFlotante() {
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(Compra.this, carrito.class);
            startActivity(intent);
        });
    }


    private void initNavigationButtons() {
        bottomAppBar = findViewById(R.id.app_bar);
        inicio = findViewById(R.id.botoninicio);
        dieta = findViewById(R.id.botondieta);
        rutina = findViewById(R.id.botonrutina);
        ajustes = findViewById(R.id.botonajustes);
        compras = findViewById(R.id.botontienda);

        // Configuración de los clics de los botones de navegación
        inicio.setOnClickListener(v -> {
            startActivity(new Intent(Compra.this, Inicio.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        dieta.setOnClickListener(v -> {
            startActivity(new Intent(Compra.this, Dieta.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        rutina.setOnClickListener(v -> {
            startActivity(new Intent(Compra.this, Rutina.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        ajustes.setOnClickListener(v -> {
            startActivity(new Intent(Compra.this, Ajustes.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        compras.setOnClickListener(v -> {
            startActivity(new Intent(Compra.this, Compra.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void configurarRecyclerView() {
        recyclerViewCategoryList = findViewById(R.id.recyclerViewdecomprables);
        recyclerViewCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new AdapterCompra(comprables, this, recyclerViewCategoryList);
        //adapter = new AdapterCompra(comprables, this);
        recyclerViewCategoryList.setAdapter(adapter);
    }
    private void descargarComprables() {
        API.getComprables(new UtilREST.OnResponseListener() {
            @Override
            public void onSuccess(UtilREST.Response r) {
                try {
                    comprables = UtilJSONParser.parseComprables(r.content);
                    Log.d(getClass().getName(), "Comprables descargados: " + comprables.size());
                    adapter.clear();
                    adapter.addAll(comprables);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(getClass().getName(), "Error al analizar el contenido JSON", e);
                }
            }

            @Override
            public void onError(UtilREST.Response r) {
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fototuya:
                verificarPermisosCamara();
                break;
            case R.id.progresofisico:
                Intent progresoIntent = new Intent(Compra.this, GalleryActivity.class);
                startActivity(progresoIntent);
                break;
            case R.id.nutricion_deportiva:
                // Filtra los comprables que tengan la categoría "nutricion_deportiva"
                ArrayList<Comprable> comprablesNutricionDeportiva = filterComprablesByCategory("nutricion_deportiva");
                // Actualiza el adaptador del RecyclerView con la lista filtrada
                adapter.updateData(comprablesNutricionDeportiva);
                break;
            case R.id.salud_y_bienestar:
                // Filtra los comprables que tengan la categoría "salud_bienestar"
                ArrayList<Comprable> comprablesSaludBienestar = filterComprablesByCategory("salud_bienestar");
                // Actualiza el adaptador del RecyclerView con la lista filtrada
                adapter.updateData(comprablesSaludBienestar);
                break;
            case R.id.accesorios:
                // Si el submenú de Accesorios está abierto, lo cerramos
                if (alimentacionSubMenu != null) {
                    alimentacionSubMenu.close();
                    alimentacionSubMenu = null;
                }

                // Si el submenú de Accesorios ya está abierto, no hacemos nada
                if (accesoriosSubMenuVisible) {
                    return true;
                }

                // Si el submenú de Accesorios no está abierto, lo inflamos y lo marcamos como visible
                accesoriosSubMenuVisible = true;
                SubMenu subMenuAccesorios = item.getSubMenu();
                if (subMenuAccesorios == null) {
                    subMenuAccesorios = navigationView.getMenu().addSubMenu(R.id.accesorios, Menu.NONE, Menu.NONE, "ACCESORIOS");
                }
                getMenuInflater().inflate(R.menu.menu_accesorios, subMenuAccesorios);

                // Si el submenú de Alimentación está creado, lo eliminamos al abrir el de Accesorios
                if (alimentacionSubMenu != null) {
                    alimentacionSubMenu.close();
                    alimentacionSubMenu = null;
                }

                return true;
            case R.id.alimentacion:
                // Filtra los comprables que tengan la categoría "alimentacion"
                ArrayList<Comprable> comprablesAlimentacion = filterComprablesByCategory("alimentacion");
                // Actualiza el adaptador del RecyclerView con la lista filtrada
                adapter.updateData(comprablesAlimentacion);

                // Si el submenú de Accesorios está abierto, lo cerramos
                if (accesoriosSubMenuVisible) {
                    accesoriosSubMenuVisible = false;
                    navigationView.getMenu().removeGroup(R.id.accesorios);
                }
                break;
            case R.id.ingredientes:
                // Filtra los comprables que tengan la categoría "ingredientes"
                ArrayList<Comprable> comprablesIngredientes = filterComprablesByCategory("ingredientes");
                // Actualiza el adaptador del RecyclerView con la lista filtrada
                adapter.updateData(comprablesIngredientes);
                break;
            case R.id.novedades:
                // Filtra los comprables que tengan la categoría "novedades"
                ArrayList<Comprable> comprablesNovedades = filterComprablesByCategory("novedades");
                // Actualiza el adaptador del RecyclerView con la lista filtrada
                adapter.updateData(comprablesNovedades);
                // Cierra el submenú de accesorios si está abierto
                if (accesoriosSubMenuVisible) {
                    accesoriosSubMenuVisible = false;
                    navigationView.getMenu().removeGroup(R.id.accesorios);
                }
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void verificarPermisosCamara() {
        int estado = ContextCompat.checkSelfPermission(Compra.this, android.Manifest.permission.CAMERA);
        if (estado == PackageManager.PERMISSION_GRANTED) {
            iniciarCapturaImagen();
        } else {
            ActivityCompat.requestPermissions(Compra.this, new String[]{Manifest.permission.CAMERA}, PermisoCamara);
        }
    }
    private void iniciarCapturaImagen() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, codigofoto);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == codigofoto && resultCode == RESULT_OK && data != null) {
            // Obtener la imagen capturada del intent
            Bitmap fotoCapturada = (Bitmap) data.getExtras().get("data");

            // Generar un nombre de archivo único para la imagen
            String fileName = "imagen_" + System.currentTimeMillis() + ".jpg";

            // Guardar la imagen capturada en la carpeta de la galería
            File imageFile = new File(getExternalFilesDir(null), "GaleriaApp/" + fileName);
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                // Guardar la imagen como un archivo JPG
                fotoCapturada.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            } catch (IOException e) {
                e.printStackTrace();
                showCustomToast("Error al guardar la imagen");
            }

            // Actualizar la actividad de la galería con la nueva imagen
            updateGalleryActivity(imageFile);
        }
    }

    private void updateGalleryActivity(File newImageFile) {
        // Iniciar `GalleryActivity` con un Intent
        Intent intent = new Intent(this, GalleryActivity.class);
        // Puedes pasar información adicional si es necesario
        startActivity(intent);
    }

    private ArrayList<Comprable> filterComprablesByCategory(String category) {
        ArrayList<Comprable> filteredComprables = new ArrayList<>();
        for (Comprable comprable : comprables) {
            if (comprable.getPic().equals(category)) {
                filteredComprables.add(comprable);
            }
        }
        return filteredComprables;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Elimina la inflación del menú aquí
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Elimina el manejo del evento de clic del menú aquí
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PermisoCamara:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCustomToast("No existe el producto");
                    iniciarCapturaImagen();
                } else {
                    showCustomToast("Permiso denegado para la cámara");
                }
        }
    }
    @Override
    public void onBackPressed() {
        // Finaliza la actividad actual y cierra la aplicación
        super.onBackPressed();
        finishAffinity();
    }
    private void showCustomToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.toast_text));

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
    private void hideNavigationButtons() {
        bottomAppBar.setVisibility(View.GONE);
        inicio.setVisibility(View.GONE);
        dieta.setVisibility(View.GONE);
        rutina.setVisibility(View.GONE);
        ajustes.setVisibility(View.GONE);
        compras.setVisibility(View.GONE);
    }

    private void showNavigationButtons() {
        bottomAppBar.setVisibility(View.VISIBLE);
        inicio.setVisibility(View.VISIBLE);
        dieta.setVisibility(View.VISIBLE);
        rutina.setVisibility(View.VISIBLE);
        ajustes.setVisibility(View.VISIBLE);
        compras.setVisibility(View.VISIBLE);
    }
}
