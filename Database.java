package org.example;

import java.io.*;

public class Database {

    // Méthode pour sauvegarder un objet sur le disque
    public static void sauvegarder(Object obj, String nomFichier) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            out.writeObject(obj);
        }
    }

    // Méthode pour lire un objet depuis le disque
    public static Object lire(String nomFichier) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomFichier))) {
            return in.readObject();
        }
    }
}
