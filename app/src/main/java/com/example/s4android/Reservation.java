package com.example.s4android;


public class Reservation {
    private String nom;
    private String telephone;
    private String date;
    private String horaire;
    private int nbPersonnes;

    public Reservation(String nom, String telephone, String date, String horaire, int nbPersonnes) {
        this.nom = nom;
        this.telephone = telephone;
        this.date = date;
        this.horaire = horaire;
        this.nbPersonnes = nbPersonnes;
    }

    public String getNom() { return nom; }
    public String getTelephone() { return telephone; }
    public String getDate() { return date; }
    public String getHoraire() { return horaire; }
    public int getNbPersonnes() { return nbPersonnes; }
}

