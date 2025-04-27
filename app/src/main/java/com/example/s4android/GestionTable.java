package com.example.s4android;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;




public class GestionTable extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable updateRunnable;
    private final int UPDATE_INTERVAL = 5000; // 10 secondes


    private Button[] buttons;  // Tableau de boutons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_table);

        Button btnAccueil = findViewById(R.id.button2);
        buttons = new Button[5]; // 5 tables par secteur

        int secteur = getIntent().getIntExtra("SECTEUR", -1);

        // Initialisation des boutons et des TextViews pour afficher les numéros des tables
        if (secteur == 1) {
            buttons[0] = findViewById(R.id.button3);  // Table 1
            buttons[1] = findViewById(R.id.button5);  // Table 2
            buttons[2] = findViewById(R.id.button7);  // Table 3
            buttons[3] = findViewById(R.id.button8);  // Table 4
            buttons[4] = findViewById(R.id.button10); // Table 5

            // Mettre à jour les TextViews pour afficher les numéros de tables
            ((TextView) findViewById(R.id.button3)).setText("Table 1");
            ((TextView) findViewById(R.id.button5)).setText("Table 2");
            ((TextView) findViewById(R.id.button7)).setText("Table 3");
            ((TextView) findViewById(R.id.button8)).setText("Table 4");
            ((TextView) findViewById(R.id.button10)).setText("Table 5");
        } else if (secteur == 2) {
            buttons[0] = findViewById(R.id.button3);  // Table 6
            buttons[1] = findViewById(R.id.button5);  // Table 7
            buttons[2] = findViewById(R.id.button7);  // Table 8
            buttons[3] = findViewById(R.id.button8);  // Table 9
            buttons[4] = findViewById(R.id.button10); // Table 10

            // Mettre à jour les TextViews pour afficher les numéros de tables
            ((TextView) findViewById(R.id.button3)).setText("Table 6");
            ((TextView) findViewById(R.id.button5)).setText("Table 7");
            ((TextView) findViewById(R.id.button7)).setText("Table 8");
            ((TextView) findViewById(R.id.button8)).setText("Table 9");
            ((TextView) findViewById(R.id.button10)).setText("Table 10");
        } else if (secteur == 3) {
            buttons[0] = findViewById(R.id.button3);  // Table 11
            buttons[1] = findViewById(R.id.button5);  // Table 12
            buttons[2] = findViewById(R.id.button7);  // Table 13
            buttons[3] = findViewById(R.id.button8);  // Table 14
            buttons[4] = findViewById(R.id.button10); // Table 15

            // Mettre à jour les TextViews pour afficher les numéros de tables
            ((TextView) findViewById(R.id.button3)).setText("Table 11");
            ((TextView) findViewById(R.id.button5)).setText("Table 12");
            ((TextView) findViewById(R.id.button7)).setText("Table 13");
            ((TextView) findViewById(R.id.button8)).setText("Table 14");
            ((TextView) findViewById(R.id.button10)).setText("Table 15");
        } else if (secteur == 4) {
            buttons[0] = findViewById(R.id.button3);  // Table 16
            buttons[1] = findViewById(R.id.button5);  // Table 17
            buttons[2] = findViewById(R.id.button7);  // Table 18
            buttons[3] = findViewById(R.id.button8);  // Table 19
            buttons[4] = findViewById(R.id.button10); // Table 20

            // Mettre à jour les TextViews pour afficher les numéros de tables
            ((TextView) findViewById(R.id.button3)).setText("Table 16");
            ((TextView) findViewById(R.id.button5)).setText("Table 17");
            ((TextView) findViewById(R.id.button7)).setText("Table 18");
            ((TextView) findViewById(R.id.button8)).setText("Table 19");
            ((TextView) findViewById(R.id.button10)).setText("Table 20");
        }

//        // Mise en place des listeners pour les boutons
//        for (int i = 0; i < buttons.length; i++) {
//            final int tableId = (secteur - 1) * 5 + i + 1;
//            if (buttons[i] != null) {
//                buttons[i].setOnClickListener(v -> ouvrirCommande(tableId));
//            }
//        }

        // Mise à jour des boutons avec les numéros des tables
        for (int i = 0; i < buttons.length; i++) {
            final int tableId = (secteur - 1) * 5 + i + 1;
            if (buttons[i] != null) {
                buttons[i].setText("Table " + tableId);  // Ajoute le numéro de la table sur le bouton
                buttons[i].setOnClickListener(v -> ouvrirCommande(tableId));  // Ajoute l'action pour ouvrir la commande
            }
        }


