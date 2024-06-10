package com.example.tfg.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.tfg.R;
import com.example.tfg.domain.trending;

import java.util.ArrayList;

public class TrendAdaptador extends RecyclerView.Adapter<TrendAdaptador.Viewholder> {

    ArrayList<trending> items;
    Context context;

    public TrendAdaptador(ArrayList<trending> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public TrendAdaptador.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_trend,parent,false);
        context=parent.getContext();
        return new Viewholder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendAdaptador.Viewholder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.subtitle.setText(items.get(position).getSubtitle());

        int drawableResourceId=holder.itemView.getResources().getIdentifier(items.get(position).getPicAdress(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(30,30,0,0))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends  RecyclerView.ViewHolder{
    TextView title, subtitle;
    ImageView pic;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tituloviewtren);
            subtitle=itemView.findViewById(R.id.subtituloviewtren);
            pic=itemView.findViewById(R.id.imagenviewtren);
        }
    }
}
