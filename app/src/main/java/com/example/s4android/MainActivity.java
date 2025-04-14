package com.example.s4android;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.util.Log;
import java.sql.Connection;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public Database database;
    int secteur = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int secteur = getIntent().getIntExtra("SECTEUR", -1);

        Button btnSecteur1 = findViewById(R.id.btnSecteur1);
        Button btnSecteur2 = findViewById(R.id.btnSecteur2);
        Button btnSecteur3 = findViewById(R.id.btnSecteur3);
        Button btnSecteur4 = findViewById(R.id.btnSecteur4);


        btnSecteur1.setOnClickListener(view -> ouvrirTables(1));
        btnSecteur2.setOnClickListener(view -> ouvrirTables(2));
        btnSecteur3.setOnClickListener(view -> ouvrirTables(3));
        btnSecteur4.setOnClickListener(view -> ouvrirTables(4));

//        database = new Database();
//        database.connectDB();

        Database db = new Database();
        Connection conn = db.connectDB();

        if (conn != null) {
            List<Statut_table> statuts = db.getStatuts(conn, secteur);
            for (Statut_table s : statuts) {
                Log.d("Statut", s.toString());
            }
        }


    }
    private void ouvrirTables(int secteur) {
        Intent intent = new Intent(this, GestionTable.class);
        intent.putExtra("SECTEUR", secteur);
        startActivity(intent);
    }




}
