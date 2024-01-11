package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CalculProbabilite_old {

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

        System.out.println("Nombre de billets nécessaires pour atteindre une probabilité de gagner de 100 % : " + nombreDeBilletsAchetes);

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

//    public static int[][] genererBilletsGagnants(int n, int k, int nombreDeBillets) {
//        int[][] billets = new int[nombreDeBillets][k];
//        Random random = new Random();
//
//        for (int i = 0; i < nombreDeBillets; i++) {
//            for (int j = 0; j < k; j++) {
//                // Génération aléatoire des numéros des billets gagnants
//                int numero = random.nextInt(n) + 1;
//                billets[i][j] = numero;
//            }
//        }
//        return billets;
//    }

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

    public static void ecrireBilletsDansFichier(int[][] billets, String nomFichier) {
        try (FileWriter fileWriter = new FileWriter(nomFichier);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            for (int i = 0; i < billets.length; i++) {
                // printWriter.println("Billet " + (i + 1) + ": ");
                for (int j = 0; j < billets[i].length; j++) {
                    printWriter.print(billets[i][j] + " ");
                }
                printWriter.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean verifierSiAuMoinsUnBilletGagnant(int[][] billets, int n, int k, int t) {
        // Génération des numéros gagnants
        int[] numerosGagnants = new int[k];
        Random random = new Random();

        for (int i = 0; i < k; i++) {
            int numero = random.nextInt(n) + 1;
            numerosGagnants[i] = numero;
        }

        // Affichage des numéros gagnants
        afficherNumerosGagnants(numerosGagnants);

        // Vérification de la condition d'au moins un billet gagnant parmi les billets générés
        for (int i = 0; i < billets.length; i++) {
            int billetGagnant = 0;
            for (int j = 0; j < k; j++) {
                if (contains(billets[i], numerosGagnants[j])) {
                    billetGagnant++;
                }
            }
            if (billetGagnant >= t) {
                return true;
            }
        }

        return false;
    }




    public static boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

    public static void afficherNumerosGagnants(int[] numerosGagnants) {
        System.out.println("Numéros Gagnants : ");
        for (int numero : numerosGagnants) {
            System.out.print(numero + " ");
        }
        System.out.println();
    }



    //    // Calcule la probabilité de gagner pour un nombre donné de billets
//    public static double calculerProbabilite(int n, int k, int t, int nombreDeBillets) {
//        BigInteger totalCombinations = combinaisons(n, k);
//        BigInteger winningCombinations = BigInteger.ZERO;
//
//        // Calculer le nombre total de combinaisons gagnantes
//        for (int i = t; i <= k; i++) {
//            winningCombinations = winningCombinations.add(combinaisons(k, i).multiply(combinaisons(n - k, k - i)));
//        }
//
//        // Calculer la probabilité de gagner avec un seul billet
//        double probUnBillet = new BigInteger("1").divide(totalCombinations).doubleValue();
//
//        // Calculer la probabilité de gagner avec le nombre donné de billets
//        // Utilisation de l'approche de complément: 1 - prob de ne pas gagner avec tous les billets
//        double probNePasGagnerUnBillet = 1 - probUnBillet;
//        double probNePasGagnerTousBillets = Math.pow(probNePasGagnerUnBillet, nombreDeBillets);
//
//        return 1 - probNePasGagnerTousBillets;
//    }

//    // Méthode utilitaire pour calculer les combinaisons C(n, k)
//    private static BigInteger combinaisons(int n, int k) {
//        return factorial(n).divide(factorial(k).multiply(factorial(n - k)));
//    }

//    // Méthode utilitaire pour calculer n!
//    private static BigInteger factorial(int n) {
//        BigInteger result = BigInteger.ONE;
//        for (int i = 2; i <= n; i++) {
//            result = result.multiply(BigInteger.valueOf(i));
//        }
//        return result;
//    }



}
