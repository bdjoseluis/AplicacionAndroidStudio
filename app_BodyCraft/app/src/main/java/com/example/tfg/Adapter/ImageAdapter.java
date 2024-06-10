package com.example.tfg.Adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> imagePaths;

    public ImageAdapter(Context context, List<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return imagePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        // Si no se reutiliza la vista, crea una nueva
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        // Obtiene la ruta de la imagen para el índice de la lista
        String imagePath = imagePaths.get(position);

        // Carga la imagen en el ImageView usando Glide
        Glide.with(context)
                .load(imagePath)
                .into(imageView);

        return imageView;
    }

    // Añade una imagen a la lista de imágenes
    public void addImage(String imagePath) {
        if (!imagePaths.contains(imagePath)) {
            imagePaths.add(imagePath);
            notifyDataSetChanged();  // Actualiza el adaptador para reflejar los cambios
        } else {
            Toast.makeText(context, "Imagen ya añadida.", Toast.LENGTH_SHORT).show();
        }
    }
}