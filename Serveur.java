
/*
 * Ce serveur permet de vendre des billets de loterie et de stocker les infomation dans la blockchain
 * Il est multi-thread pour permettre la vente de plusieurs billets en meme temps
 */

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Serveur extends Thread implements AutreEventListener {
    public String staut;
    public Integer n; // Le nombre total de numéros possibles pour un billet
    public Integer k; // Le nombre de numéros que doit choisir un joueur sur son billet.
    public Integer t; // Le seuil de numéros gagnants.
    public Integer duree; // C'est la période pendant laquelle les billets peuvent être achetés avant le
                          // tirage.
    public Blockchain blockchain;

    private CopyOnWriteArrayList<Billet> billetsvendue; // Les billets vendus

    public Serveur(Integer n, Integer k, Integer t, Integer duree) {
        this.n = n;
        this.k = k;
        this.t = t;
        this.duree = duree;
        this.blockchain = new Blockchain();
        this.blockchain.addEventListener(new AutreEventListener() {
            @Override
            public void actionADeclancher(AutreEvent evt) {
                Block block = (Block) evt.getDonnee();
                //On updtae la blockchain dans un fichier


            }
        });
        this.billetsvendue = new CopyOnWriteArrayList<Billet>();
    }

    public Integer getN() {
        return n;
    }

    public Integer getK() {
        return k;
    }

    public Integer getT() {
        return t;
    }

    public Integer getDuree() {
        return duree;
    }

    public String getStaut() {
        return staut;
    }

    public void acheterBillettype1(Integer quantite, Joueur joueur) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                achterbillet1thread(quantite, joueur);
            }
        });
        thread.start();
    }

    public void achterbillet1thread(Integer quantite, Joueur joueur) {
        for (int i = 0; i < quantite; i++) {
            ArrayList<Integer> numerorends=new ArrayList<Integer>();
            /*
             * On genere un numero aleatoire entre 1 et n
             */
            for (int j = 0; j < k; j++) {
                numerorends.add((int) (Math.random() * n + 1));
            }
            if(staut=="En cours")
                billetsvendue.add(Transaction.vendreBillet("Billet 1", numerorends, joueur, blockchain));
        }
    }

    public void acheterBillettype2(ArrayList<Integer> numeros, Joueur joueur) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                achterbillet2thread(numeros, joueur);
            }
        });
        thread.start();
    }

    public void achterbillet2thread(ArrayList<Integer> numeros, Joueur joueur) {
        if(staut=="En cours")
            billetsvendue.add(Transaction.vendreBillet("Billet 2", numeros, joueur, blockchain));
    }

    public void run() {
        try {
            staut = "En cours";
            System.out.println("Le serveur est en cours");
            Thread.sleep(duree);
            staut = "Fermer";
            System.out.println("Le serveur est fermer");
            this.effectuerTirage();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    @Override
    public void actionADeclancher(AutreEvent evt) {
    }

    public void effectuerTirage() {
        System.out.println("Tirage en cours");
        /*
         * chois des k numero ganant
         */
        ArrayList<Integer> numerosgagnants = new ArrayList<Integer>();
        for (int i = 0; i < k; i++) {
            numerosgagnants.add((int) (Math.random() * n + 1));
        }
        /*
         * Les identifant de billets gagants sont ceux qui ont au moins t numeros gagnants
         */
        ArrayList<Billet> billetsgagnants = new ArrayList<Billet>();
        for (Billet billet : billetsvendue) {
            int compteur = 0;
            for (Integer numero : billet.getNumeros()) {
                if (numerosgagnants.contains(numero)) {
                    compteur++;
                }
            }
            if (compteur >= t) {
                billetsgagnants.add(billet);
            }
        }

        if(billetsgagnants.size()==0){
            //On choisit un billet gagnant au hasard
            int index=(int) (Math.random() * billetsvendue.size());
            numerosgagnants=billetsvendue.get(index).getNumeros();
            System.out.println("Les numeros gagnants sont: " + numerosgagnants);
            System.out.println("Le billet gagnant est: "+billetsvendue.get(index));
        }
        else{
            System.out.println("Les numeros gagnants sont: " + numerosgagnants);
            for (Billet billet : billetsgagnants) {
                System.out.println(billet.getNid() + " est un billet gagnant");
            }
        }
    }
}
