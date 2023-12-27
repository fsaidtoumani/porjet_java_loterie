
/*
 * Ce serveur permet de vendre des billets de loterie et de stocker les infomation dans la blockchain
 * Il est multi-thread pour permettre la vente de plusieurs billets en meme temps
 */



public class Serveur extends Thread implements AutreEventListener {
    public String staut;
    public Integer n; // Le nombre total de numéros possibles pour un billet
    public Integer k; // Le nombre de numéros que doit choisir un joueur sur son billet.
    public Integer t; // Le seuil de numéros gagnants.
    public Integer duree; //C'est la période pendant laquelle les billets peuvent être achetés avant le tirage.
    
    public Serveur( Integer n, Integer k, Integer t, Integer duree)  {
        this.staut = "En cours";    
        this.n = n;
        this.k = k;
        this.t = t;
        this.duree = duree;
    }

    public void acheterBillet(String Type, Integer quantite, Joueur joueur) {
        

    }

    public void actionADeclancher(AutreEvent evt){
    }
    
}
