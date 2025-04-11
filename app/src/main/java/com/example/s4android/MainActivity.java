package com.example.s4android;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnSecteur1 = findViewById(R.id.btnSecteur1);
        Button btnSecteur2 = findViewById(R.id.btnSecteur2);
        Button btnSecteur3 = findViewById(R.id.btnSecteur3);
        Button btnSecteur4 = findViewById(R.id.btnSecteur4);


        btnSecteur1.setOnClickListener(view -> ouvrirTables(1));
        btnSecteur2.setOnClickListener(view -> ouvrirTables(2));
        btnSecteur3.setOnClickListener(view -> ouvrirTables(3));
        btnSecteur4.setOnClickListener(view -> ouvrirTables(4));
    }


    private void ouvrirTables(int secteur) {
        Intent intent = new Intent(this, GestionTable.class);
        intent.putExtra("SECTEUR", secteur);
        startActivity(intent);
    }
}
