package org.example;

public class VenteFermeeException extends Exception {
    public VenteFermeeException() {
        super("La vente de billets est fermée.");
    }

    public VenteFermeeException(String message) {
        super(message);
    }
}
