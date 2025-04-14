package com.example.s4android;

public class Statut_table {
    private int ID_STATUT_TABLE;

    private int ID_SECTEUR;
    private String STATUT_TABLE;

    public Statut_table(int id, int secteur, String nom) {
        this.ID_STATUT_TABLE = id;
        this.ID_SECTEUR = secteur;
        this.STATUT_TABLE = nom;
    }

    public int getId() {
        return ID_STATUT_TABLE;
    }

    public String getNom() {
        return STATUT_TABLE;
    }

    public int getSecteur() {
        return ID_SECTEUR;
    }


    @Override
    public String toString() {
        return "ID_STATUT_TABLE= " + ID_STATUT_TABLE + ", ID_SECTEUR =" +ID_SECTEUR + ", STATUT_TABLE =" + STATUT_TABLE;
    }
}
