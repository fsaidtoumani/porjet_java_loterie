package org.example;

import java.util.List;

public class Ticket {

    private List<Integer> chosenNumbers;
    private String serialNumber;
    private TicketCategory category;


    public Ticket(List<Integer> chosenNumbers, String serialNumber, TicketCategory category) {
        this.chosenNumbers = chosenNumbers;
        this.serialNumber = serialNumber;
        this.category = category;
    }


    public TicketCategory getCategory() {
        return category;
    }

    public List<Integer> getChosenNumbers() {
        return chosenNumbers;
    }


    public String getSerialNumber() {
        return serialNumber;
    }


}


