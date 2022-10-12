package operationsManager;

import java.util.ArrayList;

public final class Inventory {

    private final int id;
    private final String description;
    private final String Zone;
    private static int lastID = 0;
    private final ArrayList<Article> articles;

    public Inventory(ArrayList<Article> articles, String description , String Zone) {
        this.articles = articles;
        this.description = description;
        this.Zone = Zone;
        lastID++;
        this.id=lastID;
    }

    public Inventory(ArrayList<Article> articles, String description , String Zone, int id) {
        this.articles = articles;
        this.description = description;
        this.Zone = Zone;
        this.id=id;
        lastID = Math.max(lastID, id);
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getZone() {
        return Zone;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void printInventory() {
        System.out.println("Id: "+id+" Inventario:  " + description +" Zona: " + Zone);
        for(Article i : articles){
            i.display();
        }
    }

}