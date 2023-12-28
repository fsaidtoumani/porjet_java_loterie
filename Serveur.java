
/*
 * Ce serveur permet de vendre des billets de loterie et de stocker les infomation dans la blockchain
 * Il est multi-thread pour permettre la vente de plusieurs billets en meme temps
 */

public class Serveur extends Thread implements AutreEventListener {
    public String staut;
    public Integer n; // Le nombre total de numéros possibles pour un billet
    public Integer k; // Le nombre de numéros que doit choisir un joueur sur son billet.
    public Integer t; // Le seuil de numéros gagnants.
    public Integer duree; // C'est la période pendant laquelle les billets peuvent être achetés avant le
                          // tirage.
    public Blockchain blockchain;

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
                System.out.println("Nouveau bloc ajouté : " + block.data);
            }
        });
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
            Integer numerorend;
            /*
             * On genere un numero aleatoire entre 1 et n
             */
            numerorend = (int) (Math.random() * (n - 1)) + 1;
            if(staut=="En cours")
                Transaction.vendreBillet("Billet 1", numerorend, joueur, blockchain);
        }
    }

    public void acheterBillettype2(Integer numero, Joueur joueur) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                achterbillet2thread(numero, joueur);
            }
        });
        thread.start();
    }

    public void achterbillet2thread(Integer numero, Joueur joueur) {
        if(staut=="En cours")
        Transaction.vendreBillet("Billet 2", numero, joueur, blockchain);
    }

    public void run() {
        try {
            staut = "En cours";
            System.out.println("Le serveur est en cours");
            Thread.sleep(duree);
            staut = "Fermer";
            System.out.println("Le serveur est fermer");
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    @Override
    public void actionADeclancher(AutreEvent evt) {
    }

}
