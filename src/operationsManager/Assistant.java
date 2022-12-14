package operationsManager;

import org.javatuples.Pair;

import java.util.ArrayList;

public final class Assistant extends User implements Subject {

    private final Inventory inventory;
    private final float personalCost    ;
    private final ArrayList<Observer> observers;

    public Assistant(String name, String password, float personalCost, Inventory inventory, String email) {
        super(name,password,email);
        this.personalCost = personalCost;
        this.inventory = inventory;
        this.observers = new ArrayList<>();
    }

    public Assistant(String name, String passwordHash, float personalCost, Inventory inventory, String email, int id) {
        super(name,passwordHash, email,id);
        this.personalCost = personalCost;
        this.inventory = inventory;
        this.observers = new ArrayList<>();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public float getPersonalCost() {
        return personalCost;
    }

    public void createOperation(Customer c, ArrayList<Pair<Article,Integer>> articles) {
        Operation operation = new Operation(this,articles,c);
        Program.getInstance().getOperations().add(operation);
        System.out.println("Creato!");
        notify(new Operation(operation));
    }

    public boolean deleteOperation(int id) {

        Operation operationToDelete = null;

        for(Operation i : Program.getInstance().getOperations()) {
            if(i.getId() == id && i.getAssistant().getId() == this.getId()){
                operationToDelete = i;
            }
        }

        if(operationToDelete ==null) {
            System.err.println("Errore! Reinserire l'ID");
            return false;
        }

        Program.getInstance().getOperations().remove(operationToDelete);

        return true;

    }

    @Override
    public void notify(Object obj) {
        for(Observer o: observers)
            o.update(obj);
    }

    @Override
    public void viewInventory() {
        System.out.println("----------------------------------");
        inventory.printInventory();
        System.out.println("----------------------------------");
    }

    @Override
    public void viewOperations() {
        System.out.println("----------------------------------");
        boolean check = false;
        for(Operation i : Program.getInstance().getOperations()){
            if(i.getAssistant().getId() == this.getId()) {
                System.out.println("Operazione -> ID: " + i.getId() + " Costo Totale: " + i.getTotal() + "Commissione per l'operazione: " + i.getOPersonalCost() + "??? Cliente: " + i.getCustomer().getBusinessName());
                i.printArticle();
                System.out.println();
                check=true;
            }
        }
        if(!check)
            System.out.println("Non ci sono operazioni fatte da questo assistente, ?? ancora in prova.");
        System.out.println("----------------------------------");
    }

    @Override
    public void attach(Observer o) {

        observers.add(o);

    }

    @Override
    public void detach(Observer o) {

        observers.remove(o);

    }
}