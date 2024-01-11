package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Tirage_old {
    private int n; // Nombre total de numéros possibles
    private int k; // Nombre de numéros gagnants
    private int t; // Borne pour les billets gagnants
    private List<Billet> billetsVendus;

    public Tirage_old(int n, int k, int t,List<Billet> billetsVendus) {
        this.n = n;
        this.k = k;
        this.t = t;
        this.billetsVendus=billetsVendus;
    }

//    public List<Integer> effectuerTirage() {
//        List<Integer> numerosGagnants = new ArrayList<>();
//        Random random = new Random();
//
//        // Génération initiale de k numéros gagnants
//        while (numerosGagnants.size() < k) {
//            int numero = random.nextInt(n) + 1;
//            if (!numerosGagnants.contains(numero)) {
//                numerosGagnants.add(numero);
//            }
//        }
//
//        // Vérification de la condition d'au moins un gagnant
//        while (!auMoinsUnGagnant(numerosGagnants)) {
//            // Génération d'un nouveau numéro
//            int nouveauNumero = random.nextInt(n) + 1;
//            if (!numerosGagnants.contains(nouveauNumero)) {
//                // Remplacement d'un numéro existant de manière aléatoire
//                int indexARemplacer = random.nextInt(k);
//                numerosGagnants.set(indexARemplacer, nouveauNumero);
//            }
//        }
//
//        return numerosGagnants;
//    }


    public List<Integer> genererNumerosGagnants() {
        return ThreadLocalRandom.current().ints(1, n + 1)
                .distinct().limit(k)
                .boxed()
                .collect(Collectors.toList());
    }

    public List<Integer> effectuerTirage() {

//        if (t == k) {
//            // Si t est égal à k, choisissez un billet gagnant au hasard parmi les billets vendus.
//            if (!billetsVendus.isEmpty()) {
//                Billet billetGagnant = billetsVendus.get(new Random().nextInt(billetsVendus.size()));
//                return true;
//            }
//        }

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

//    private boolean estGagnant(Billet billet) {
//        List<Integer> numerosDuBillet = billet.getNumeros();
////        numerosDuBillet.retainAll(this.numerosGagnants); // Garde uniquement les numéros gagnants
//        int temp = billet.nombreNumerosGagnants(this.numerosGagnants);
////        return numerosDuBillet.size() >= this.t; // T est le seuil pour gagner
//        return temp >= this.t; // T est le seuil pour gagner
//    }

    public boolean verifierSiGagnant(Billet billet,List<Integer> numerosGagnants) {
        if (billet == null) {
            return false; // Billet non trouvé
        }
        return billet.nombreNumerosGagnants(numerosGagnants) >= t;
    }


    private void verifierBilletsGagnants(List<Integer> numerosGagnants) {
        System.out.println("Billets gagnants:");
        for (Billet billet : billetsVendus) {
//            boolean estGagnant = verifierSiBilletGagnant(billet, numerosGagnants);
            boolean estGagnant = verifierSiGagnant(billet,numerosGagnants);
            if (estGagnant) {
                System.out.println(billet);
            }
        }
    }


    public List<Integer> effectuerTirage_v2() {
        List<Integer> numerosGagnants = new ArrayList<>();
        List<Integer> numerosReserves = new ArrayList<>();
        Random random = new Random();

        // Génération initiale de k numéros gagnants distincts
        while (numerosGagnants.size() < k) {
            int numero = random.nextInt(n) + 1;
            if (!numerosGagnants.contains(numero)) {
                numerosGagnants.add(numero);
            }
        }

        // Génération de k-1 numéros de réserve distincts
        while (numerosReserves.size() < k - 1) {
            int numero = random.nextInt(n) + 1;
            if (!numerosReserves.contains(numero) && !numerosGagnants.contains(numero)) {
                numerosReserves.add(numero);
            }
        }

        // Vérification des billets pour trouver un gagnant
        while (true) {
            boolean gagnantTrouve = verifierBilletsPourGagnant_v1(numerosGagnants, numerosReserves,this.billetsVendus);
            if (gagnantTrouve) {
                break;
            }
            // Si aucun gagnant n'est trouvé, réessayez avec un nouveau billet.
            // Vous devrez implémenter la logique pour vérifier les billets vendus.
        }

        return numerosGagnants;
    }

    private boolean verifierBilletsPourGagnant_v1(List<Integer> numerosGagnants, List<Integer> numerosReserves, List<Billet> billetsVendus) {
        for (Billet billet : billetsVendus) {
            List<Integer> numerosDuBillet = billet.getNumeros();

            // Vérifiez d'abord si le billet contient tous les numéros gagnants initiaux.
            if (numerosDuBillet.containsAll(numerosGagnants)) {
                return true; // Le billet est gagnant.
            }

            // Ensuite, vérifiez si le billet contient au moins k-1 numéros gagnants initiaux et un numéro de réserve.
            int numerosGagnantsCommuns = 0;
            int numerosReservesCommuns = 0;

            for (int numero : numerosGagnants) {
                if (numerosDuBillet.contains(numero)) {
                    numerosGagnantsCommuns++;
                }
            }

            for (int numero : numerosReserves) {
                if (numerosDuBillet.contains(numero)) {
                    numerosReservesCommuns++;
                }
            }

            if (numerosGagnantsCommuns >= k - 1 && numerosReservesCommuns >= 1) {
                return true; // Le billet est gagnant.
            }
        }

        return false; // Aucun gagnant parmi les billets vendus.
    }


    public List<Integer> effectuerTirage_v3() {
        List<Integer> numerosGagnants = new ArrayList<>();
        Random random = new Random();

        // Génération du numéro de segment gagnant
        int segmentGagnant = random.nextInt((n - k + 1) / k) + 1;

        // Trouver un billet gagnant couvrant le segment gagnant
        Billet billetGagnant = trouverBilletGagnant(segmentGagnant);

        // Ajouter les numéros du billet gagnant aux numéros gagnants
        numerosGagnants.addAll(billetGagnant.getNumeros());

        return numerosGagnants;
    }

    private Billet trouverBilletGagnant(int segmentGagnant) {
        // Parcourez les billets vendus pour trouver un billet qui couvre le segment gagnant.
        for (Billet billet : billetsVendus) {
            List<Integer> numerosBillet = billet.getNumeros(); // Obtenez les numéros du billet

            // Vérifiez si le billet couvre le segment gagnant en examinant ses numéros
            for (int numero : numerosBillet) {
                int segmentDuNumero = (numero - 1) / k + 1; // Calculez le segment du numéro
                if (segmentDuNumero == segmentGagnant) {
                    return billet; // Le billet couvre le segment gagnant, donc c'est un gagnant.
                }
            }
        }
        return null; // Aucun billet gagnant trouvé pour le segment gagnant.
    }


    public List<Integer> effectuerTirage_v4() {
        List<Integer> numerosGagnants = new ArrayList<>();
        Billet billet = choisirBilletGagnant();

        return billet.getNumeros();
    }

    public Billet choisirBilletGagnant() {
        if (!billetsVendus.isEmpty()) {
            Random random = new Random();
            int indexBilletGagnant = random.nextInt(billetsVendus.size());
            return billetsVendus.get(indexBilletGagnant);
        }
        return null; // Aucun billet vendu, pas de gagnant.
    }


}
