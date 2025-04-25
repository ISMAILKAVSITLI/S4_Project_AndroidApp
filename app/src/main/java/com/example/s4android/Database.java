package com.example.s4android;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;

import javax.xml.transform.sax.SAXResult;


public class Database {

    private static final String URL = "jdbc:mysql://10.0.2.2:3306/nouveaus4";
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

    public Reservation getReservationForTable(Connection conn, int idTable) {
        Reservation reservation = null;
        try {
            String sql = "SELECT ID_RESERVATION, NOM_CLIENT, TELEPHONE, DATE_RESERVATION, HORAIRE, NOMBRE_PERSONNE, ID_TABLES " +
                    "FROM RESERVATION " +
                    "JOIN TABLES ON RESERVATION.ID_TABLES = TABLES.ID_TABLES " +
                    "WHERE RESERVATION.DATE_RESERVATION = CURRENT_DATE" +
                    "AND RESERVATION.HORAIRE >=CURRENT_TIME";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idTable);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("ID_RESERVATION");
                String nom = rs.getString("NOM_CLIENT");
                String telephone = rs.getString("TELEPHONE");
                String date = rs.getString("DATE_RESERVATION");
                String horaire = rs.getString("HORAIRE");
                int nbPersonnes = rs.getInt("NOMBRE_PERSONNE");

                reservation = new Reservation(id, nom, telephone, date, horaire, nbPersonnes);
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservation;
    }

    public List<Reservation> getAllReservationsForTable(Connection conn, int idTable) {
        List<Reservation> reservations = new ArrayList<>();
        try {
            String sql = "SELECT ID_RESERVATION, NOM_CLIENT, TELEPHONE, DATE_RESERVATION, HORAIRE, NOMBRE_PERSONNE, ID_TABLES " +
                    "FROM RESERVATION WHERE ID_TABLES = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idTable);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID_RESERVATION");
                String nom = rs.getString("NOM_CLIENT");
                String telephone = rs.getString("TELEPHONE");
                String date = rs.getString("DATE_RESERVATION");
                String horaire = rs.getString("HORAIRE");
                int nbPersonnes = rs.getInt("NOMBRE_PERSONNE");

                reservations.add(new Reservation(id, nom, telephone, date, horaire, nbPersonnes));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }


    public String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    public void deleteReservation(Connection conn, int idReservation) {
        try {
            Statement stmt = conn.createStatement();
            String query = "DELETE FROM RESERVATION WHERE ID_RESERVATION = " + idReservation;
            stmt.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







}




