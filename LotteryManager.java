package org.example;

import java.util.List;

public class LotteryManager {

    private int n; // Nombre total de numéros disponibles
    private int k; // Nombre de numéros à choisir pour un billet
    private int t; // Borne pour les billets gagnants
    private int lotteryDuration; // Durée de la loterie en secondes
    private TicketManager ticketManager;

    // Constructeur
    public LotteryManager(TicketManager ticketManager,int n, int k, int t, int lotteryDuration) {
        this.n = n;
        this.k = k;
        this.t = t;
        this.lotteryDuration = lotteryDuration;
        this.ticketManager = ticketManager;
    }

    // Méthode pour initialiser la loterie
    public void initializeLottery() {
        // Logique d'initialisation de la loterie (à implémenter)
    }

    // Méthode pour effectuer le tirage des numéros gagnants
    public List<Integer> drawWinningNumbers() {
        // Logique pour tirer les numéros gagnants (à implémenter)
        return null; // À adapter
    }

    // Méthode pour fermer la vente des billets
    public void closeTicketSale() {
        // Logique pour fermer la vente des billets (à implémenter)
    }

//    public boolean isWinningTicket(Ticket ticket, int t) {
//        Set<Integer> chosenNumbers = new HashSet<>(ticket.getChosenNumbers());
//        chosenNumbers.retainAll(winningNumbers);
//        return chosenNumbers.size() >= t;
//    }

}
