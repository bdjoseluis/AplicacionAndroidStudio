package com.example.tfg;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tfg.Adapter.ImageAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 1001;
    private static final int REQUEST_PICK_IMAGE = 1002;

    private GridView gridViewImages;
    private ImageAdapter imageAdapter;

    // Directorio de almacenamiento para las fotos seleccionadas
    private File photoDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Inicializa el GridView y el adaptador para mostrar las imágenes
        gridViewImages = findViewById(R.id.gridViewImages);
        imageAdapter = new ImageAdapter(this, new ArrayList<>());
        gridViewImages.setAdapter(imageAdapter);

        // Verifica permisos de almacenamiento
        if (checkStoragePermissions()) {
            setupPhotoDirectory();
        } else {
            requestStoragePermissions();
        }

        // Configura un botón para seleccionar imágenes de la galería
        Button buttonSelectImages = findViewById(R.id.buttonSelectImages);
        buttonSelectImages.setOnClickListener(v -> selectImagesFromGallery());
    }

    // Verifica permisos de almacenamiento
    private boolean checkStoragePermissions() {
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
    }

    // Solicita permisos de almacenamiento
    private void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_PERMISSION_CODE);
    }

    // Configura el directorio para guardar las fotos seleccionadas
    private void setupPhotoDirectory() {
        photoDirectory = new File(getExternalFilesDir(null), "GaleriaApp");
        if (!photoDirectory.exists()) {
            photoDirectory.mkdirs();  // Crea el directorio si no existe
        }
        // Cargar fotos existentes desde el directorio
        loadExistingPhotos();
    }

    // Selecciona imágenes de la galería
    private void selectImagesFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // Permitir múltiples selecciones
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // Maneja el resultado de la selección de imágenes de la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Maneja múltiples selecciones de imágenes
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        saveImageToDirectory(imageUri);
                    }
                } else if (data.getData() != null) {
                    // Maneja una sola selección de imagen
                    Uri imageUri = data.getData();
                    saveImageToDirectory(imageUri);
                }
            }
        }
    }

    // Guarda la imagen seleccionada en el directorio específico
    private void saveImageToDirectory(Uri imageUri) {
        try {
            // Obtener el nombre del archivo de la imagen
            String fileName = new File(imageUri.getPath()).getName();
            File destFile = new File(photoDirectory, fileName);

            // Copiar la imagen al directorio específico
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            OutputStream outputStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Cerrar streams
            inputStream.close();
            outputStream.close();

            // Actualizar el adaptador con la nueva imagen
            imageAdapter.addImage(destFile.getPath());

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la imagen.", Toast.LENGTH_SHORT).show();
        }
    }

    // Carga fotos existentes desde el directorio y las muestra en el GridView
    private void loadExistingPhotos() {
        File[] files = photoDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    imageAdapter.addImage(file.getPath());
                }
            }
        }
    }

    // Maneja la respuesta de permisos de almacenamiento
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CODE) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                setupPhotoDirectory();
            } else {
                Toast.makeText(this, "Permisos de almacenamiento requeridos.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}