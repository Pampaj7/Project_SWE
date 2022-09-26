package agentManager;

import java.util.Scanner;

public final class AssistantHistoricalOrderMenu implements Menu{

    @Override
    public void showMenu() {

        Assistant assistant = (Assistant) Program.getInstance().getActiveUser();
        Scanner in = new Scanner(System.in);

        boolean quit = false;
        int idOrder;
        int menuItem;
        do {
            assistant.viewOperations();
            System.out.println("Opzioni del menu:");
            System.out.println("1. Cancella un'operazione");
            System.out.println("9. Indietro");
            System.out.println("0. Esci");
            System.out.print("Scelta del menù: ");

            try {
                menuItem = Integer.parseInt(in.next());
            }catch (Exception e){
                menuItem = -1;
            }

            switch (menuItem) {
                case 1:
                    do {
                        System.out.println("Inserisci l'ID dell'operazione");
                        try {
                            idOrder = Integer.parseInt(in.next());
                        }catch (Exception e){
                            idOrder = -1;
                        }
                    }while(!assistant.deleteOrder(idOrder));
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
                    System.err.println("Invalid choice.");

            }
        } while (!quit);
    }

}
