public class Billet {
    private String nid; //numero d'identification unique
    private Integer numero; // Numero jouer
    private String type; // Type de billet
   
    
    public Billet(Integer numero, String type) {
        try{
            this.nid = Transaction.generateUniqueId();
        }catch(Exception e){
            System.out.println(e);
        }
        this.numero = numero;
        this.type = type;
    }

    public String getNid() {
        return nid;
    }
    public Integer getNumero() {
        return numero;
    }
    public String getType() {
        return type;
    }

    /*
     * TODO: Implementer un algo de hashage pour le nid type bloc chain
     * 
     */
    
}
