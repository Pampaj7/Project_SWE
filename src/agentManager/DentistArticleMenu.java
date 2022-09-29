package agentManager;

import java.util.ArrayList;
import java.util.Scanner;

public final class DentistArticleMenu implements Menu{

    @Override
    public void showMenu() {

        Dentist admin = (Dentist) Program.getInstance().getActiveUser();
        Scanner in = new Scanner(System.in);

        boolean quit = false;
        int menuItem;

        do {
            admin.viewProduct();
            System.out.println("1. Aggiungi articolo");
            System.out.println("2. Cancella articolo");
            System.out.println("9. Idietro");
            System.out.println("0. Quit");
            System.out.print("Opzione scelta: ");
            try {
                menuItem = Integer.parseInt(in.next());
            }catch (Exception e){
                menuItem = -1;
            }
            switch (menuItem) {

                case 1:
                    createProductQuery(admin);
                    break;

                case 2:
                    System.out.println("Inserisci il codice del prodotto da eliminare");
                    try {
                        int idP = in.nextInt();
                        admin.deleteProduct(idP);
                    }catch (Exception e){
                        System.err.println("Id errato!");
                    }
                    break;

                case 9:
                    quit = true;
                    Program.getInstance().setMenu(new DentistMainMenu());
                    break;

                case 0:
                    quit = true;
                    Program.getInstance().close();
                    break;

                default:
                    System.err.println("Scelta non valida.");

            }

        } while (!quit);
    }

    private void createProductQuery(Dentist activeUser){

        Scanner in = new Scanner(System.in);
        boolean done = false;
        float price = 0;
        ArrayList<Article> articles = new ArrayList<>();

        System.out.println("Inserisci nome articolo:");
        String name = in.nextLine();

        int reply;
        do {
            System.out.println("Vuoi creare un oggetto composto?");
            System.out.println("1. Si");
            System.out.println("0. No");

            try {
                reply = Integer.parseInt(in.next());
            }catch (Exception e){
                reply = -1;
            }
            switch (reply) {

                case 1:
                    boolean agg;
                    while (true) {
                        agg = false;
                        activeUser.viewProduct();
                        System.out.println("Inserisci l'ID dell'articolo per la composizione o 0 per terminare la composizione");
                        try {
                            int idArticle = Integer.parseInt(in.next());

                            if (idArticle == 0) {
                                if (articles.size() > 0) {
                                    break;
                                } else {
                                    System.err.println("Seleziona almeno un articolo!");
                                    continue;
                                }
                            }

                            for (Article i : Program.getInstance().getArticles()) {
                                if (i.getId() == idArticle) {
                                    articles.add(i);
                                    price+=i.price;
                                    agg = true;
                                }
                            }

                            if (!agg) System.err.println("ID non trovato!");
                        } catch (Exception e) {
                            System.err.println("ID non valido!");
                        }
                    }
                    done = true;
                    activeUser.createProduct(name,articles);
                    break;

                case 0:
                    do {
                        System.out.println("Inserisci il prezzo :");
                        try {
                            price = Float.parseFloat(in.next());
                            break;
                        } catch (Exception e) {
                            System.err.println("Scelta non valida!");
                        }
                    } while (true);
                    done = true;
                    activeUser.createProduct(name,price);
                    break;

                default:
                    System.err.println("Scelta non valida!");
            }
        } while (!done);
    }

}
