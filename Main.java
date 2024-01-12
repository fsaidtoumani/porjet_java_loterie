package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int n = 90;
        int k = 5;
        int t = 2;
        long duration = 10; // en seconds

        LoterieServer serveur = new LoterieServer(n,k,t,duration*1000);
        serveur.start();

        //SwingUtilities.invokeLater(() -> new ServerGUI(serveur));

        Joueur joueur1 = new Joueur("Alice", serveur,"Bete");
        Joueur joueur2 = new Joueur("Bob", serveur,"Bete");
        Joueur joueur3 = new Joueur("yassine", serveur,"IntelliJ");
        Joueur joueur4 = new Joueur("saad", serveur,"IntelliJ");


        joueur1.start();
        joueur2.start();
        joueur3.start();
        joueur4.start();

        try {
            serveur.join();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("fin");

    }
}