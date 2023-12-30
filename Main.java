import java.util.ArrayList;

/*
 * TODO: Faire la serealisation des billets et de la blockchain pour ne pas avoir de debordement de memoire
 * TODO: Faire la verification des billets
 * 
 */
public class Main {

/*     public static void main(String[] args) {
        Joueur joueur1 = new Joueur("Joueur 1", "Normal");
        Billet billet1 = new Billet( 1, "Billet 1"); 
        Billet billet2 = new Billet( 2, "Billet 2");
        joueur1.addBillet(billet1);
        joueur1.addBillet(billet2);
        try{
        System.out.println("Le billet numero 1 est: "+Block.isvalid(billet1.getNid()));
        System.out.println("Le billet numero 2 est: "+Block.isvalid(billet2.getNid()));
        System.out.println("Le billet numero 2 est: "+Block.isvalid("80555d67b8b941fb0aa3"));
        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println("Le billet numero 1 a pour nid: "+billet1.getNid());
        System.out.println("Le billet numero 1 a pour nid: "+billet2.getNid());
    } */


    public static void main(String[] args) {
        Joueur joueur1 = new Joueur("Joueur 1", "Normal");
        Joueur joueur2 = new Joueur("Joueur 2", "Normal");

        Serveur serveur = new Serveur(20, 8, 1, 10000);
        serveur.start();
        serveur.acheterBillettype1(1000, joueur1);
        ArrayList<Integer> numeros = new ArrayList<Integer>();
        numeros.add(1);
        numeros.add(2);
        numeros.add(3);
        numeros.add(65);
        numeros.add(33);
        serveur.acheterBillettype2(numeros, joueur2);
        
        serveur.acheterBillettype2(numeros, joueur2);
        System.out.println("Fin programme");
    }
}