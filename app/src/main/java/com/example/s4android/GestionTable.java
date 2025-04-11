package com.example.s4android;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;


public class GestionTable extends AppCompatActivity {

    private int secteur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_table); // ton fichier FXML renommé en XML

        secteur = getIntent().getIntExtra("SECTEUR", 1);

        Button btnTable1 = findViewById(R.id.button3);
        Button btnTable2 = findViewById(R.id.button5);
        Button btnTable3 = findViewById(R.id.button7);
        Button btnTable4 = findViewById(R.id.button8);
        Button btnTable5 = findViewById(R.id.button10);
        Button button2 = findViewById(R.id.button2);

        // Exemple : pour Secteur 1 on affiche tables 1 à 5
        int[][] tablesParSecteur = {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20}
        };

        int[] tables = tablesParSecteur[secteur - 1];

        btnTable1.setText("Table " + tables[0]);
        btnTable2.setText("Table " + tables[1]);
        btnTable3.setText("Table " + tables[2]);
        btnTable4.setText("Table " + tables[3]);
        btnTable5.setText("Table " + tables[4]);

        // Ajoute des listeners si tu veux gérer les clics
        btnTable1.setOnClickListener(v -> afficherToast(tables[0]));
        btnTable2.setOnClickListener(v -> afficherToast(tables[1]));
        btnTable3.setOnClickListener(v -> afficherToast(tables[2]));
        btnTable4.setOnClickListener(v -> afficherToast(tables[3]));
        btnTable5.setOnClickListener(v -> afficherToast(tables[4]));
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(GestionTable.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void afficherToast(int table) {
        Intent intent = new Intent(this, Commande.class);
        intent.putExtra("SECTEUR", secteur);
        intent.putExtra("TABLE", table);
        startActivity(intent);
    }


}


