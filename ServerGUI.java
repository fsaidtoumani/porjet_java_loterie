package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI implements Observer {
    private JFrame frame;
    private JLabel statusLabel;
    private JLabel  messageLabel;
    private LoterieServer serveur;
    private Timer timer;

    public ServerGUI(LoterieServer serveur) {
        this.serveur = serveur;
        this.serveur.addObserver(this);
        createAndShowGUI();
        startCountdown();
    }

    private void createAndShowGUI() {
        frame = new JFrame("Serveur de Loterie");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BorderLayout());
        //Status Label qui sera au top de la fenêtre
        statusLabel = new JLabel("État du serveur: "+ (serveur.isAlive() ? "en cours" : "Arrêté"), JLabel.CENTER);
        statusLabel.setSize(350, 100);
        frame.add(statusLabel, BorderLayout.NORTH);

        //Message Label qui sera au centre de la fenêtre
        messageLabel = new JLabel("Message", JLabel.CENTER);
        messageLabel.setSize(350, 100);
        frame.add(messageLabel, BorderLayout.CENTER);



        frame.setVisible(true);
    }

    private void startCountdown() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long remainingTime = serveur.getRemainingTime()/1000; // Implémentez cette méthode dans LoterieServer
                if (remainingTime > 0) {
                    messageLabel.setText("Temps restant: " + remainingTime + " secondes");
                } else {
                    messageLabel.setText("Les ventes sont fermées");
                    statusLabel.setText("État du serveur: Fini");
                    timer.stop();
                    messageLabel.setText("Le tirage est en cours...");
                }
            }
        });
        timer.start();
    }


    @Override
    public void update(String SN) {
        messageLabel.setText("Les billets gagnants sont: \n" + SN);
    }
}
