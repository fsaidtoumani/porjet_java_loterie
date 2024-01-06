package org.example;

public class Main {
    public static void main(String[] args) {
        int n = 90;
        int k = 5;
        int t = 2;
        long duration = 10; // en seconds

        LoterieServer serveur = new LoterieServer(n,k,t,duration*1000);
        serveur.start();

        Joueur joueur1 = new Joueur("Alice", serveur);
        Joueur joueur2 = new Joueur("Bob", serveur);
        Joueur joueur3 = new Joueur("yassine", serveur);
        Joueur joueur4 = new JoueurIntelligent("saad", serveur,n,k,t);


        joueur1.start();
        joueur2.start();
        joueur3.start();
        joueur4.start();

//        try {
//            Thread.sleep(3000); // Simuler un délai pour le traitement
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new RuntimeException("Interruption pendant l'attente du traitement", e);
//        }

        try {
            serveur.join();
//            joueur2.join();
//            joueur2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("fin");

    }
}