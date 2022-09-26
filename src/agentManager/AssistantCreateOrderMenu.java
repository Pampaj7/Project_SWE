package agentManager;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Scanner;

public final class AssistantCreateOrderMenu implements Menu{

    @Override
    public void showMenu() {
        Assistant assistant = (Assistant)Program.getInstance().getActiveUser();
        Scanner in = new Scanner(System.in);
        boolean quit = false;
        boolean checkOp;
        int menuItem;
        int idS;
        do {

            assistant.viewCustomers();
            System.out.println("Opzioni menù dell'assistente");
            System.out.println("1. Crea un nuovo cliente");
            System.out.println("2. Selezione un cliente già esistente da operare");
            System.out.println("9. indietro");
            System.out.println("0. Esci");
            System.out.print("Opzione scelta: ");
            System.out.println("");

            try {
                menuItem = Integer.parseInt(in.next());
            }catch (Exception e){
                menuItem = -1;
            }

            switch (menuItem) {

                case 1:
                    createOperation(assistant);
                    break;

                case 2:
                    do{
                        System.out.println("Inserisci un id:");
                        try {
                            idS = Integer.parseInt(in.next());
                        }catch (Exception e){
                            idS = -1;
                        }

                        checkOp = false;
                        for(Customer c :Program.getInstance().getCustomers()){
                            if (c.getId() == idS) {
                                checkOp = true;
                                break;
                            }
                        }
                        if(!checkOp)
                            System.err.println("ID sbagliato! Ritenta");

                    }while( !checkOp );

                    subMenuSelectArticles(assistant, idS );
                    break;

                case 9:
                    Program.getInstance().setMenu(new AssistantMainMenu());
                    quit = true;
                    break;

                case 0:
                    quit = true;
                    Program.getInstance().close();
                    break;

                default:
                    System.err.println("Scelta non valida");
            }
        } while (!quit);
    }

    private void createOperation(Assistant activeUser){
        Scanner in = new Scanner(System.in);
        System.out.println("Inserisci Email:");
        String email = in.nextLine();
        System.out.println("Inserisci città :");
        String country = in.nextLine();
        System.out.println("Inserisci nome :");
        String name = in.nextLine();
        activeUser.createOperation(name,country,email);
    }

    private void subMenuSelectArticles(Assistant assistant, int idSelectedOperation){

        Scanner in = new Scanner(System.in);
        ArrayList<Pair<Article,Integer>> articlesPair = new ArrayList<>();
        Inventory c = assistant.getInventory();

        boolean agg;
        int qtaArticle;
        while (true){
            agg = false;
            assistant.viewInventory();
            System.out.println("Inserisci un ID di un articolo da assegnare per la sua operazione o 0 per terminare l'operazione");
            try {
                int idArticle = Integer.parseInt(in.next());
                if (idArticle == 0) {
                    if (articlesPair.size() > 0)
                        break;
                    else {
                        System.err.println("Selezione almeno un articolo!");
                        continue;
                    }
                }
                for (Article i : c.getArticles()) {
                    if (i.getId() == idArticle) {

                        do {
                            System.out.println("Inserisci la quantità di articoli (>0)");
                            try {
                                qtaArticle = Math.abs(Integer.parseInt(in.next()));
                            } catch (Exception e) {
                                System.err.println("Valore non valido");
                                qtaArticle = -1;
                            }
                            if(qtaArticle==0){
                                System.err.println("Valore non valido");
                                qtaArticle = -1;
                            }
                        }while(qtaArticle==-1);

                        articlesPair.add(new Pair<>(i,qtaArticle));
                        agg = true;
                    }
                }
                if (!agg) System.err.println("ID non trovato!");
            }catch (Exception e){
                System.err.println("Id non valido!");
            }
        }

        for(Customer i : Program.getInstance().getCustomers()){
            if(i.getId() == idSelectedOperation) {
                assistant.createOrder(i, articlesPair);
                return;
            }
        }

    }

}
