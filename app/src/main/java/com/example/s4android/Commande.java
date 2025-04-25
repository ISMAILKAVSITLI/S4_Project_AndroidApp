package com.example.s4android;


import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Commande extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PlatAdapter platAdapter;
    private List<PlatItem> platItems;
    private List<PlatItem> platsCommandes;
    private TextView textViewResume;
    private Button buttonValiderCommande;
    private boolean isResumeMode = false;
    private int idTable = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commande);

        recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        textViewResume = findViewById(R.id.textViewResume);
        textViewResume.setVisibility(View.GONE);

        buttonValiderCommande = findViewById(R.id.button_valider_commande);
        buttonValiderCommande.setVisibility(View.GONE);

        platItems = new ArrayList<>();
        platsCommandes = new ArrayList<>();

        idTable = getIntent().getIntExtra("ID_TABLE", -1);

        setupAdapter(false);

        findViewById(R.id.button13).setOnClickListener(v -> chargerMenu("Entrée"));
        findViewById(R.id.button14).setOnClickListener(v -> chargerMenu("Plat"));
        findViewById(R.id.button15).setOnClickListener(v -> chargerMenu("Dessert"));
        findViewById(R.id.button16).setOnClickListener(v -> chargerMenu("Boisson"));
        findViewById(R.id.button12).setOnClickListener(v -> afficherResume());


        Button boutonEntree = findViewById(R.id.button13);
        Button boutonPlat = findViewById(R.id.button14);
        Button boutonDessert = findViewById(R.id.button15);
        Button boutonBoisson = findViewById(R.id.button16);



        // Cache le bouton pour les autres catégories
        View.OnClickListener hideButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonValiderCommande.setVisibility(View.GONE);
                textViewResume.setVisibility(View.GONE);
            }
        };

        boutonEntree.setOnClickListener(v -> {
            hideButtonListener.onClick(v);
            chargerMenu("Entrée");
        });
        boutonPlat.setOnClickListener(v -> {
            hideButtonListener.onClick(v);
            chargerMenu("Plat");
        });
        boutonDessert.setOnClickListener(v -> {
            hideButtonListener.onClick(v);
            chargerMenu("Dessert");
        });
        boutonBoisson.setOnClickListener(v -> {
            hideButtonListener.onClick(v);
            chargerMenu("Boisson");
        });

        buttonValiderCommande.setOnClickListener(v -> {
            if (platsCommandes.isEmpty()) {
                Toast.makeText(this, "Aucun plat à envoyer", Toast.LENGTH_SHORT).show();
                return;
            }

            Database db = new Database();
            Connection conn = db.connectDB();

            if (conn != null) {
                new Thread(() -> {
                    try {
                        Statement stmt = conn.createStatement();
                        int idCommande = -1;

                        String requete_table = "SELECT ST.STATUT_TABLE FROM TABLES T " +
                                "JOIN STATUT_TABLE ST ON T.ID_STATUT_TABLE = ST.ID_STATUT_TABLE " +
                                "WHERE T.ID_TABLES = " + idTable;
                        ResultSet rsEtat = stmt.executeQuery(requete_table);

                        boolean tableOccupee = false;
                        if (rsEtat.next()) {
                            String statut = rsEtat.getString("STATUT_TABLE");
                            tableOccupee = statut != null && statut.equalsIgnoreCase("occupée");
                        }

                        if (tableOccupee) {
                            ResultSet rsCommande = stmt.executeQuery(
                                    "SELECT ID_COMMANDE FROM COMMANDE WHERE ID_TABLES = " + idTable +
                                            " AND COMMANDE_PAYEE = FALSE ORDER BY DATE_COMMANDE DESC LIMIT 1");
                            if (rsCommande.next()) {
                                idCommande = rsCommande.getInt("ID_COMMANDE");
                            } else {
                                throw new Exception("Aucune commande non payée trouvée.");
                            }
                        } else {
                            stmt.executeUpdate(
                                    "INSERT INTO COMMANDE (DATE_COMMANDE, COMMANDE_PAYEE, ID_TABLES) " +
                                            "VALUES (NOW(), FALSE, " + idTable + ")", Statement.RETURN_GENERATED_KEYS);
                            ResultSet rsCmd = stmt.getGeneratedKeys();
                            if (rsCmd.next()) {
                                idCommande = rsCmd.getInt(1);
                            } else {
                                throw new Exception("Erreur création commande.");
                            }
                            stmt.executeUpdate("UPDATE TABLES SET ID_STATUT_TABLE = " +
                                    "(SELECT ID_STATUT_TABLE FROM STATUT_TABLE WHERE STATUT_TABLE = 'occupée') " +
                                    "WHERE ID_TABLES = " + idTable);
                        }

                        stmt.executeUpdate("INSERT INTO TICKET (TICKET_EN_COURS, ID_COMMANDE) VALUES (TRUE, " + idCommande + ")", Statement.RETURN_GENERATED_KEYS);
                        ResultSet rsTicket = stmt.getGeneratedKeys();
                        int idTicket = rsTicket.next() ? rsTicket.getInt(1) : -1;

                        for (PlatItem item : platsCommandes) {
                            if (item.getCompteur() > 0) {
                                String commentaire = item.getCommentaire().replace("'", "''");
                                int idSauce = 1;
                                int idCuisson = 1;

                                ResultSet rsPlat = stmt.executeQuery("SELECT ID_PLAT FROM PLAT WHERE NOM_PLAT = '" + item.getNom().replace("'", "''") + "'");
                                int idPlat = rsPlat.next() ? rsPlat.getInt("ID_PLAT") : -1;

                                for (int i = 0; i < item.getCompteur(); i++) {
                                    stmt.executeUpdate("INSERT INTO ITEM (COMMENTAIRE, ID_SAUCE, ID_CUISSON_VIANDE, ID_PLAT, ID_TICKET) " +
                                            "VALUES ('" + commentaire + "', " + idSauce + ", " + idCuisson + ", " + idPlat + ", " + idTicket + ")");
                                }
                            }
                        }

                        runOnUiThread(() -> {
                            Toast.makeText(this, "Commande enregistrée !", Toast.LENGTH_LONG).show();
                            platsCommandes.clear();
                            afficherResume();
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }
                }).start();
            } else {
                Toast.makeText(this, "Connexion impossible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter(boolean isResume) {
        this.isResumeMode = isResume;

        platAdapter = new PlatAdapter(platItems, new PlatAdapter.OnPlatClickListener() {
//            @Override
//            public void onPlatClick(PlatItem clickedItem) {
//                clickedItem.incrementer();
//                if (!platsCommandes.contains(clickedItem)) {
//                    platsCommandes.add(clickedItem);
//                }
//
//                if (clickedItem.getContientSauce()) {
//                    showModaleSauce(clickedItem, () -> {
//                        if (clickedItem.getContientViande()) {
//                            showModaleViande(clickedItem);
//                        }
//                    });
//                } else if (clickedItem.getContientViande()) {
//                    showModaleViande(clickedItem);
//                }
//
//                if (isResumeMode) {
//                    afficherResume();
//                } else {
//                    platAdapter.notifyDataSetChanged();
//                }
//
//                Toast.makeText(Commande.this, clickedItem.getNom() + " ajouté", Toast.LENGTH_SHORT).show();
//            }

            @Override
            public void onPlatClick(PlatItem clickedItem) {
                clickedItem.incrementer();

                // Créer une copie indépendante du plat sélectionné
                PlatItem copyItem = new PlatItem(clickedItem.getNom(), clickedItem.getPrix());
                copyItem.setContientViande(clickedItem.getContientViande());
                copyItem.setContientSauce(clickedItem.getContientSauce());
                copyItem.setCompteur(1); // chaque ajout = 1 exemplaire
                copyItem.setCommentaire(""); // on laisse vide ou on ajoute plus tard

                platsCommandes.add(copyItem);

                if (clickedItem.getContientSauce()) {
                    showModaleSauce(copyItem, () -> {
                        if (copyItem.getContientViande()) {
                            showModaleViande(copyItem);
                        }
                    });
                } else if (copyItem.getContientViande()) {
                    showModaleViande(copyItem);
                }

                if (isResumeMode) {
                    afficherResume();
                } else {
                    platAdapter.notifyDataSetChanged();
                }

                Toast.makeText(Commande.this, clickedItem.getNom() + " ajouté", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onPlatSupprime(PlatItem clickedItem) {
                clickedItem.decrementer();
                if (clickedItem.getCompteur() <= 0) {
                    platsCommandes.remove(clickedItem);
                }

                if (isResumeMode) {
                    afficherResume();
                } else {
                    platAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCommentaireClick(PlatItem item) {
                if (item.getContientSauce()) {
                    showModaleSauce(item, () -> {
                        if (item.getContientViande()) {
                            showModaleViande(item);
                        }
                    });
                } else if (item.getContientViande()) {
                    showModaleViande(item);
                } else {
                    View dialogView = getLayoutInflater().inflate(R.layout.modale_commentaire, null);
                    EditText editCommentaire = dialogView.findViewById(R.id.edit_commentaire);
                    editCommentaire.setText(item.getCommentaire());

                    AlertDialog dialog = new AlertDialog.Builder(Commande.this)
                            .setView(dialogView)
                            .create();

                    Button btnOk = dialogView.findViewById(R.id.btn_ok);
                    btnOk.setOnClickListener(v -> {
                        String commentaire = editCommentaire.getText().toString().trim();
                        item.setCommentaire(commentaire);
                        Toast.makeText(Commande.this, "Commentaire enregistré", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });

                    dialog.show();
                }
            }
        }, isResume);

        recyclerView.setAdapter(platAdapter);
    }

    private void chargerMenu(String category) {
        textViewResume.setVisibility(View.GONE);
        Database db = new Database();
        Connection conn = db.connectDB();

        if (conn != null) {
            try {
                String query = "SELECT PLAT.ID_PLAT, PLAT.NOM_PLAT, PLAT.PRIX, PLAT.SUR_LA_CARTE, TYPE_PLATS.TYPE_PLAT, " +
                        "PLAT.CONTIENT_CUISSON_VIANDE, PLAT.CONTIENT_SAUCE " +
                        "FROM PLAT " +
                        "JOIN TYPE_PLATS ON PLAT.ID_TYPE_PLATS = TYPE_PLATS.ID_TYPE_PLATS " +
                        "WHERE TYPE_PLATS.TYPE_PLAT = '" + category + "' AND PLAT.SUR_LA_CARTE = TRUE";

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                platItems.clear();
                while (rs.next()) {
                    String nom = rs.getString("NOM_PLAT");
                    double prix = rs.getDouble("PRIX");
                    boolean contientViande = rs.getBoolean("CONTIENT_CUISSON_VIANDE");
                    boolean contientSauce = rs.getBoolean("CONTIENT_SAUCE");

                    PlatItem existingItem = null;
                    for (PlatItem cmdItem : platsCommandes) {
                        if (cmdItem.getNom().equals(nom)) {
                            existingItem = cmdItem;
                            break;
                        }
                    }

                    if (existingItem != null) {
                        platItems.add(existingItem);
                    } else {
                        PlatItem platItem = new PlatItem(nom, prix);
                        platItem.setContientViande(contientViande);
                        platItem.setContientSauce(contientSauce);
                        platItems.add(platItem);
                    }
                }

                runOnUiThread(() -> setupAdapter(false));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Erreur récupération plats", Toast.LENGTH_SHORT).show());
            }
        } else {
            Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
        }
    }

//    private void afficherResume() {
//        textViewResume.setVisibility(View.VISIBLE);
//        platItems.clear();
//
//        for (PlatItem item : platsCommandes) {
//            if (item.getCompteur() > 0) {
//                platItems.add(item);
//            }
//        }
//
//        buttonValiderCommande.setVisibility(platItems.size() > 0 ? View.VISIBLE : View.GONE);
//        setupAdapter(true);
//    }

//    private void afficherResume() {
//        textViewResume.setVisibility(View.VISIBLE);
//        platItems.clear();
//
//        for (PlatItem item : platsCommandes) {
//            int count = item.getCompteur();
//            for (int i = 0; i < count; i++) {
//                PlatItem copy = new PlatItem(
//                        item.getNom(),
//                        item.getPrix()
//                );
//                copy.setCommentaire(item.getCommentaire());
//                platItems.add(copy);
//            }
//        }
//
//        buttonValiderCommande.setVisibility(platItems.size() > 0 ? View.VISIBLE : View.GONE);
//        setupAdapter(true);
//    }

    private void afficherResume() {
        textViewResume.setVisibility(View.VISIBLE);
        platItems.clear();
        platItems.addAll(platsCommandes); // on montre directement les commandes
        buttonValiderCommande.setVisibility(platItems.size() > 0 ? View.VISIBLE : View.GONE);
        setupAdapter(true);
    }


    private void showModaleSauce(PlatItem item, Runnable onFinish) {
        View dialogView = getLayoutInflater().inflate(R.layout.modale_sauce_commentaire, null);
        AlertDialog dialog = new AlertDialog.Builder(Commande.this).setView(dialogView).create();

        Button[] buttons = {
                dialogView.findViewById(R.id.button),
                dialogView.findViewById(R.id.button2),
                dialogView.findViewById(R.id.button3),
                dialogView.findViewById(R.id.button4),
                dialogView.findViewById(R.id.button5),
        };

        List<String> sauces = getSaucesFromDB();
        for (int i = 0; i < buttons.length; i++) {
            if (i < sauces.size()) {
                final String sauce = sauces.get(i);
                buttons[i].setText(sauce);
                buttons[i].setVisibility(View.VISIBLE);
                buttons[i].setOnClickListener(v -> {
                    item.setCommentaire("Sauce: " + sauce);
                    Toast.makeText(Commande.this, "Sauce sélectionnée : " + sauce, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    if (onFinish != null) onFinish.run();
                });
            } else {
                buttons[i].setVisibility(View.GONE);
            }
        }

        dialog.show();
    }

    private void showModaleViande(PlatItem item) {
        View dialogView = getLayoutInflater().inflate(R.layout.modale_viande_commentaire, null);
        AlertDialog dialog = new AlertDialog.Builder(Commande.this).setView(dialogView).create();

        Button[] buttons = {
                dialogView.findViewById(R.id.button),
                dialogView.findViewById(R.id.button2),
                dialogView.findViewById(R.id.button3),
                dialogView.findViewById(R.id.button4),
                dialogView.findViewById(R.id.button5),
        };

        List<String> cuissons = getCuissonsFromDB();
        for (int i = 0; i < buttons.length; i++) {
            if (i < cuissons.size()) {
                final String cuisson = cuissons.get(i);
                buttons[i].setText(cuisson);
                buttons[i].setVisibility(View.VISIBLE);
                buttons[i].setOnClickListener(v -> {
                    String current = item.getCommentaire();
                    item.setCommentaire(current + (current.isEmpty() ? "" : " | ") + "Cuisson: " + cuisson);
                    Toast.makeText(Commande.this, "Cuisson sélectionnée : " + cuisson, Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            } else {
                buttons[i].setVisibility(View.GONE);
            }
        }

        dialog.show();
    }

    private List<String> getSaucesFromDB() {
        List<String> sauces = new ArrayList<>();
        Database db = new Database();
        Connection conn = db.connectDB();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT NOM FROM SAUCE");
                while (rs.next()) {
                    sauces.add(rs.getString("NOM"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sauces;
    }

    private List<String> getCuissonsFromDB() {
        List<String> cuissons = new ArrayList<>();
        Database db = new Database();
        Connection conn = db.connectDB();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT CUISSON FROM CUISSON_VIANDE");
                while (rs.next()) {
                    cuissons.add(rs.getString("CUISSON"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cuissons;
    }
}








