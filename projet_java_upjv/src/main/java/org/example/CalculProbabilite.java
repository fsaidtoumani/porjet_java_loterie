package org.example;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculProbabilite {

    public static long calculerTempsAchatBillets(int n, int k, int t, long tempsAchatUnBillet) {
        int nombreDeBilletsPourGagner = calculerNombreDeBilletsPourGagner(n, k, t);
        return nombreDeBilletsPourGagner * tempsAchatUnBillet;
    }

    public static int calculerNombreDeBilletsPourGagner(int n, int k, int t){

        int nombreDeBilletsAchetes = 0; // Initialisation avec un billet
        double probabiliteDeGagner = 0.0;

        while (probabiliteDeGagner < 1) {
            nombreDeBilletsAchetes++;
            probabiliteDeGagner = calculerProbabiliteDeGagner(n, k, t, nombreDeBilletsAchetes);
        }

        //System.out.println("Nombre de billets nécessaires pour atteindre une probabilité de gagner de 100 % : " + nombreDeBilletsAchetes);

        return nombreDeBilletsAchetes;
    }





    public static double calculerProbabiliteDeGagner(int n, int k, int t, int nombreDeBilletsAchetes) {
        // Calcul du nomb
        // re de combinaisons de choisir k numéros gagnants parmi n
        long combinaisonsGagnantes = combinaisons(n, k);

        // Calcul du nombre total de combinaisons de choisir k numéros parmi n
        long combinaisonsTotales = combinaisons(n, k);

        // Si t est inférieur à k, ajustez le nombre de combinaisons gagnantes
        if (t < k) {
            combinaisonsGagnantes -= combinaisons(n - t, k);
        }

        // Calcul de la probabilité de gagner
        double probabiliteDeGagner = (double) combinaisonsGagnantes / combinaisonsTotales;

        // Calcul de la probabilité de gagner au moins une fois avec n billets
        double probabiliteDeGagnerAuMoinsUneFois = 1 - Math.pow((1 - probabiliteDeGagner), nombreDeBilletsAchetes);

        return probabiliteDeGagnerAuMoinsUneFois;
    }



    public static long combinaisons(int n, int k) {
        if (k == 0 || k == n) {
            return 1;
        }
        if (k > n) {
            return 0;
        }
        long resultat = 1;
        for (int i = 1; i <= k; i++) {
            resultat *= (n - i + 1);
            resultat /= i;
        }
        return resultat;
    }


    public static List<List<Integer>> genererBilletsGagnants(int n, int k, int nombreDeBillets) {
        List<List<Integer>> billets = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < nombreDeBillets; i++) {
            List<Integer> billet = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                // Génération aléatoire des numéros des billets gagnants
                int numero = random.nextInt(n) + 1;
                billet.add(numero);
            }
            billets.add(billet);
        }
        return billets;
    }

}
