package org.example;

import io.reactivex.rxjava3.core.Single;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class TicketServer extends Thread {
    private TicketManager ticketManager;
    private AtomicBoolean isSaleOpen;

    // Un Single est un type spécial d'Observable qui émet soit un seul élément soit une erreur.

    public TicketServer(long saleDurationMillis) {
        this.ticketManager = new TicketManager();
        this.isSaleOpen = new AtomicBoolean(true);
        closeSaleAfter(saleDurationMillis);
    }

//    public Single<String> buyTicket(int numberOfTickets) {
//        return Single.create(emitter -> {
//            new Thread(() -> {
//                // Simuler le traitement de l'achat
//                try {
//                    System.out.println("Traitement de l'achat de " + numberOfTickets + " billets...");
//                    Thread.sleep(1000 * numberOfTickets); // Simuler le temps nécessaire pour traiter chaque billet
//                    emitter.onSuccess("Achat réussi de " + numberOfTickets + " billets");
//                } catch (InterruptedException e) {
//                    emitter.onError(e);
//                }
//            }).start();
//        });
//    }



//    public Single<String> buyTicket_V2(int numberOfTickets) {
//        return Single.create(emitter -> {
//            new Thread(() -> {
//                try {
//                    System.out.println("Traitement de l'achat de " + numberOfTickets + " billets...");
//                    Thread.sleep(1000 * numberOfTickets); // Temps pour traiter chaque billet
//                    emitter.onSuccess("Achat réussi de " + numberOfTickets + " billets");
//                } catch (InterruptedException e) {
//                    emitter.onError(e);
//                }
//            }).start();
//        });
//    }
//



//    public Single<Ticket> buyTicket(int numberOfTickets, String playerName) {
//        return Single.create(emitter -> {
//            new Thread(() -> {
//                try {
//                    System.out.println(playerName + " demande l'achat de " + numberOfTickets + " billets...");
//                    Thread.sleep(1000 * numberOfTickets); // Temps pour traiter chaque billet
//                    emitter.onSuccess(new Ticket());
//                } catch (InterruptedException e) {
//                    emitter.onError(e);
//                }
//            }).start();
//        });
//    }


//    public Single<Ticket> buyTicket(int numberOfNumbers,TicketCategory category) {
//        return Single.create(emitter -> {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(1000); // Simuler le traitement
//                    Ticket ticket = ticketManager.createTicket(numberOfNumbers, category);
//                    emitter.onSuccess(ticket);
//                } catch (InterruptedException e) {
//                    emitter.onError(e);
//                }
//            }).start();
//        });
//    }

    public Single<Ticket> buyTicket(int numberOfNumbers, TicketCategory category) {
        return Single.create(emitter -> {
            new Thread(() -> {
                if (!isSaleOpen.get()) {
                    emitter.onError(new IllegalStateException("La vente de billets est fermée"));
                    return;
                }

                try {
                    Thread.sleep(1000); // Simuler le traitement
                    Ticket ticket = ticketManager.createTicket(numberOfNumbers, category);
                    emitter.onSuccess(ticket);
                } catch (InterruptedException e) {
                    emitter.onError(e);
                }
            }).start();
        });
    }


    public void closeSaleAfter(long milliseconds) {
        new Thread(() -> {
            try {
                Thread.sleep(milliseconds);
                isSaleOpen.set(false);
                System.out.println("La vente de billets est maintenant fermée.");
            } catch (InterruptedException e) {
                System.err.println("Erreur lors de la fermeture de la vente: " + e.getMessage());
            }
        }).start();
    }

//    public Set<Integer> performDraw(int numberOfWinningNumbers) {
//        Set<Integer> winningNumbers = new HashSet<>();
//        while (winningNumbers.size() < numberOfWinningNumbers) {
//            int number = random.nextInt(100); // Supposons que les numéros vont de 0 à 99
//            winningNumbers.add(number);
//        }
//
//        System.out.println("Numéros gagnants: " + winningNumbers);
//        return winningNumbers;
//    }
//
//    // Méthode pour afficher les billets gagnants
//    public void displayWinningTickets(Set<Integer> winningNumbers) {
//        System.out.println("Billets gagnants:");
//        for (Ticket ticket : ticketManager.getSoldTickets()) {
//            if (ticket.getChosenNumbers().containsAll(winningNumbers)) {
//                System.out.println(ticket);
//            }
//        }
//    }


    public boolean isSaleOpen() {
        return isSaleOpen.get();
    }

    @Override
    public void run() {
        System.out.println("server is running ...");
    }

//    public void drawWinningNumbers() {
//        ticketManager.drawWinningNumbers();
//    }
//
//    public boolean isWinningTicket(Ticket ticket, int t) {
//        return ticketManager.isWinningTicket(ticket, t);
//    }

}


