package com.example.tfg.Adapter;

import android.animation.AnimatorSet;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tfg.R;
import com.example.tfg.domain.DiasSemana;

import java.util.List;

public class MiAdaptadorDiasSemana extends RecyclerView.Adapter<MiAdaptadorDiasSemana.ViewHolder> {
    List<DiasSemana> categoriaDominios;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = -1;
    private int lastSelectedPosition = -1;

    private AnimatorSet lastAnimator;

    public MiAdaptadorDiasSemana(List<DiasSemana> categoriaDominios) {
        this.categoriaDominios = categoriaDominios;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholdercomidas, parent, false);
        return new ViewHolder(inflate);
    }

    public void clear() {
        categoriaDominios.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<DiasSemana> nuevosElementos) {
        categoriaDominios.addAll(nuevosElementos);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryName.setText(categoriaDominios.get(position).getTitulo());
        String picUrl = "icono_l";  // Reemplaza con la lógica correcta si es necesario
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(picUrl, "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId).into(holder.categoryPic);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                int idDia = categoriaDominios.get(position).getId();
                onItemClickListener.onItemClick(idDia);

                // Detener la animación en el elemento anterior si es diferente del actual
                if (selectedPosition != position && lastAnimator != null) {
                    lastAnimator.cancel();
                    lastAnimator = null;
                    notifyItemChanged(lastSelectedPosition);
                }

                // Iniciar la animación en el elemento actual
                selectedPosition = position;
                lastSelectedPosition = position;
                notifyDataSetChanged();
                showCustomToast(holder, categoriaDominios.get(position).getTitulo());
            }
        });

        if (selectedPosition == position) {
            holder.mainLayout.setBackgroundResource(R.drawable.border_color_animation);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.mainLayout.getBackground();
            animationDrawable.start();
        } else {
            holder.mainLayout.setBackgroundResource(getBackgroundForPosition(position));
        }
        holder.categoryPic.setBackgroundResource(getBackgroundForPosition(position));

    }

    private int getBackgroundForPosition(int position) {
        switch (position) {
            case 0:
                return R.drawable.catbackground1;
            case 1:
                return R.drawable.catbackground2;
            case 2:
                return R.drawable.catbackground3;
            case 3:
                return R.drawable.catbackground4;
            case 4:
                return R.drawable.catbackground5;
            case 5:
                return R.drawable.catbackground2;
            case 6:
                return R.drawable.catbackground1;
            default:
                return R.drawable.catbackground5; // Reemplaza con tu fondo por defecto
        }
    }

    @Override
    public int getItemCount() {
        return categoriaDominios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPic;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryname);
            categoryPic = itemView.findViewById(R.id.categorypic);
            mainLayout = itemView.findViewById(R.id.layoutprincipal);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int nombreDia);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    private void showCustomToast(ViewHolder holder, String diaSemana) {
        LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(diaSemana);

        Toast toast = new Toast(holder.itemView.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
