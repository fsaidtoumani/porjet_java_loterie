import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;
    private AutreEventNotifieur notifieur;

    public Blockchain() {
        chain = new ArrayList<>();
        notifieur = new AutreEventNotifieur();
        // Ajouter le bloc genesis
        chain.add(new Block("Genesis Block", "0"));
    }

    public void addBlock(String data) {
        Block previousBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(data, previousBlock.hash);
        chain.add(newBlock);
        notifieur.diffuserAutreEvent(new AutreEvent(this, newBlock));
    }

    public void addEventListener(AutreEventListener listener) {
        notifieur.addAutreEventListener(listener);
    }

    public void removeEventListener(AutreEventListener listener) {
        notifieur.removeAutreEventListener(listener);
    }
}
