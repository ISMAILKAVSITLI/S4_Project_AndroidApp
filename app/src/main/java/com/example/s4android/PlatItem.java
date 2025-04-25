package com.example.s4android;


public class PlatItem {
    private String nom;
    private double prix;
    private int compteur;
    private String commentaire;
    private boolean contientViande;  // Nouveau champ pour savoir si le plat contient de la viande

    private boolean contientSauce;

    // Constructor
    public PlatItem(String nom, double prix) {
        this.nom = nom;
        this.prix = prix;
        this.compteur = 0;
        this.commentaire = "";
        this.contientViande = false;  // Par défaut, on suppose que ça ne contient pas de viande
        this.contientSauce = false;
    }

    // Getters et Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getCompteur() {
        return compteur;
    }

    public void incrementer() {
        compteur++;
    }

    public void decrementer() {
        if (compteur > 0) {
            compteur--;
        }
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public boolean getContientViande() {
        return contientViande;
    }

    public void setContientViande(boolean contientViande) {
        this.contientViande = contientViande;
    }

    public void setContientSauce(boolean contientSauce) {
        this.contientSauce = contientSauce;
    }

    public boolean getContientSauce() {
        return contientSauce;
    }

    public void reset() {
        this.compteur = 0;
        this.commentaire = "";
    }

    public void setCompteur(int compteur) {
        this.compteur = compteur;
    }
}
