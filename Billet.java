import java.util.ArrayList;

public class Billet {
    private String nid; //numero d'identification unique
    private ArrayList<Integer> numeros; // numeros du billet
    private String type; // Type de billet
   
    
    public Billet(ArrayList<Integer> numeros, String type) {
        try{
            this.nid = Transaction.generateUniqueId();
        }catch(Exception e){
            System.out.println(e);
        }
        this.numeros = numeros;
        this.type = type;
    }

    public String getNid() {
        return nid;
    }
    public ArrayList<Integer> getNumeros() {
        return numeros;
    }
    public String getType() {
        return type;
    }
    public String toString() {
        return "Billet{" +
                "nid='" + nid + '\'' +
                ", numeros=" + numeros +
                '}';
    }
}
