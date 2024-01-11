package org.example;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if(categorieJ.equals("Bete")){
            jouerBetement();
        } else if (categorieJ.equals("IntelliJ")) {
            jouerIntelligemment();

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
                        if (erreur instanceof VenteFermeeException) {

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
        System.out.println("Flag"+nombreDeBilletsAcheter);
        while (nombreDeBilletsAcheter > 0) {


            List<Integer> numerosbilletsGagnant = genereNumeroBilletGn(getServeur().getN(), getServeur().getK()); // generer une list des nemeros

            //System.out.println(numerosbilletsGagnant);
            Observable<Billet> achatObservable = getServeur().vendreBillet(numerosbilletsGagnant, this);
            Disposable disposable = achatObservable.subscribe(
                    billet -> {
                        billetsAchetes.add(billet);
                    },
                    erreur -> {
                        if (erreur instanceof VenteFermeeException) {

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
}
