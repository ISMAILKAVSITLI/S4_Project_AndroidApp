package com.example.s4android;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import android.os.StrictMode;
import android.util.Log;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.transform.sax.SAXResult;


public class Database {

    private static final String URL = "jdbc:mysql://100.73.248.3:3306/s4";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Connection connectDB() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Log.d("db_log", "Connexion réussie");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("db_log", "Erreur connexion: " + e.toString());
            return null;
        }
    }

    public List<Statut_table> getStatuts(Connection connection, int secteur) {
        List<Statut_table> statuts = new ArrayList<>();
        try {
            String query = "SELECT * FROM tables WHERE ID_SECTEUR = " + secteur;
            Log.d("Database", "Requête SQL : " + query);  // Affichage de la requête SQL
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("ID_TABLES");
                int idSecteur = rs.getInt("ID_SECTEUR");
                String nom = rs.getString("ID_STATUT_TABLE");
                statuts.add(new Statut_table(id, idSecteur, nom));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Database", "Erreur requête: " + e.toString());
        }
        return statuts;
    }



}
