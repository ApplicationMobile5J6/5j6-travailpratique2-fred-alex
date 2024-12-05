package com.example.tp2_fred_alex;

public class Utilisateur {

private String nomcomplet;
private String email;
private  String motdepasse;
    private String cheminImage;


    public Utilisateur (String nomcomplet,String email)

    {
this.nomcomplet=nomcomplet;
this.email=email;

    }



    public String getCheminImage() {
        return cheminImage;
    }

    public void setCheminImage(String cheminImage) {
        this.cheminImage = cheminImage;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomcomplet() {
        return nomcomplet;
    }

    public void setNomcomplet(String nomcomplet) {
        this.nomcomplet = nomcomplet;
    }





}