//        // Gérer la connexion à la base de données
//        Database db = new Database();
//        Connection conn = db.connectDB();
//
//        if (conn != null) {
//            Log.d("GestionTable", "Connexion réussie à la base de données.");
//            List<Statut_table> statuts = db.getStatuts(conn, secteur);
//            Log.d("GestionTable", "Statuts récupérés: " + statuts.size());
//
//            for (Statut_table s : statuts) {
//                Log.d("GestionTable", "Table ID: " + s.getId() + ", Statut: " + s.getNom());
//
//                // Transformation du statut numérique en chaîne de caractères
//                String statut = "";
//                switch (s.getNom()) {
//                    case "1":  // Si le statut est 'libre' (par exemple)
//                        statut = "libre";
//                        break;
//                    case "2":  // Si le statut est 'occupée'
//                        statut = "occupée";
//                        break;
//                    case "3":  // Si le statut est 'réservée'
//                        statut = "réservée";
//                        break;
//                    case "4":  // Si le statut est 'a nettoyer'
//                        statut = "a nettoyer";
//                        break;
//                    default:
//                        statut = "inconnu";  // Par sécurité, pour gérer un statut inconnu
//                        break;
//                }
//
//                // Mise à jour des boutons selon le statut
//                if (s.getId() >= (secteur - 1) * 5 + 1 && s.getId() <= secteur * 5) {
//                    setButtonColor(buttons[s.getId() - (secteur - 1) * 5 - 1], statut);  // -1 pour l'indexation dans le tableau
//                }
//            }
//        }

        // Listener pour le bouton retour
        btnAccueil.setOnClickListener(v -> finish());
        startUpdating();

    }

    private void setButtonColor(Button btn, String statut) {
        switch (statut) {
            case "libre":
                btn.setBackgroundColor(getResources().getColor(R.color.grey)); // Gris pour libre
                break;
            case "occupée":
                btn.setBackgroundColor(getResources().getColor(R.color.red)); // Rouge pour occupée
                break;
            case "réservée":
                btn.setBackgroundColor(getResources().getColor(R.color.green)); // Vert pour réservée
                break;
            case "a nettoyer":
                btn.setBackgroundColor(getResources().getColor(R.color.orange)); // Orange pour à nettoyer
                break;
            default:
                btn.setBackgroundColor(getResources().getColor(R.color.blue)); // Bleu si statut inconnu
                break;
        }
    }


    private void updateTables() {
        Database db = new Database();
        Connection conn = db.connectDB();

        if (conn != null) {
            int secteur = getIntent().getIntExtra("SECTEUR", -1);
            List<Statut_table> statuts = db.getStatuts(conn, secteur);

            for (Statut_table s : statuts) {
                String statut = "";
                switch (s.getNom()) {
                    case "1":
                        statut = "libre";
                        break;
                    case "2":
                        statut = "occupée";
                        break;
                    case "3":
                        statut = "réservée";
                        break;
                    case "4":
                        statut = "a nettoyer";
                        break;
                    default:
                        statut = "inconnu";
                        break;
                }

                if (s.getId() >= (secteur - 1) * 5 + 1 && s.getId() <= secteur * 5) {
                    setButtonColor(buttons[s.getId() - (secteur - 1) * 5 - 1], statut);
                }
            }
        }
    }


    private void startUpdating() {
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateTables(); // ta fonction qui recharge les statuts et couleurs
                handler.postDelayed(this, UPDATE_INTERVAL); // relance toutes les 10 sec
            }
        };
        handler.post(updateRunnable);
    }


    private void ouvrirCommande(int idTable) {
        // Récupère la connexion à la base de données
        Database db = new Database();
        Connection conn = db.connectDB();

        if (conn != null) {
            List<Statut_table> statuts = db.getStatuts(conn, getIntent().getIntExtra("SECTEUR", -1));

            // Vérifie le statut de la table
            for (Statut_table s : statuts) {
                if (s.getId() == idTable) {
                    String statut = s.getNom();

                    // Affiche le statut dans les logs pour déboguer
                    Log.d("GestionTable", "Statut de la table " + idTable + ": " + statut);

                    if ("1".equals(statut) || "2".equals(statut)) {
                        Intent intent = new Intent(this, Commande.class);
                        intent.putExtra("ID_TABLE", idTable);
                        startActivity(intent);
                    } else if ("3".equals(statut)) {
                        afficherModaleReservation(idTable);
                    } else if("4".equals(statut)){
                        afficherModaleNettoyage(idTable);
                    } else{
                        Toast.makeText(this, "Cette table n'est pas disponible pour une commande.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    }

    private void afficherModaleNettoyage(int idTable) {
        View dialogView = getLayoutInflater().inflate(R.layout.modale_a_nettoyer, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        Button btnYes = dialogView.findViewById(R.id.buttonYes);
        Button btnNo = dialogView.findViewById(R.id.buttonNo);

        btnYes.setOnClickListener(v -> {
            Database db = new Database();
            Connection conn = db.connectDB();

            if (conn != null) {
                new Thread(() -> {
                    try {
                        // 2. Vérifier les autres réservations futures
                        List<Reservation> autresReservations = db.getAllReservationsForTable(conn, idTable);
                        String now = db.getCurrentDateTime();

                        boolean hasFutureReservation = false;
                        for (Reservation r : autresReservations) {
                            String fullDateTime = r.getDate() + " " + r.getHoraire();
                            if (fullDateTime.compareTo(now) > 0) {
                                hasFutureReservation = true;
                                break;
                            }
                        }

                        // 3. Mise à jour du statut
                        int newStatus = hasFutureReservation ? 3 : 1; // 3 = réservée, 1 = libre
                        final String message = hasFutureReservation ?
                                "La table est de nouveau marquée comme réservée." :
                                "Table remise en état libre.";

                        Statement stmt = conn.createStatement();
                        String update = "UPDATE TABLES SET ID_STATUT_TABLE = " + newStatus + " WHERE ID_TABLES = " + idTable;
                        stmt.executeUpdate(update);

                        runOnUiThread(() -> {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            recreate(); // recharge l’activité
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show());
                    }
                }).start();
            }
        });

        btnNo.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }






    private void afficherModaleReservation(int idTable) {
        View dialogView = getLayoutInflater().inflate(R.layout.modale_reservation, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        Button btnNom = dialogView.findViewById(R.id.button2);
        Button btnTel = dialogView.findViewById(R.id.button7);
        Button btnDate = dialogView.findViewById(R.id.button8);
        Button btnHoraire = dialogView.findViewById(R.id.button9);
        Button btnNbPersonnes = dialogView.findViewById(R.id.button10);
        Button btnCommande = dialogView.findViewById(R.id.button);
        LinearLayout autresReservationsLayout = dialogView.findViewById(R.id.layoutReservationsContainer);

        Database db = new Database();
        Connection conn = db.connectDB();

        if (conn != null) {
            Reservation reservationPrincipale = db.getReservationForTable(conn, idTable);

            if (reservationPrincipale != null) {
                btnNom.setText(reservationPrincipale.getNom());
                btnTel.setText(reservationPrincipale.getTelephone());
                btnDate.setText(reservationPrincipale.getDate());
                btnHoraire.setText(reservationPrincipale.getHoraire());
                btnNbPersonnes.setText(String.valueOf(reservationPrincipale.getNbPersonnes()));

                List<Reservation> autres = db.getAllReservationsForTable(conn, idTable);
                List<Reservation> reservationsAffichees = new ArrayList<>();

                for (Reservation r : autres) {
                    if (!r.getHoraire().equals(reservationPrincipale.getHoraire()) &&
                            r.getDate().equals(reservationPrincipale.getDate())) {
                        reservationsAffichees.add(r);
                    }
                }

                afficherAutresReservations(reservationsAffichees, reservationPrincipale, btnNom, btnTel, btnDate, btnHoraire, btnNbPersonnes, autresReservationsLayout);
            }
        }

        btnCommande.setOnClickListener(v -> {
            Intent intent = new Intent(this, Commande.class);
            intent.putExtra("ID_TABLE", idTable);
            startActivity(intent);
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(
                (int) (getResources().getDisplayMetrics().widthPixels * 0.95),
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    private void afficherAutresReservations(List<Reservation> reservationsAffichees,
                                            Reservation current,
                                            Button btnNom, Button btnTel, Button btnDate,
                                            Button btnHoraire, Button btnNbPersonnes,
                                            LinearLayout container) {

        container.removeAllViews();

        for (int i = 0; i < reservationsAffichees.size(); i++) {
            Reservation r = reservationsAffichees.get(i);
            Button btn = new Button(this);
            btn.setText("Nom :" + r.getNom() +
                    "\nHoraire :" + r.getHoraire());

            btn.setOnClickListener(v -> {
                // Swap
                reservationsAffichees.remove(r);
                reservationsAffichees.add(current);

                // Remplir les infos avec celle cliquée
                btnNom.setText(r.getNom());
                btnTel.setText(r.getTelephone());
                btnDate.setText(r.getDate());
                btnHoraire.setText(r.getHoraire());
                btnNbPersonnes.setText(String.valueOf(r.getNbPersonnes()));

                // Relancer l'affichage des autres
                afficherAutresReservations(reservationsAffichees, r, btnNom, btnTel, btnDate, btnHoraire, btnNbPersonnes, container);
            });

            container.addView(btn);
        }
    }


}





















