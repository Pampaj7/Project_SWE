package operationsManager;

import org.javatuples.Pair;

import java.util.ArrayList;

public final class Operation {

    private final ArrayList<Pair<Article, Integer>> pairOp;
    private final int id;
    private static int lastID;
    private Assistant agent;
    private final float total;
    private final float commissionTot;
    private final Customer client;

    public Operation(Operation old){
        this(old.getTotal(),old.getOPersonalCost(), old.getAssistant(), old.getRows(), old.getCustomer(), old.getId());
    }

    public Operation(Assistant assistant, ArrayList<Pair<Article, Integer>> pairOp, Customer client ) {

        float tmp = 0;
        for (Pair<Article, Integer> a : pairOp)
            tmp = tmp + a.getValue0().getPrice() * a.getValue1();

        this.total = tmp;
        this.commissionTot = (assistant.getPersonalCost()*this.total)/100;
        lastID++;
        this.id = lastID;
        this.agent = assistant;
        this.pairOp = pairOp;
        this.client = client;
    }

    public Operation(float total, float commissionTot, Assistant assistant, ArrayList<Pair<Article, Integer>> pairOp, Customer client ) {
        this.total = total;
        this.commissionTot = commissionTot;
        lastID++;
        this.id = lastID;
        this.agent = assistant;
        this.pairOp = pairOp;
        this.client = client;
    }

    public Operation(float total, float commissionTot, Assistant assistant, ArrayList<Pair<Article, Integer>> pairOp, Customer client , int id ) {
        this.total = total;
        this.commissionTot = commissionTot;
        this.agent = assistant;
        this.pairOp = pairOp;
        this.id=id;
        lastID = Math.max(lastID, id);
        this.client = client;
    }

    public ArrayList<Article> getArticles() {

        ArrayList<Article> tmp = new ArrayList<>();
        for (Pair<Article, Integer> a : pairOp){
            tmp.add(a.getValue0());
        }

        return tmp;

    }

    public ArrayList<Pair<Article, Integer>> getRows() {
        return pairOp;
    }

    public Customer getCustomer() {
        return client;
    }

    public int getId() {
        return id;
    }

    public static int getLastID() {
        return lastID;
    }

    public float getTotal() {
        return total;
    }

    public Assistant getAssistant() {
        return agent;
    }

    public float getOPersonalCost() {
        return commissionTot;
    }

    public void printArticle() {

        for(Pair<Article,Integer> i: pairOp)
            System.out.println("    • Id: "+i.getValue0().getId()+" Articolo: "+i.getValue0().getName()+" Prezzo: "+i.getValue0().getPrice()+" Quantità: "+i.getValue1());

    }

    public void agentDeleted(){
        agent=null;
    }

}