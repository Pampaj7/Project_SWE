package operationsManager;

import java.util.ArrayList;

public final class Dentist extends User {

    public Dentist(String name, String password, String email) {
        super(name, password,email);
    }
    public Dentist(String name, String passwordHash, String email , int id) {
        super(name, passwordHash, email,id);
    }

    @Override
    public void viewOperations() {
        System.out.println("----------------------------------");
        boolean check = false;
        for(Operation i : Program.getInstance().getOperations()){
            if (i.getAssistant()!=null) {
                System.out.println("Operation -> ID: " + i.getId() + " TOTALE: " + i.getTotal() + "€ COSTO PERSONALE: " + i.getOPersonalCost() + "€ CLIENTE: " + i.getCustomer().getBusinessName()+" Assistente: "+i.getAssistant().getName());
            }else{
                System.out.println("Operation -> ID: " + i.getId() + " TOTALE: " + i.getTotal() + "€ COSTO PERSONALE: " + i.getOPersonalCost() + "€ CLIENTE: " + i.getCustomer().getBusinessName()+" Assistente: CANCELLATO");
            }
            i.printArticle();
            System.out.println();
            check=true;
        }
        if(!check)
            System.out.println("Non ci sono nuovi ordini.");
        System.out.println("----------------------------------");
    }

    @Override
    public void viewInventory() {
        System.out.println("----------------------------------");
        if (Program.getInstance().getInventories().size()>0){
            System.out.println();
            for (Inventory i : Program.getInstance().getInventories()) {
                i.printInventory();
                System.out.println();
            }
        }else
            System.out.println("Non c'è un inventario!.");
        System.out.println("----------------------------------");
    }

    public void viewProduct() {
        System.out.println("----------------------------------");
        for(Article i : Program.getInstance().getArticles()){
            i.display();
        }
        System.out.println("----------------------------------");
    }

    public void viewAssistant() {
        System.out.println("----------------------------------");
        Assistant a;
        boolean check = false;
        for (User u : Program.getInstance().getUsers()){
            if(u instanceof Assistant){
                check = true;
                a = (Assistant)u;
                System.out.println("Assistente -> ID: "+a.getId()+" Nome: "+a.getName()+" Costo personale: "+a.getPersonalCost()+"%");
            }
        }
        if(!check)
            System.out.println("Non ci sono assistenti.");
        System.out.println("----------------------------------");
    }

    public void viewNotification() {
        Program.getInstance().getNotificationCenter().viewNotification();
    }

    public void viewInventoryAssistant(int idAssistant){
        System.out.println("----------------------------------");
        Assistant a;
        boolean check = false;
        for (User u : Program.getInstance().getUsers()){
            if((u instanceof Assistant) && u.getId() == idAssistant){
                check = true;
                a = (Assistant)u;
                System.out.println("Assistente -> ID: "+a.getId()+" Nome: "+a.getName()+" Costo personale: "+a.getPersonalCost()+"%");
                a.getInventory().printInventory();
            }
        }
        if(!check)
            System.err.println("Id assistente errato!.");
        System.out.println("----------------------------------");
    }

    public void viewCustomerOperations(int idCustomer){

        System.out.println("----------------------------------");
        boolean check = false;
        for(Operation i : Program.getInstance().getOperations()){
            if (i.getAssistant()!=null&&i.getAssistant().getId()==idCustomer) {
                System.out.println("Operazione -> ID: " + i.getId() + " TOTALE: " + i.getTotal() + "€ COSTO PERSONALE: " + i.getOPersonalCost() + "€ CLIENTE: " + i.getCustomer().getBusinessName());
                i.printArticle();
                check = true;
            }
        }
        if(!check)
            System.out.println("Non ci sono ordini!.");
        System.out.println("----------------------------------");

    }

    public void createAssistant(String name, String password, float commission, Inventory inventory, String email) {
        Program.getInstance().getUsers().add(new Assistant(name,password,commission, inventory,email));
        System.out.println("Creato!");
    }

    public void createInventory(String description, String marketZone, ArrayList<Article> articles) {
        Program.getInstance().getInventories().add(new Inventory(articles,description,marketZone));
        System.out.println("Creato!");
    }

    public void createProduct(String name, ArrayList<Article> a) {
        Program.getInstance().getArticles().add(new Compound(name, a));
    }

    public void createProduct(String name, float price) {
        Program.getInstance().getArticles().add(new Product(name, price));
    }

    public void deleteInventory(int IdCatalog){
        Inventory tmp = null;



        for(Inventory i: Program.getInstance().getInventories()){
            if(i.getId()==IdCatalog){
                tmp = i;
            }
        }

        if (tmp == null){
            System.err.println("ID sbagliato! Riprovare");
            return;
        }

        Program.getInstance().getInventories().remove(tmp);
        System.out.println("Cancellato!");
    }

    public void deleteCustomer(int idClient) {
        Customer tmp = null;



        for(Customer i: Program.getInstance().getCustomers()){
            if(i.getId()==idClient){
                tmp = i;
            }
        }

        if (tmp == null){
            System.err.println("ID sbagliato! Riprovare");
            return;
        }

        Program.getInstance().getCustomers().remove(tmp);
        System.out.println("Cancellato!");
    }

    public void deleteProduct(int idArticle) {
        Article tmp = null;


        for(Article i: Program.getInstance().getArticles()){
            if(i.getId()==idArticle){
                tmp = i;
            }
        }

        if (tmp == null){
            System.err.println("ID sbagliato! Riprovare");
            return;
        }

        for(Inventory i: Program.getInstance().getInventories())
            i.getArticles().removeIf(j -> j.getId() == idArticle);


        Program.getInstance().getArticles().remove(tmp);
        System.out.println("Cancellato!");
    }

    public void deleteAssistant(int idAgent){
        Assistant assistant =null;
        for(User i : Program.getInstance().getUsers()){
            if (i instanceof Assistant && i.getId()==idAgent){
                assistant = (Assistant) i;
                break;
            }
        }

        if (assistant ==null){
            System.err.println("L'ID dell'assistente non esiste!");
            return;
        }

        for(Operation i : Program.getInstance().getOperations()){
            if (i.getAssistant()== assistant){
                i.agentDeleted();
            }
        }

        Program.getInstance().getUsers().remove(assistant);
        System.out.println("Cancellato!");
    }

}