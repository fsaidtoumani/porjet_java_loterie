package org.example;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Billet implements Serializable {
    private String numeroDeSerie;
    private final List<Integer> numeros;

    private final Categorie categorie;





    public Billet(List<Integer> numeros, Categorie categorie) {
        try {
            this.numeroDeSerie = Transaction.generateUniqueId();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        this.numeros = numeros;
        this.categorie = categorie;
    }



    public Categorie getCategorie() {
        return categorie;
    }

    // Getters et autres méthodes
    public String getNumeroDeSerie() {
        return numeroDeSerie;
    }

    public List<Integer> getNumeros() {
        return numeros;
    }

    public int nombreNumerosGagnants(List<Integer> numerosGagnants) {
        int compteur = 0;
        for (int numero : numeros) {
            if (numerosGagnants.contains(numero)) {
                compteur++;
            }
        }
        return compteur;
    }

    @Override
    public String toString() {
        return "Billet{" +
                "sn='" + numeroDeSerie + '\'' +
                ", numeros=" + numeros +
                ", categorie=" + categorie +
                '}';
    }

}
