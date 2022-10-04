package agentManager;

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
        for(Operation i : Program.getInstance().getOrders()){
            if (i.getAgent()!=null) {
                System.out.println("Operation -> ID: " + i.getId() + " TOTALE: " + i.getTotal() + "€ COSTO PERSONALE: " + i.getCommissionTot() + "€ CLIENTE: " + i.getClient().getBusinessName()+" Assistente: "+i.getAgent().getName());
            }else{
                System.out.println("Operation -> ID: " + i.getId() + " TOTALE: " + i.getTotal() + "€ COSTO PERSONALE: " + i.getCommissionTot() + "€ CLIENTE: " + i.getClient().getBusinessName()+" Assistente: CANCELLATO");
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
        if (Program.getInstance().getCatalogs().size()>0){
            System.out.println();
            for (Inventory i : Program.getInstance().getCatalogs()) {
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

    public void viewAgent() {
        System.out.println("----------------------------------");
        Assistant a;
        boolean check = false;
        for (User u : Program.getInstance().getUsers()){
            if(u instanceof Assistant){
                check = true;
                a = (Assistant)u;
                System.out.println("Assistente -> ID: "+a.getId()+" Nome: "+a.getName()+" Costo personale: "+a.getCommissionPercentage()+"%");
            }
        }
        if(!check)
            System.out.println("Non ci sono assistenti.");
        System.out.println("----------------------------------");
    }

    public void viewNotification() {
        Program.getInstance().getNotificationCenter().viewNotification();
    }

    public void viewCatalogAgent(int idAgent){
        System.out.println("----------------------------------");
        Assistant a;
        boolean check = false;
        for (User u : Program.getInstance().getUsers()){
            if((u instanceof Assistant) && u.getId() == idAgent){
                check = true;
                a = (Assistant)u;
                System.out.println("Assistente -> ID: "+a.getId()+" Nome: "+a.getName()+" Costo personale: "+a.getCommissionPercentage()+"%");
                a.getInventory().printInventory();
            }
        }
        if(!check)
            System.err.println("Id assistente errato!.");
        System.out.println("----------------------------------");
    }

    public void viewCustomerOrders(int idCustomer){

        System.out.println("----------------------------------");
        boolean check = false;
        for(Operation i : Program.getInstance().getOrders()){
            if (i.getAgent()!=null&&i.getAgent().getId()==idCustomer) {
                System.out.println("Operazione -> ID: " + i.getId() + " TOTALE: " + i.getTotal() + "€ COSTO PERSONALE: " + i.getCommissionTot() + "€ CLIENTE: " + i.getClient().getBusinessName());
                i.printArticle();
                check = true;
            }
        }
        if(!check)
            System.out.println("Non ci sono ordini!.");
        System.out.println("----------------------------------");

    }

    public void createAgent(String name, String password, float commission, Inventory inventory, String email) {
        Program.getInstance().getUsers().add(new Assistant(name,password,commission, inventory,email));
        System.out.println("Creato!");
    }

    public void createCatalog(String description, String marketZone, ArrayList<Article> articles) {
        Program.getInstance().getCatalogs().add(new Inventory(articles,description,marketZone));
        System.out.println("Creato!");
    }

    public void createProduct(String name, ArrayList<Article> a) {
        Program.getInstance().getArticles().add(new Compound(name, a));
    }

    public void createProduct(String name, float price) {
        Program.getInstance().getArticles().add(new Product(name, price));
    }

    public void deleteCatalog(int IdCatalog){
        Inventory tmp = null;

        for(User i: Program.getInstance().getUsers()){
            if(i instanceof Assistant && ((Assistant) i).getInventory().getId()==IdCatalog){
                System.err.println("L'inventario non può essere cancellato! E' legato ad un' assistente!");
                return;
            }
        }

        for(Inventory i: Program.getInstance().getCatalogs()){
            if(i.getId()==IdCatalog){
                tmp = i;
            }
        }

        if (tmp == null){
            System.err.println("ID sbagliato! Riprovare");
            return;
        }

        Program.getInstance().getCatalogs().remove(tmp);
        System.out.println("Cancellato!");
    }

    public void deleteCustomer(int idClient) {
        Customer tmp = null;

        for(Operation i: Program.getInstance().getOrders()){
            if(i.getClient().getId()==idClient){
                System.err.println("Il cliente non può essere cancellato! E' legato ad un operazione!");
                return;
            }
        }

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

        /*for(Operation i: Program.getInstance().getOrders()){
            for(Article j:i.getArticles()){
                if (j.getId()==idArticle){
                    System.err.println("Questo articolo è necessario per un operazione! Non può essere attualmente cancellato!");
                    return;
                }
            }
        }*/

        for(Article i: Program.getInstance().getArticles()){
            if(i instanceof Compound){
                for(Article j : ((Compound) i).getComponents()){
                    if (j.getId()==idArticle){
                        System.err.println("Questo articolo è necessario per una o piu operazioni! Devi prima cancellare le operazioni associate!");
                        return;
                    }
                }
            }
        }

        for(Article i: Program.getInstance().getArticles()){
            if(i.getId()==idArticle){
                tmp = i;
            }
        }

        if (tmp == null){
            System.err.println("ID sbagliato! Riprovare");
            return;
        }

        for(Inventory i: Program.getInstance().getCatalogs())
            i.getArticles().removeIf(j -> j.getId() == idArticle);


        Program.getInstance().getArticles().remove(tmp);
        System.out.println("Cancellato!");
    }

    public void deleteAgent(int idAgent){
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

        for(Operation i : Program.getInstance().getOrders()){
            if (i.getAgent()== assistant){
                i.agentDeleted();
            }
        }

        Program.getInstance().getUsers().remove(assistant);
        System.out.println("Cancellato!");
    }

}