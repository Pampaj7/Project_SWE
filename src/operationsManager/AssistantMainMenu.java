package operationsManager;

import java.util.Scanner;

public final class AssistantMainMenu implements Menu {

    @Override
    public void showMenu() {

        Assistant activeAssistant = (Assistant) Program.getInstance().getActiveUser();
        Scanner in = new Scanner(System.in);
        System.out.println("Ciao "+ activeAssistant.getName()+"!");

        boolean quit = false;
        int menuItem;
        do {
            System.out.println("Opzioni del menù:");
            System.out.println("1. Vedi l'inventario");
            System.out.println("2. Operazioni recenti");
            System.out.println("3. Nuova operazione per un cliente");
            System.out.println("9. Logout");
            System.out.println("0. Esci");
            System.out.print("Scelta del menù: ");

            try {
                menuItem = Integer.parseInt(in.next());
            }catch (Exception e){
                menuItem = -1;
            }

            switch (menuItem) {
                case 1:
                    activeAssistant.viewInventory();
                    break;
                case 2:
                    Program.getInstance().setMenu(new AssistantHistoricalOrderMenu());
                    quit = true;
                    break;
                case 3:
                    Program.getInstance().setMenu(new AssistantCreateOrderMenu());
                    quit = true;
                    break;
                case 9:
                    Program.getInstance().logout();
                    quit = true;
                    break;
                case 0:
                    quit = true;
                    Program.getInstance().close();
                    break;
                default:
                    System.err.println("Scelta non valida!");
            }
        } while (!quit);
    }

}
