//package org.example;
//
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.disposables.Disposable;
//import io.reactivex.rxjava3.schedulers.Schedulers;
//
//import javax.swing.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.CompletableFuture;
//
//public class Joueur_old extends Thread{
//    private final LoterieServer serveur;
//    private final List<Billet> billetsAchetes;
//    private Disposable billetSubscription;
//
//
//    public Joueur_old(String nom, LoterieServer serveur) {
//        super(nom); // Le nom du thread est le nom du joueur
//        this.serveur = serveur;
//        this.billetsAchetes = new ArrayList<>();
//        // Initialiser les autres attributs
//    }
//
////    @Override
////    public void run() {
////
////
//////        // Exemple d'achat asynchrone d'un billet
//////        int categorie = 1; // Catégorie pour l'exemple
//////
//////        CompletableFuture<Billet> achatFutur = serveur.vendreBilletAsync(categorie);
//////        achatFutur.thenAccept(billet -> {
//////            System.out.println("Billet acheté : " + billet);
//////            // Traitement supplémentaire si nécessaire
//////        }).exceptionally(e -> {
//////            System.err.println("Erreur lors de l'achat du billet : " + e.getMessage());
//////            return null;
//////        });
////
////        System.out.println("joueur " + getName());
////        this.billetSubscription = serveur.vendreBillet(Categorie.CATEGORIE_I)
////                .subscribe(
////                        billet -> {
////                            System.out.println("Billet acheté : " + billet);
////                            billetsAchetes.add(billet);
////                            },
////                        erreur -> System.err.println("Erreur lors de l'achat du billet : " + erreur.getMessage())
////                );
////
////    }
//
//
//    @Override
//    public void run() {
//
//
//
//        while (!isInterrupted()) {
//            Observable<Billet> achatObservable = serveur.vendreBillet();
//
////            Disposable disposable = achatObservable.subscribeOn(Schedulers.io()).subscribe(
//            Disposable disposable = achatObservable.subscribeOn(Schedulers.io()).subscribe(
//                    billet -> billetsAchetes.add(billet),
//                    erreur -> {
//                        if (erreur instanceof VenteFermeeException) {
//                            System.out.println(getName() + " : La vente de billets est fermée.");
//                            afficherBilletsAchetes();
//                            interrupt();
//                        } else {
//                            System.err.println(getName() + " : Erreur lors de l'achat du billet : " + erreur.getMessage());
//                        }
//                    }
//            );
//
////            try {
////                Thread.sleep(1000); // Délai entre les tentatives d'achat
////            } catch (InterruptedException e) {
////                interrupt();
////            }
//        }
//    }
//
//    private void afficherBilletsAchetes() {
////        System.out.println(getName() + " a acheté les billets suivants : ");
////        for (Billet billet : billetsAchetes) {
////            System.out.println(billet);
////        }
//
//        System.out.println(getName() + " a acheté "+billetsAchetes.size() +" les billets");
//    }
//
//
//    public List<Billet> getBilletsAchetes() {
//        return new ArrayList<>(billetsAchetes);
//    }
//
////    // Méthode pour acheter un billet
////    public void acheterBillet(Set<Integer> numeros, int categorie, LoterieServer serveur) {
////        Billet billet = serveur.vendreBillet(numeros, categorie);
////        billetsAchetes.add(billet);
////    }
////
////    // Méthode pour vérifier les billets gagnants
////    public void verifierBilletsGagnants(Set<Integer> numerosGagnants) {
////        for (Billet billet : billetsAchetes) {
////            if (billet.estGagnant(numerosGagnants)) {
////                System.out.println("Billet gagnant: " + billet);
////            }
////        }
////    }
//
//    @Override
//    public String toString() {
//        return "Joueur{" +
//                "nom='" + getName() + '\'' +
//                ", billetsAchetes=" + billetsAchetes +
//                '}';
//    }
//
//}
