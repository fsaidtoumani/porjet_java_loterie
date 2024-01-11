package org.example;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class Joueur extends Thread {
    private final LoterieServer serveur;
    private final List<Billet> billetsAchetes;
    private final CompositeDisposable disposables; // Pour gérer plusieurs abonnements

    public Joueur(String nom, LoterieServer serveur) {
        super(nom); // Le nom du thread est le nom du joueur
        this.serveur = serveur;
        this.billetsAchetes = new ArrayList<>();
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            Observable<Billet> achatObservable = serveur.vendreBillet(this);
            Disposable disposable = achatObservable.subscribeOn(Schedulers.io()).subscribe(
                    billet -> {
                        billetsAchetes.add(billet);
                        //System.out.println(getName() + " a acheté un billet: " + billet);
                    },
                    erreur -> {
                        if (erreur instanceof VenteFermeeException) {
                            System.out.println(getName() + " : La vente de billets est fermée.");
                            afficherBilletsAchetes();
                            interrupt();
                        } else {
                            System.err.println(getName() + " : Erreur lors de l'achat du billet : " + erreur.getMessage());
                        }
                    }
            );
//            disposables.add(disposable); // Ajouter à la liste des Disposable

            try {
                Thread.sleep(10); // Délai entre les tentatives d'achat
            } catch (InterruptedException e) {
                interrupt();
            }
        }
        //disposables.dispose(); // Nettoyer tous les Disposable lors de l'interruption
    }

    private void afficherBilletsAchetes() {
//        System.out.println(getName() + " a acheté les billets suivants : ");
//        billetsAchetes.forEach(billet -> System.out.println(billet));
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
}
