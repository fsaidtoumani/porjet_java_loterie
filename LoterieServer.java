package org.example;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class LoterieServer extends Thread implements org.example.Observable {
    private int n; // Nombre total de numéros possibles
    private int k; // Nombre de numéros sur un billet
    private int t; // Nombre minimum de numéros gagnants pour gagner
    private long duration; // Durée de la loterie en millisecondes
    private List<Integer> numerosGagnants;
    private final GestionnaireBillets gestionnaireBillets;
    private Tirage tirage;


    public int getN() {
        return n;
    }

    public int getK() {
        return k;
    }

    public int getT() {
        return t;
    }

    private boolean venteOuverte;

    public long getRemainingTime() {
        return duration;
    }

    private ArrayList<Observer> observers;

    private final int nombreDeBilletsPourGagner;


    private final Object lock = new Object(); // Un objet de verrouillage


    private int synctest;

    public LoterieServer(int n, int k, int t, long duration) {
        this.n = n;
        this.k = k;
        this.t = t;
        this.duration = duration;

        this.numerosGagnants = new ArrayList<>();
        this.gestionnaireBillets = new GestionnaireBillets(n, k, t);
        this.venteOuverte = false;

        this.observers = new ArrayList<>();


        this.nombreDeBilletsPourGagner = Verificationjeu.verificationIntelligent(n, k, t);

        this.duration = duration;

    }

    public Observable<Billet> vendreBillet(Joueur joueur) {  // methode vendre Billet synchro !
        return Observable.<Billet>create(emitter -> {
                synchronized (lock) {
                    if (!venteOuverte) {
                        emitter.onError(new VenteFermeeException("La vente de billets est fermée."));
                    } else {
                        try {

                            synctest++; // Incrément synchronisé
                            Billet billet = gestionnaireBillets.creerBillet();
                            emitter.onNext(billet);
                            emitter.onComplete();
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }
        }).subscribeOn(Schedulers.newThread()); // Souscription sur un nouveau thread
    }

    public Observable<Billet> vendreBillet(List<Integer> numeros, Joueur joueur) {
        // Création de l'Observable avec un type spécifié
        return Observable.<Billet>create(emitter -> {
            synchronized (lock) {
                if (!Verificationjeu.joueurLegit(this, joueur) || !venteOuverte) {
                    emitter.onError(new VenteFermeeException("La vente de billets est fermée."));
                } else {
                    try {
                        Billet billet = gestionnaireBillets.creerBillet(numeros);
                        emitter.onNext(billet);
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }
            }
        }).subscribeOn(Schedulers.newThread()); // Souscription sur un nouveau thread
    }

    private void clearCash() {
        File repertoireTickets = new File("tickets");
        if (repertoireTickets.exists() && repertoireTickets.isDirectory()) {
            File[] fichiers = repertoireTickets.listFiles();

            if (fichiers != null) {
                for (File fichier : fichiers) {
                    if (fichier.isFile()) {
                        if (fichier.delete()) {
//                            System.out.println("Fichier supprimé : " + fichier.getName());
                        } else {
                            System.err.println("Impossible de supprimer le fichier : " + fichier.getName());
                        }
                    }
                }
            }
        } else {
            System.err.println("Le répertoire 'tickets' n'existe pas.");
        }
    }

    public int getNombreDeBilletsPourGagner() {
        return nombreDeBilletsPourGagner;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String SN) {
        for (Observer observer : observers) {
            observer.update(SN);
        }
    }

    @Override
    public void run() {
        System.out.println("server started ...");
        System.out.println("Achat des billets ouvert ...");
        this.venteOuverte = true;

        try {

            while (this.duration >=0 && Verificationjeu.etatlegit){
                this.duration-=1000;
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.venteOuverte=false;
        System.out.println("Achat des billets fermé ...");

        System.out.println("La vente de billets est maintenant fermée....");

//
        this.tirage = new Tirage(this.n,this.k,this.t);
        this.numerosGagnants = tirage.effectuerTirage(gestionnaireBillets);
//
//        // Afficher les numéros gagnants
        System.out.println("[ Serveur ] Numéros gagnants: " + numerosGagnants);

        notifyObservers(gestionnaireBillets.listeIdBilletGagnant(numerosGagnants));

        clearCash();
    }

}
