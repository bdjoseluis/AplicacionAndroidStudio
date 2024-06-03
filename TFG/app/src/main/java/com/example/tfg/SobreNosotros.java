package com.example.tfg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class SobreNosotros extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nosotros);

        Button contactButton = findViewById(R.id.contact_button);
        ImageButton linkedinButton = findViewById(R.id.linkedin_button);

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open Gmail for sending an email
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "comando1.yt@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto desde la app");
                startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
            }
        });

        linkedinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open LinkedIn
                Intent linkedinIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com"));
                startActivity(linkedinIntent);
            }
        });

    }
}
