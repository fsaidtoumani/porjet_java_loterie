package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TicketManager {
    private List<Ticket> soldTickets;
    private Random random;

    public TicketManager() {
        this.soldTickets = new ArrayList<>();
        this.random = new Random();
    }

    public Ticket createTicket(int numberOfNumbers, TicketCategory category) {
        List<Integer> chosenNumbers = IntStream.range(0, numberOfNumbers)
                .map(i -> random.nextInt(100))
                .boxed()
                .collect(Collectors.toList());
        String serialNumber = "SN" + random.nextInt(1000);
        Ticket ticket = new Ticket(chosenNumbers, serialNumber, category);
        soldTickets.add(ticket);
        return ticket;
    }


    public List<Ticket> getSoldTickets() {
        return soldTickets;
    }


    private List<Integer> generateRandomNumbers() {
        List<Integer> randomNumbers = new ArrayList<>();
        Random random = new Random();
        // Exemple : génération de 5 numéros entre 1 et 90
        for (int i = 0; i < 5; i++) {
            int randomNumber = random.nextInt(90) + 1;
            randomNumbers.add(randomNumber);
        }
        return randomNumbers;
    }


    private String generateSerialNumber() {
        // methode pour generer un SN unique
        return null ;
    }

//    public TicketManager(int n, int k) {
//        this.n = n;
//        this.k = k;
//        this.winningNumbers = new HashSet<>();
//    }
//
//    public void drawWinningNumbers() {
//        winningNumbers = new Random().ints(1, n + 1)
//                .distinct()
//                .limit(k)
//                .boxed()
//                .collect(Collectors.toSet());
//        System.out.println("Numéros gagnants: " + winningNumbers);
//    }
//
//    public boolean isWinningTicket(Ticket ticket, int t) {
//        Set<Integer> chosenNumbers = new HashSet<>(ticket.getChosenNumbers());
//        chosenNumbers.retainAll(winningNumbers);
//        return chosenNumbers.size() >= t;
//    }




}

