package org.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GestionnaireBillets {
    private List<Billet> billetsEnregistres;
    private int n; // Nombre total de numéros possibles
    private int k; // Nombre de numéros sur un billet

    private int t; // Nombre minimum de numéros gagnants pour gagner

    public GestionnaireBillets(int n, int k, int t) {
        this.billetsEnregistres = new ArrayList<>();
        this.n = n;
        this.k = k;
        this.t = t;
    }

    // Méthode pour générer aléatoirement des numéros de billet
    private List<Integer> genererNumerosAleatoires() {
        return ThreadLocalRandom.current().ints(1, n + 1)
                .distinct().limit(k)
                .boxed()
                .collect(Collectors.toList());
    }

    // Méthode pour créer un nouveau billet
    public Billet creerBillet() {
        List<Integer> numeros = genererNumerosAleatoires();
        Billet nouveauBillet = new Billet(numeros, Categorie.CATEGORIE_I);
        billetsEnregistres.add(nouveauBillet);
        saveTicketToDisk(nouveauBillet);
        return nouveauBillet;
    }

    public Billet creerBillet(List<Integer> numeros) {
        Billet nouveauBillet = new Billet(numeros, Categorie.CATEGORIE_II);
        billetsEnregistres.add(nouveauBillet);
        saveTicketToDisk(nouveauBillet);
        return nouveauBillet;
    }



    private void saveTicketToDisk(Billet billet) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tickets/" + billet.getNumeroDeSerie() + ".ser"))) {
            out.writeObject(billet);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement du billet: " + e.getMessage());
        }
    }

    public String listeIdBilletGagnant(List<Integer> numerosGagnants) {
        StringBuilder listidgagnant = new StringBuilder();
        File dossier = new File("tickets");

        if (!dossier.exists() || !dossier.isDirectory()) {
            System.err.println("Le dossier de billets n'existe pas.");
            return null;
        }

        File[] listeFichiers = dossier.listFiles();
        if (listeFichiers == null) {
            System.err.println("Erreur lors de la lecture du dossier.");
            return null;
        }

        for (File fichier : listeFichiers) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichier))) {
                Billet billet = (Billet) in.readObject();

                if (estGagnant(billet, numerosGagnants)) {
                    if (listidgagnant.length() > 0) {
                        listidgagnant.append("-");
                    }
                    listidgagnant.append(billet.getNumeroDeSerie());
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lors de la lecture du billet: " + e.getMessage());
            }
        }
        return listidgagnant.toString();
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

    public List<Integer> numerogagnantverife(List<Integer> numerosGagnants) {
        List<Integer> numerosGagnantsVerifies = new ArrayList<>();
        File dossier = new File("tickets");

        if (!dossier.exists() || !dossier.isDirectory()) {
            System.err.println("Le dossier de billets n'existe pas.");
            return null;
        }

        File[] listeFichiers = dossier.listFiles();
        if (listeFichiers == null) {
            System.err.println("Erreur lors de la lecture du dossier.");
            return null;
        }

        for (File fichier : listeFichiers) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichier))) {
                Billet billet = (Billet) in.readObject();
                int compteur = 0;
                for (Integer numero : billet.getNumeros()) {
                    if (numerosGagnants.contains(numero)) {
                        compteur++;
                    }
                }
                if (compteur >= t) {
                    return numerosGagnants;
                }


            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lors de la lecture du billet: " + e.getMessage());
            }
        }

        // On choisit un billet gagnant au hasard
        int index = (int) (Math.random() * listeFichiers.length  );
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream( listeFichiers[index] ))) {
            Billet billet = (Billet) in.readObject();
            numerosGagnantsVerifies = billet.getNumeros();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la lecture du billet: " + e.getMessage());
        }

       return  numerosGagnantsVerifies;
    }
}
