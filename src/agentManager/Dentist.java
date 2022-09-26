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
                System.out.println("Operation -> ID: " + i.getId() + " TOTAL: " + i.getTotal() + "€ COMMISSION: " + i.getCommissionTot() + "€ CLIENT: " + i.getClient().getBusinessName()+" Assistant: "+i.getAgent().getName());
            }else{
                System.out.println("Operation -> ID: " + i.getId() + " TOTAL: " + i.getTotal() + "€ COMMISSION: " + i.getCommissionTot() + "€ CLIENT: " + i.getClient().getBusinessName()+" Assistant: DELETED");
            }
            i.printArticle();
            System.out.println();
            check=true;
        }
        if(!check)
            System.out.println("There are no orders.");
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
            System.out.println("There are no catalogs!.");
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
                System.out.println("Assistant -> ID: "+a.getId()+" Name: "+a.getName()+" Commission: "+a.getCommissionPercentage()+"%");
            }
        }
        if(!check)
            System.out.println("There are no agent.");
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
                System.out.println("Assistant -> ID: "+a.getId()+" Name: "+a.getName()+" Commission: "+a.getCommissionPercentage()+"%");
                a.getInventory().printInventory();
            }
        }
        if(!check)
            System.err.println("Assistant ID wrong!.");
        System.out.println("----------------------------------");
    }

    public void viewCustomerOrders(int idCustomer){

        System.out.println("----------------------------------");
        boolean check = false;
        for(Operation i : Program.getInstance().getOrders()){
            if (i.getAgent()!=null&&i.getAgent().getId()==idCustomer) {
                System.out.println("Operation -> ID: " + i.getId() + " TOTAL: " + i.getTotal() + "€ COMMISSION: " + i.getCommissionTot() + "€ CLIENT: " + i.getClient().getBusinessName());
                i.printArticle();
                check = true;
            }
        }
        if(!check)
            System.out.println("There are no orders.");
        System.out.println("----------------------------------");

    }

    public void createAgent(String name, String password, float commission, Inventory inventory, String email) {
        Program.getInstance().getUsers().add(new Assistant(name,password,commission, inventory,email));
        System.out.println("Created!");
    }

    public void createCatalog(String description, String marketZone, ArrayList<Article> articles) {
        Program.getInstance().getCatalogs().add(new Inventory(articles,description,marketZone));
        System.out.println("Created!");
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
                System.err.println("Inventory Can't be Deleted! It's Linked to an User!");
                return;
            }
        }

        for(Inventory i: Program.getInstance().getCatalogs()){
            if(i.getId()==IdCatalog){
                tmp = i;
            }
        }

        if (tmp == null){
            System.err.println("Wrong ID! Re-insert it");
            return;
        }

        Program.getInstance().getCatalogs().remove(tmp);
        System.out.println("Deleted!");
    }

    public void deleteCustomer(int idClient) {
        Customer tmp = null;

        for(Operation i: Program.getInstance().getOrders()){
            if(i.getClient().getId()==idClient){
                System.err.println("Client Can't be Deleted! It's Linked to an Operation!");
                return;
            }
        }

        for(Customer i: Program.getInstance().getCustomers()){
            if(i.getId()==idClient){
                tmp = i;
            }
        }

        if (tmp == null){
            System.err.println("Wrong ID! Re-insert it");
            return;
        }

        Program.getInstance().getCustomers().remove(tmp);
        System.out.println("Deleted!");
    }

    public void deleteProduct(int idArticle) {
        Article tmp = null;

        for(Operation i: Program.getInstance().getOrders()){
            for(Article j:i.getArticles()){
                if (j.getId()==idArticle){
                    System.err.println("This Article is Already Ordered! It can't be Deleted!");
                    return;
                }
            }
        }

        for(Article i: Program.getInstance().getArticles()){
            if(i instanceof Compound){
                for(Article j : ((Compound) i).getComponents()){
                    if (j.getId()==idArticle){
                        System.err.println("This Article is a Component Of Another Article! It can't be Deleted!");
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
            System.err.println("Wrong ID! Re-insert it");
            return;
        }

        for(Inventory i: Program.getInstance().getCatalogs())
            i.getArticles().removeIf(j -> j.getId() == idArticle); //remove j if j.getId() == article


        Program.getInstance().getArticles().remove(tmp);
        System.out.println("Deleted!");
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
            System.err.println("Id Assistant Doesn't Exist!");
            return;
        }

        for(Operation i : Program.getInstance().getOrders()){
            if (i.getAgent()== assistant){
                i.agentDeleted();
            }
        }

        Program.getInstance().getUsers().remove(assistant);
        System.out.println("Deleted!");
    }

}