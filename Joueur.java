package org.example;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Joueur extends Thread implements Observer {
    private final LoterieServer serveur;
    private final List<Billet> billetsAchetes;
    private final CompositeDisposable disposables; // Pour gérer plusieurs abonnements

    private String categorieJ;

    public Joueur(String nom, LoterieServer serveur, String categorie) {
        super(nom); // Le nom du thread est le nom du joueur
        this.serveur = serveur;
        this.billetsAchetes = new ArrayList<>();
        this.disposables = new CompositeDisposable();
        this.categorieJ=categorie;
        serveur.addObserver(this);
    }

    @Override
    public void run() {
        switch (categorieJ) {
            case "Bete" -> jouerBetement();
            case "IntelliJ" -> jouerIntelligemment();
            case "moimeme" -> jouermoimeme();
        }
    }

    private void afficherBilletsAchetes() {

        System.out.println(getName() + " a acheté "+billetsAchetes.size() +" les billets");
    }

    public List<Billet> getBilletsAchetes() {
        return new ArrayList<>(billetsAchetes);
    }

    public int getNombreBilletsAchetes(){
        return billetsAchetes.size();
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "nom='" + getName() + '\'' +
                ", billetsAchetes=" + billetsAchetes +
                '}';
    }


    public LoterieServer getServeur() {
        return serveur;
    }

    public void jouerBetement(){
        Random random = new Random();
        Integer nbaacheter=random.nextInt(serveur.getN()) + 1;
        while (!isInterrupted() && nbaacheter > 0 ) {
            Observable<Billet> achatObservable = serveur.vendreBillet(this);
            Disposable disposable = achatObservable.subscribeOn(Schedulers.io()).subscribe(
                    billet -> {
                        billetsAchetes.add(billet);
                        //System.out.println(getName() + " a acheté un billet: " + billet);
                    },
                    erreur -> {
                        if (erreur instanceof VenteFermeeException || erreur instanceof Exception) {

                            interrupt();

                        } else {
                            System.err.println(getName() + " : Erreur lors de l'achat du billet : " + erreur.getMessage());
                        }
                    }
            );
//            disposables.add(disposable); // Ajouter à la liste des Disposable

            try {
                Thread.sleep(100); // Délai entre les tentatives d'achat
            } catch (InterruptedException e) {
                interrupt();
            }
            nbaacheter--;
        }
    }


    public void jouerIntelligemment(){

        int nombreDeBilletsAcheter = getServeur().getNombreDeBilletsPourGagner();
        while (nombreDeBilletsAcheter > 0) {


            List<Integer> numerosbilletsGagnant = genereNumeroBilletGn(getServeur().getN(), getServeur().getK()); // generer une list des nemeros

            //System.out.println(numerosbilletsGagnant);
            Observable<Billet> achatObservable = getServeur().vendreBillet(numerosbilletsGagnant, this);
            Disposable disposable = achatObservable.subscribe(
                    billet -> {
                        billetsAchetes.add(billet);
                    },
                    erreur -> {
                        if (erreur instanceof VenteFermeeException || erreur instanceof Exception ) {

                            interrupt();

                        } else {
                            System.err.println(getName() + " : Erreur lors de l'achat du billet : " + erreur.getMessage());
                        }
                    }
            );
            try {
                Thread.sleep(100); // Délai entre les tentatives d'achat
            } catch (InterruptedException e) {
                interrupt();
            }
            nombreDeBilletsAcheter--;
        }

    }

    public  List<Integer> genereNumeroBilletGn(int n, int k) {

        Random random = new Random();

        List<Integer> billet = new ArrayList<>();
        for (int j = 0; j < k; j++) {
            // Génération aléatoire des numéros des billets gagnants
            int numero = random.nextInt(n) + 1;
            billet.add(numero);
        }
        return billet;
    }


    @Override
    public void update(String SN) {
        for(Billet billet : billetsAchetes){
            if(SN.contains(billet.getNumeroDeSerie())){
                System.out.println(getName()+" Yupiii j'ai gagner avec le billet "+ billet);
            }
        }
    }

    public void jouermoimeme(){

        boolean reponse = true;
        while (reponse && serveur.isAlive()) {


            List<Integer> numerosbilletsajouer = new ArrayList<>();

            //On demande au joueur de saisir les numeros
            System.out.println("Veuillez saisir les numeros du billet");
            for (int i = 0; i < serveur.getK(); i++) {
                System.out.println("Veuillez saisir le numero " + (i + 1) + " :");
                numerosbilletsajouer.add(new Scanner(System.in).nextInt());
                while (numerosbilletsajouer.get(i) < 1 || numerosbilletsajouer.get(i) > serveur.getN()) {
                    System.out.println("Veuillez saisir un numero entre 1 et " + serveur.getN());
                    numerosbilletsajouer.set(i, new Scanner(System.in).nextInt());
                }
            }

            Observable<Billet> achatObservable = getServeur().vendreBillet(numerosbilletsajouer, this);
            Disposable disposable = achatObservable.subscribe(
                    billet -> {
                        billetsAchetes.add(billet);
                    },
                    erreur -> {
                        if (erreur instanceof VenteFermeeException || erreur instanceof Exception) {

                            interrupt();

                        } else {
                            System.err.println(getName() + " : Erreur lors de l'achat du billet : " + erreur.getMessage());
                        }
                    }
            );
            try {
                Thread.sleep(100); // Délai entre les tentatives d'achat
            } catch (InterruptedException e) {
                interrupt();
            }
            if(!serveur.isAlive()){
                return;
            }
            System.out.println("Voulez vous continuer a jouer ? (o/n)");
            String rep = new Scanner(System.in).nextLine();
            if (rep.equals("n")) {
                reponse = false;
            }

        }


    }
}
