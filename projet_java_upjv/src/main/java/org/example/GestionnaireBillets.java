package org.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GestionnaireBillets {
    private List<Billet> billetsEnregistres;
    private int n; // Nombre total de numéros possibles
    private int k; // Nombre de numéros sur un billet

    public GestionnaireBillets(int n, int k) {
        this.billetsEnregistres = new ArrayList<>();
        this.n = n;
        this.k = k;
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

    // Méthode pour obtenir la liste des billets enregistrés
    public List<Billet> getBilletsEnregistres() {
        return new ArrayList<>(billetsEnregistres);
    }

    private void saveTicketToDisk(Billet billet) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tickets/" + billet.getNumeroDeSerie() + ".ser"))) {
            out.writeObject(billet);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement du billet: " + e.getMessage());
        }
    }


    public static Billet readTicketFromDisk(String filePath) {
        Billet billet = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            billet = (Billet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors de la lecture du fichier de billets: " + e.getMessage());
        }
        return billet;
    }


}
