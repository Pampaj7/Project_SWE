package operationsManager;

import java.util.ArrayList;
import java.util.Scanner;

public final class DentistInventoryMenu implements Menu{

    @Override
    public void showMenu() {

        Dentist admin = (Dentist) Program.getInstance().getActiveUser();
        Scanner in = new Scanner(System.in);

        boolean quit = false;

        int menuItem;

        do {
            admin.viewInventory();
            System.out.println("1. Aggiungi inventario");
            System.out.println("2. Cancella inventario");
            System.out.println("9. Indietro");
            System.out.println("0. Quit");
            System.out.print("Opzione scelta: ");
            try {
                menuItem = Integer.parseInt(in.next());
            }catch (Exception e){
                menuItem = -1;
            }
            switch (menuItem) {

                case 1:
                    createCatalog(admin);
                    break;

                case 2:
                    System.out.println("Inserisci il codice dell'inventario da eliminare");
                    int idCatalog;
                    try {
                        idCatalog = Integer.parseInt(in.next());
                    }catch (Exception e){
                        idCatalog = -1;
                    }
                    admin.deleteCatalog(idCatalog);
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

    private void createCatalog(Dentist activeUser){

        Scanner in = new Scanner(System.in);

        System.out.println("Inserisci descrizione:");
        String description = in.nextLine();
        System.out.println("Inserisci zona inventario :");
        String marketZone = in.nextLine();

        ArrayList<Article> articles = new ArrayList<>();
        boolean agg;
        while (true){
            agg = false;
            activeUser.viewProduct();
            System.out.println("Inserisci l'ID dell'articolo o 0 per terminare");
            try {
                int idArticle = Integer.parseInt(in.next());

                if (idArticle == 0) {
                    if (articles.size()>0) {
                        break;
                    }else{
                        System.err.println("Seleziona almeno un articolo!");
                        continue;
                    }
                }

                for (Article i : Program.getInstance().getArticles()) {
                    if (i.getId() == idArticle) {
                        articles.add(i);
                        agg = true;
                    }
                }
                if (!agg) System.err.println("ID Articolo non trovato!");
            }catch (Exception e){
                System.err.println("ID non valido!");
            }
        }
        activeUser.createCatalog(description,marketZone,articles);
    }

}
