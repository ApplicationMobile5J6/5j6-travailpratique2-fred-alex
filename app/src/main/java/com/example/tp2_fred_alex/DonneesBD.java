package com.example.tp2_fred_alex;

import java.io.Serializable;

public class DonneesBD implements Serializable {
    public String pays;
    public String ville;
    public String age;
    public String statut;


    public DonneesBD() {
    }
    public DonneesBD(String pPays, String pVille, String pAge, String pStatut){
        this.pays = pPays;
        this.ville = pVille;
        this.age = pAge;
        this.statut = pStatut;
    }


}
