package org.example;

public class Verificationjeu {

    public static  boolean etatlegit = true;
    public static int verificationIntelligent(Integer n, Integer k, Integer t) {


        // Calcule le nombre de combinaisons gagnantes
        long winningCombinations = 1;
        for (int i = 0; i < k - t; i++) {
            winningCombinations *= (n - k + i);
            winningCombinations /= (i + 1);
        }

        // Calcule le nombre total de combinaisons possibles
        long totalCombinations = 1;
        for (int i = 0; i < k; i++) {
            totalCombinations *= (n - i);
            totalCombinations /= (i + 1);
        }

        return (int) Math.ceil((double) totalCombinations / winningCombinations);
    }
    public static boolean  joueurLegit(LoterieServer server, Joueur joueur ){
        Integer nbilletcat2=0;
        //Parcourir la liste des billet du joeur
        for (Billet billet2 : joueur.getBilletsAchetes()){
            if (billet2.getCategorie() == Categorie.CATEGORIE_II){
                nbilletcat2++;
            }
        }
        etatlegit= nbilletcat2 < server.getNombreDeBilletsPourGagner()-1;
        return etatlegit;
    }
}
