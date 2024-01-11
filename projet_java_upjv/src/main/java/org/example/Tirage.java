
package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Tirage {
    private int n;
    private int k;
    private int t;
    private List<Billet> billetsVendus;

    public Tirage(int n, int k, int t,List<Billet> billetsVendus) {
        this.n = n;
        this.k = k;
        this.t = t;
        this.billetsVendus=billetsVendus;
    }


    public List<Integer> effectuerTirage() {
        List<Integer> numerosGagnants = new ArrayList<>();
        Random random = new Random();

        // Génération initiale de k numéros gagnants
        while (numerosGagnants.size() < k) {
            int numero = random.nextInt(n) + 1;
            if (!numerosGagnants.contains(numero)) {
                numerosGagnants.add(numero);
            }
        }

        // Vérification de la condition d'au moins un gagnant parmi les billets vendus
        while (!auMoinsUnGagnant(numerosGagnants)) {
            // Génération d'un nouveau numéro
            int nouveauNumero = random.nextInt(n) + 1;
            if (!numerosGagnants.contains(nouveauNumero)) {
                // Remplacement d'un numéro existant de manière aléatoire
                int indexARemplacer = random.nextInt(k);
                numerosGagnants.set(indexARemplacer, nouveauNumero);
            }
        }

        return numerosGagnants;
    }



    private boolean auMoinsUnGagnant(List<Integer> numerosGagnants) {
        // Vérifie s'il existe au moins un billet gagnant parmi les billets vendus
        for (Billet billet : billetsVendus) {
            if (estGagnant(billet, numerosGagnants)) {
                return true;
            }
        }
        return false;
    }

    public  boolean estGagnant(Billet billet, List<Integer> numerosGagnants) {
        List<Integer> numerosDuBillet = billet.getNumeros();
        int numerosGagnantsCommuns = 0;
        for (int numero : numerosGagnants) {
            if (numerosDuBillet.contains(numero)) {
                numerosGagnantsCommuns++;
            }
        }
        return numerosGagnantsCommuns >= this.t;
    }

}
