package org.example;

public class Main {
    public static void main(String[] args) {
        TicketServer server = new TicketServer(10000);

        // Création et démarrage de plusieurs joueurs (threads) interagissant avec le serveur
        Player player1 = new Player(server,"Joueur1");
        Player player2 = new Player(server,"Joueur2");
        Player player3 = new Player(server,"Joueur3");

        player1.start();
        player2.start();
        player3.start();

        // Attendre la fin de la vente
        try {
            Thread.sleep(10000); // Temps de vente
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        // Tirage des numéros gagnants
//        server.drawWinningNumbers();


        try {
            player1.join();
            player2.join();
            player3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}