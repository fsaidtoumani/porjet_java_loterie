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

        Serveur serveur = new Serveur(100, 5, 3, 100);
        serveur.start();
        serveur.acheterBillettype1(3, joueur1);
        serveur.acheterBillettype2(-5, joueur2);
        
        while (serveur.getStaut() == "En cours") {
            
        }
        serveur.acheterBillettype2(155, joueur2);
        System.out.println("Fin programme");
    }
}