import java.util.ArrayList;

public class Joueur {
    private String nom;
    private ArrayList<Billet> billets;
    private String type;

    public Joueur(String nom, ArrayList<Billet> billets, String type) {
        this.nom = nom;
        this.billets = billets;
        this.type = type;
    }
        public Joueur(String nom, String type) {
        this.nom = nom;
        this.billets = new ArrayList<Billet>();
        this.type = type;
    }
    
    public String getNom() {
        return nom;
    }
    public ArrayList<Billet> getBillets() {
        return billets;
    }
    public String getType() {
        return type;
    }

    public void addBillet(Billet billet) {
     this.billets.add(billet);
    }
    public String toString() {
        return "Joueur{" +
                "nom='" + nom + '}';
    }

    public void reflexion(Serveur serveur){
        /*
         * Un joueur inteligent va achter des billets de type 1 et 2
         * pour augmenter ses chances de gagner grace au billet de type 2 où il peut choisir le numero
         * 
         */
    }
}
