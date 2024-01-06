package org.example;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JoueurIntelligent extends Joueur{
    private int n; // Nombre total de numéros possibles dans la loterie
    private int k;
    private int t;

    private final List<Billet> billetsAchetes;
    private Disposable billetSubscription;

    public JoueurIntelligent(String nom, LoterieServer serveur, int n, int k, int t) {
        super(nom,serveur);
        this.n = n;
        this.k = k;
        this.t = t;
        this.billetsAchetes =  new ArrayList<>();;
    }


    @Override
    public void run() {

        int nombreDeBilletsAcheter = CalculProbabilite.calculerNombreDeBilletsPourGagner(n, k, t);
        List<List<Integer>> numerosbilletsGagnants = CalculProbabilite.genererBilletsGagnants(n, k, nombreDeBilletsAcheter); // generer une list des nemeros

        for (List<Integer> numerosbilletsGagnant : numerosbilletsGagnants) {
            //System.out.println(numerosbilletsGagnant);
            Observable<Billet> achatObservable = getServeur().vendreBillet(numerosbilletsGagnant,this);

            Disposable disposable = achatObservable.subscribe(
                    billet -> { billetsAchetes.add(billet); },
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
        }

    }


    private void afficherBilletsAchetes() {
//        System.out.println(getName() + " a acheté les billets suivants : ");
//        billetsAchetes.forEach(billet -> System.out.println(billet));
        System.out.println(getName() + " a acheté "+billetsAchetes.size() +" les billets");
    }

}
