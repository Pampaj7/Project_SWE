package operationsManager;

public final class Product extends Article {

    public Product(String name, float price) {
        super(name, price);
    }

    public Product(String name, float price, int id) {
        super(name, price, id);
    }

    public void display() {
        System.out.println("--Id: "+this.id+" Articolo: " + name + " Prezzo: " + price);
    }

}