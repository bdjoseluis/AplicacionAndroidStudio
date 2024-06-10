package com.example.tfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.tfg.domain.Usuario;
import com.example.tfg.fragments.AjustesFragment;

import java.util.ArrayList;
import java.util.List;

public class Ajustes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorajustes,
                new AjustesFragment()).commit();


    }


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.volverajustes:
                finish();break;
        }
    }
}