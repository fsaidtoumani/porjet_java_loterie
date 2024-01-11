package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import java.util.stream.Collectors;


import io.reactivex.rxjava3.core.Observable;

public class LoterieServer_old extends Thread implements org.example.Observable {

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

    private ArrayList<Observer> observers; // pour passer les numeros de serie des billets gagants et donc les joueurs vont comparer leur buillets avec les billets gagnant

    private long TEMPS_ACHAT_UN_BILLET = 1000; // 1000ms
    private final int nombreDeBilletsPourGagner;
    private long tempsAchatBilletsPourGagner;

    private int sync;
    private final Object lock = new Object(); // Un objet de verrouillage

    public LoterieServer_old(int n, int k, int t, long duration) {
        this.n = n;
        this.k = k;
        this.t = t;
        this.duration = duration;

        this.billetsVendus = Collections.synchronizedList(new ArrayList<>());
        this.numerosGagnants = new ArrayList<>();
        this.venteOuverte = false;
        this.gestionnaireBillets = new GestionnaireBillets(n,k);
//        this.tirage = new Tirage(n,k,t);


        this.observers = new ArrayList<>();

        this.threadPool = Executors.newFixedThreadPool(10); // Nombre de threads à définir selon les besoins
        this.scheduler = Executors.newScheduledThreadPool(1);

        this.nombreDeBilletsPourGagner = CalculProbabilite.calculerNombreDeBilletsPourGagner(n, k, t);
        this.tempsAchatBilletsPourGagner = CalculProbabilite.calculerTempsAchatBillets(n,k,t,TEMPS_ACHAT_UN_BILLET);

        System.out.println("le temp des billet a acheter pour gagner : "+nombreDeBilletsPourGagner);
        System.out.println("le temp naicessaire pour acheter tous les billets naicessaire pour gagner : "+tempsAchatBilletsPourGagner);
        // actualiser la duree Vente
        this.duration = Math.min(duration, tempsAchatBilletsPourGagner);

//        // Planifier la fermeture de la vente
//        scheduler.schedule(this::fermerVente, duration, TimeUnit.MILLISECONDS);
    }

//    public Observable<Billet> vendreBilletAsync(Categorie categorie) {
//        return Observable.<Billet>create(emitter -> {
//            try {
//                Billet billet = gestionnaireBillets.creerBillet(Categorie.CATEGORIE_I);
//                emitter.onNext(billet); // Émettre le billet
//                emitter.onComplete(); // Compléter l'Observable
//            } catch (Exception e) {
//                emitter.onError(e); // Signaler une erreur si nécessaire
//            }
//        });
//    }

//    public synchronized Observable<Billet> vendreBillet() {
//        return Observable.create(emitter -> {
//            if (!venteOuverte) {
////                emitter.onError(new IllegalStateException("La vente de billets est fermée."));
//                emitter.onError(new VenteFermeeException( "La vente de billets est fermée."));
//            } else {
//                try {
//
//                    Billet billet = gestionnaireBillets.creerBillet();
//                    billetsVendus.add(billet);
//                    emitter.onNext(billet);
//                    emitter.onComplete();
//                } catch (Exception e) {
//                    emitter.onError(e);
//                }
//            }
//        });
//    }







    public Observable<Billet> vendreBillet() {  // methode vendre Billet synchro !
        return Observable.create(emitter -> {
            synchronized (lock) {
                if (!venteOuverte) {
                    emitter.onError(new VenteFermeeException("La vente de billets est fermée."));
                } else {
                    try {
                        sync++; // Incrément synchronisé
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

    public Observable<Billet> vendreBillet(List<Integer> numeros) {
        return Observable.create(emitter -> {
            if (!venteOuverte) {
//                emitter.onError(new IllegalStateException("La vente de billets est fermée."));
                emitter.onError(new VenteFermeeException( "La vente de billets est fermée."));
            } else {
                try {
                    sync++; // Incrément synchronisé
                    Billet billet = gestionnaireBillets.creerBillet(numeros);
                    emitter.onNext(billet);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }


//    public Observable<Billet> vendreBillet() {
//        return Observable.create(emitter -> {
//            threadPool.submit(() -> {
//                if (!venteOuverte) {
//                emitter.onError(new VenteFermeeException( "La vente de billets est fermée."));
//                threadPool.shutdown();
//                } else {
//                    try {
//
//                        Billet billet = gestionnaireBillets.creerBillet();
//                        billetsVendus.add(billet);
//                        emitter.onNext(billet);
//                        emitter.onComplete();
//                    } catch (Exception e) {
//                        emitter.onError(e);
//                    }
//                }
//            });
//        });
//    }

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

        // Fermeture de la vente de billets
         System.out.println("La vente de billets est maintenant fermée....");

//      // Effectuer le tirage des numéros gagnants
        this.tirage = new Tirage(this.n,this.k,this.t,this.billetsVendus);
        this.numerosGagnants = tirage.effectuerTirage();
//
//        // Afficher les numéros gagnants
//        System.out.println("Numéros gagnants: " + numerosGagnants);

        afficherBilletsGagnants();
        System.out.println(" sync : " + sync);
        clearCash();
    }







    // Méthode asynchrone pour vendre un billet
//    public CompletableFuture<Billet> vendreBilletAsync(int categorie) {
//        return CompletableFuture.supplyAsync(() -> {
//            Set<Integer> numeros = genererNumerosAleatoires();
//            Billet billet = gestionnaireBillets.creerBillet(numeros, categorie);
//            attendreTraitement();
//            return billet;
//        });
//    }

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
            observer.update(SN); // passer la list des numero gagnat comme argument
        }
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

}
