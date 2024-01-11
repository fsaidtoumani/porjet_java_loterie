# porjet_java_loterie
Projet : un serveur multi-thread de loterie


 public void afficherBilletsGagnants() {
        System.out.println("Numéros gagnants: " + this.numerosGagnants);

        File dossier = new File("tickets");

        if (!dossier.exists() || !dossier.isDirectory()) {
            System.err.println("Le dossier de billets n'existe pas.");
            return;
        }

        File[] listeFichiers = dossier.listFiles();
        if (listeFichiers == null) {
            System.err.println("Erreur lors de la lecture du dossier.");
            return;
        }

        for (File fichier : listeFichiers) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichier))) {
                Billet billet = (Billet) in.readObject();

                if (estGagnant(billet, this.numerosGagnants)) {
                    System.out.println("Billet gagnant: " + billet);
                    // on notifie les observers (Joueurs) on passant le sn (Serial Number comme arg
                    notifyObservers(billet.getNumeroDeSerie());
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur lors de la lecture du billet: " + e.getMessage());
            }
        }
    }
