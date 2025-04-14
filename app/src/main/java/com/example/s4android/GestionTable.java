package com.example.s4android;



import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.util.List;
import android.util.Log;

public class GestionTable extends AppCompatActivity {

    private Button[] buttons;  // Tableau de boutons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_table);

        Button btnAccueil = findViewById(R.id.button2);
        buttons = new Button[20]; // Assumons que tu aies 5 tables

        // Initialisation des boutons dans le tableau
        buttons[0] = findViewById(R.id.button3);  // Table 1  - SECTEUR 1
        buttons[1] = findViewById(R.id.button5);  // Table 2  - SECTEUR 1
        buttons[2] = findViewById(R.id.button7);  // Table 3  - SECTEUR 1
        buttons[3] = findViewById(R.id.button8);  // Table 4  - SECTEUR 1
        buttons[4] = findViewById(R.id.button10); // Table 5  - SECTEUR 1

        buttons[5] = findViewById(R.id.button3);  // Table 6  - SECTEUR 2
        buttons[6] = findViewById(R.id.button5);  // Table 7  - SECTEUR 2
        buttons[7] = findViewById(R.id.button7);  // Table 8  - SECTEUR 2
        buttons[8] = findViewById(R.id.button8);  // Table 9  - SECTEUR 2
        buttons[9] = findViewById(R.id.button10);  // Table 10 - SECTEUR 2

        buttons[10] = findViewById(R.id.button3);  // Table 11 - SECTEUR 3
        buttons[11] = findViewById(R.id.button5);  // Table 12 - SECTEUR 3
        buttons[12] = findViewById(R.id.button7);  // Table 13 - SECTEUR 3
        buttons[13] = findViewById(R.id.button8);  // Table 14 - SECTEUR 3
        buttons[14] = findViewById(R.id.button10);  // Table 15 - SECTEUR 3

        buttons[15] = findViewById(R.id.button3);  // Table 16 - SECTEUR 4
        buttons[16] = findViewById(R.id.button5);  // Table 17 - SECTEUR 4
        buttons[17] = findViewById(R.id.button7);  // Table 18 - SECTEUR 4
        buttons[18] = findViewById(R.id.button8);  // Table 19 - SECTEUR 4
        buttons[19] = findViewById(R.id.button10);  // Table 20 - SECTEUR 4

        int secteur = getIntent().getIntExtra("SECTEUR", -1);

        Database db = new Database();
        Connection conn = db.connectDB();

        if (conn != null) {
            Log.d("GestionTable", "Connexion réussie à la base de données.");
            List<Statut_table> statuts = db.getStatuts(conn, secteur);
            Log.d("GestionTable", "Statuts récupérés: " + statuts.size());

            for (Statut_table s : statuts) {
                Log.d("GestionTable", "Table ID: " + s.getId() + ", Statut: " + s.getNom());

                // Transformation du statut numérique en chaîne de caractères
                String statut = "";
                switch (s.getNom()) {
                    case "1":  // Si le statut est 'libre' (par exemple)
                        statut = "libre";
                        break;
                    case "2":  // Si le statut est 'occupée'
                        statut = "occupée";
                        break;
                    case "3":  // Si le statut est 'réservée'
                        statut = "réservée";
                        break;
                    case "4":  // Si le statut est 'indisponible' ou autre
                        statut = "a nettoyer";
                        break;
                    default:
                        statut = "inconnu";  // Par sécurité, pour gérer un statut inconnu
                        break;
                }

                // Mise à jour des boutons selon le statut
                // Utilisation de l'indexation dans le tableau pour éviter un switch
                if (s.getId() >= 1 && s.getId() <= 20) {
                    setButtonColor(buttons[s.getId() - 1], statut);  // -1 car les indices de tableau commencent à 0
                }
            }
        }

        btnAccueil.setOnClickListener(v -> finish());
    }

    private void setButtonColor(Button btn, String statut) {
        switch (statut) {
            case "libre":
                btn.setBackgroundColor(getResources().getColor(R.color.grey)); // Rouge pour occupée
                break;
            case "occupée":
                btn.setBackgroundColor(getResources().getColor(R.color.red)); // Vert pour libre
                break;
            case "réservée":
                btn.setBackgroundColor(getResources().getColor(R.color.green)); // Orange pour réservée
                break;
            case "a nettoyer":
                btn.setBackgroundColor(getResources().getColor(R.color.orange)); // Gris pour indisponible
                break;
            default:
                btn.setBackgroundColor(getResources().getColor(R.color.blue)); // Bleu si statut inconnu
                break;
        }
    }
}





