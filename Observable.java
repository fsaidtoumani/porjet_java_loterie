package org.example;

public interface Observable {
    void addObserver(Observer observer);    // Méthode pour s'inscrire en tant qu'observateur
    void removeObserver(Observer observer); // Méthode pour se désinscrire
    void notifyObservers(String SN);                 // Méthode pour notifier tous les observateurs
}