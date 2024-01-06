package org.example;

import io.reactivex.rxjava3.core.Observable;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class LoterieServer extends Thread implements org.example.Observable {
    private int n; // Nombre total de numéros possibles
    private int k; // Nombre de numéros sur un billet
    private int t; // Nombre minimum de numéros gagnants pour gagner
    private long duration; // Durée de la loterie en millisecondes

    private List<Billet> billetsVendus;
    private List<Integer> numerosGagnants;

    private final GestionnaireBillets gestionnaireBillets;
    private Tirage tirage;

    private boolean venteOuverte;
    private ScheduledExecutorService scheduler;
    private ExecutorService threadPool;

    private ArrayList<Observer> observers;

    private long TEMPS_ACHAT_UN_BILLET = 1000; // 1000ms
    private final int nombreDeBilletsPourGagner;
    private long tempsAchatBilletsPourGagner;

    private final Object lock = new Object(); // Un objet de verrouillage

    private Map<Joueur, Integer> billetsParJoueur; // Suivi des achats de billets par joueur


    private int synctest;

    public LoterieServer(int n, int k, int t, long duration) {
        this.n = n;
        this.k = k;
        this.t = t;
        this.duration = duration;

        this.billetsVendus = new CopyOnWriteArrayList<>();
        this.numerosGagnants = new ArrayList<>();
        this.gestionnaireBillets = new GestionnaireBillets(n, k);
        this.venteOuverte = false;

        this.observers = new ArrayList<>();
        this.threadPool = Executors.newFixedThreadPool(10);
        this.scheduler = Executors.newScheduledThreadPool(1);

        this.nombreDeBilletsPourGagner = CalculProbabilite.calculerNombreDeBilletsPourGagner(n, k, t);
        this.tempsAchatBilletsPourGagner = CalculProbabilite.calculerTempsAchatBillets(n, k, t, TEMPS_ACHAT_UN_BILLET);
        this.duration = Math.min(duration, tempsAchatBilletsPourGagner);

        this.billetsParJoueur = new ConcurrentHashMap<>();


    }

    public Observable<Billet> vendreBillet(Joueur joueur) {  // methode vendre Billet synchro !
        return Observable.create(emitter -> {
            synchronized (lock) {
                if (!venteOuverte) {
                    emitter.onError(new VenteFermeeException("La vente de billets est fermée."));
                } else {
                    try {
                        synctest++; // Incrément synchronisé
                        Billet billet = gestionnaireBillets.creerBillet();
                        billetsVendus.add(billet);
                        emitter.onNext(billet);
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }
            }
        });
    }

    public Observable<Billet> vendreBillet(List<Integer> numeros,Joueur joueur) {
        return Observable.create(emitter -> {
            synchronized (lock) {
                if (!venteOuverte) {
                    emitter.onError(new VenteFermeeException("La vente de billets est fermée."));
//                } else if (joueur.getNombreBilletsAchetes() >= nombreDeBilletsPourGagner) {
//                    emitter.onError(new LimiteAchatAtteinteException("Limite d'achat atteinte pour " + joueur.getName()));
                } else {
                    try {
                        Billet billet = gestionnaireBillets.creerBillet(numeros);
                        billetsVendus.add(billet);
                        emitter.onNext(billet);
                        emitter.onComplete();
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                }
            }
        });
    }

    private void attendreTraitement() {
        try {
            Thread.sleep(1000); // Simuler un délai pour le traitement
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interruption pendant l'attente du traitement", e);
        }
    }

    public void afficherBilletsGagnants() {
        System.out.println("Numéros gagnants: " + this.numerosGagnants);

        List<Billet> billets = gestionnaireBillets.getBilletsEnregistres();
        for (Billet billet : billets) {
            if (estGagnant(billet,this.numerosGagnants)) {
                System.out.println("Billet gagnant: " + billet);
                // on notifie les observers (Joueurs) on passant le sn (Serial Number comme arg
                notifyObservers(billet.getNumeroDeSerie());
            }
        }
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
            Thread.sleep(this.duration);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        this.venteOuverte=false;
        System.out.println("Achat des billets fermé ...");

        System.out.println("La vente de billets est maintenant fermée....");

//
        this.tirage = new Tirage(this.n,this.k,this.t,this.billetsVendus);
        this.numerosGagnants = tirage.effectuerTirage();
//
//        // Afficher les numéros gagnants
//        System.out.println("Numéros gagnants: " + numerosGagnants);

        afficherBilletsGagnants();
        System.out.println(" sync : " + synctest);
        clearCash();
    }

}
