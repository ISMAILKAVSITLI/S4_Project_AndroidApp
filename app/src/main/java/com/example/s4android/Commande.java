package com.example.s4android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Commande extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commande);

        Button button11 = findViewById(R.id.button11);

        // Tu peux récupérer les infos si besoin, par exemple :
        int secteur = getIntent().getIntExtra("SECTEUR", -1);
        int table = getIntent().getIntExtra("TABLE", -1);

        button11.setOnClickListener(v -> {
            Intent intent = new Intent(Commande.this, GestionTable.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
