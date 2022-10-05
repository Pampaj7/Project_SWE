package operationsManager;

import java.util.Scanner;

public final class DentistMainMenu implements Menu{

    @Override
    public void showMenu() {

        Dentist admin = (Dentist) Program.getInstance().getActiveUser();
        Scanner in = new Scanner(System.in);

        System.out.println("Ciao "+admin.getName()+"!");

        admin.viewNotification();

        boolean quit = false;
        int menuItem;

        do {
            System.out.println("Opzioni:");

            System.out.println("1. Vedi Assistenti");
            System.out.println("2. Vedi inventari");
            System.out.println("3. Vedi clienti");
            System.out.println("4. Vedi storico operazioni");
            System.out.println("5. Vedi articoli");
            System.out.println("9. Logout");
            System.out.println("0. Quit");
            System.out.print("Opzione scelta: ");

            try {
                menuItem = Integer.parseInt(in.next());
            }catch (Exception e){
                menuItem = -1;
            }
            switch (menuItem) {

                case 1:
                    Program.getInstance().setMenu(new DentistAssistantMenu());
                    quit = true;
                    break;

                case 2:
                    Program.getInstance().setMenu(new DentistInventoryMenu());
                    quit = true;
                    break;

                case 3:
                    Program.getInstance().setMenu(new DentistOperationsMenu());
                    quit = true;
                    break;

                case 4:
                    admin.viewOperations();
                    break;

                case 5:
                    Program.getInstance().setMenu(new DentistArticleMenu());
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
                    System.err.println("Scelta non valida.");
            }
        } while (!quit);
    }

}
