package org.example;

//import java.util.ArrayList;
//import java.util.List;
//
//public class Player extends Thread {
//    private final TicketServer server;
//    private final int numberOfNumbers;
//    private final TicketCategory category;
//    private List<Ticket> tickets;
//
//    public Player(TicketServer server, int numberOfNumbers, TicketCategory category, String playerName) {
//        super(playerName);
//        this.server = server;
//        this.numberOfNumbers = numberOfNumbers;
//        this.category = category;
//        this.tickets = new ArrayList<>();
//    }
//
//    @Override
//    public void run() {
//        server.buyTicket(numberOfNumbers, category).subscribe(
//                ticket -> System.out.println(getName() + " a reçu son billet: " + ticket.getChosenNumbers()),
//                error -> System.err.println("Erreur pour " + getName() + ": " + error)
//        );
//    }
//}

//import io.reactivex.rxjava3.core.SingleObserver;
//import io.reactivex.rxjava3.disposables.Disposable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Player extends Thread {
//    private final TicketServer server;
//    private final int numberOfNumbers;
//    private final TicketCategory category;
//    private List<Ticket> tickets;
//
//    public Player(TicketServer server, int numberOfNumbers, TicketCategory category, String playerName) {
//        super(playerName);
//        this.server = server;
//        this.numberOfNumbers = numberOfNumbers;
//        this.category = category;
//        this.tickets = new ArrayList<>();
//    }
//
//    @Override
//    public void run() {
//        while (!Thread.currentThread().isInterrupted()) {
//            server.buyTicket(numberOfNumbers, category).subscribe(new SingleObserver<Ticket>() {
//                @Override
//                public void onSubscribe(Disposable d) {
//                    // Gestion de l'abonnement si nécessaire
//                }
//
//                @Override
//                public void onSuccess(Ticket ticket) {
//                    System.out.println(getName() + " a acheté un billet: " + ticket);
//                    synchronized (tickets) {
//                        tickets.add(ticket);
//                    }
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    if (e instanceof IllegalStateException) {
//                        System.out.println(getName() + " : " + e.getMessage());
//                        Thread.currentThread().interrupt(); // Arrêter le thread
//                    } else {
//                        System.err.println("Erreur pour " + getName() + ": " + e.getMessage());
//                    }
//                }
//            });
//
//            try {
//                Thread.sleep(500); // Pause entre les achats
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    public List<Ticket> getTickets() {
//        synchronized (tickets) {
//            return new ArrayList<>(tickets);
//        }
//    }
//
//
//}


//import java.util.ArrayList;
//import java.util.List;
//
//public class Player extends Thread {
//    private final TicketServer server;
//    private final int numberOfNumbers;
//    private final TicketCategory category;
//    private List<Ticket> tickets;
//
//    public Player(TicketServer server, int numberOfNumbers, TicketCategory category, String playerName) {
//        super(playerName);
//        this.server = server;
//        this.numberOfNumbers = numberOfNumbers;
//        this.category = category;
//        this.tickets = new ArrayList<>();
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            server.buyTicket(numberOfNumbers, category)
//                    .subscribe(
//                            ticket -> {
//                                System.out.println(getName() + " a reçu un billet: " + ticket);
//                                tickets.add(ticket);
//                            },
//                            error -> {
//                                if (error instanceof IllegalStateException) {
//                                    System.out.println(getName() + " a été informé de la fin de la vente.");
//                                    return;
//                                }
//                                System.err.println("Erreur pour " + getName() + ": " + error);
//                            }
//                    );
//        }
//        //displayTickets();
//    }
//
//    private void displayTickets() {
//        System.out.println(getName() + " a acheté les billets suivants: " + tickets);
//    }
//}


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player extends Thread {
    private final TicketServer server;
    private final List<Ticket> tickets;
    private Random random;

    public Player(TicketServer server, String playerName) {
        super(playerName);
        this.server = server;
        this.tickets = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    public void run() {
        while (server.isSaleOpen()) {
            TicketCategory category = random.nextBoolean() ? TicketCategory.CATEGORY_I : TicketCategory.CATEGORY_II;
            int numberOfNumbers = 5; // Ou toute autre logique pour choisir ce nombre

            server.buyTicket(numberOfNumbers, category).subscribe(
                    ticket -> {
                        System.out.println(getName() + " a acheté un billet: " + ticket.getChosenNumbers());
                        tickets.add(ticket);
                    },
                    error -> System.err.println("Erreur pour " + getName() + ": " + error)
            );

            try {
                Thread.sleep(1000); // Pause entre les achats pour éviter de surcharger le serveur
            } catch (InterruptedException e) {
                System.err.println("Erreur pour " + getName() + ": " + e.getMessage());
                break;
            }
        }
        System.out.println(getName() + " a fini ses achats avec " + tickets.size() + " billets.");
    }

    // Méthode pour accéder à la liste des billets achetés par le joueur
    public List<Ticket> getTickets() {
        return tickets;
    }
}
