
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
        System.out.println("Tirage en cours");
        /*
         * chois des k numero ganant
         */
        ArrayList<Integer> numerosgagnants = new ArrayList<Integer>();
        for (int i = 0; i < k; i++) {
            numerosgagnants.add((int) (Math.random() * n + 1));
        }
        /*
        On vérifie si avec nos numeros il ya un gagnant
         */
        for (Billet billet : billetsVendus) {
            int compteur = 0;
            for (Integer numero : billet.getNumeros()) {
                if (numerosgagnants.contains(numero)) {
                    compteur++;
                }
            }
            if (compteur >= t) {
                return numerosgagnants;
            }
        }


        // On choisit un billet gagnant au hasard
        int index = (int) (Math.random() * billetsVendus.size());
        return billetsVendus.get(index).getNumeros();


    }

}
