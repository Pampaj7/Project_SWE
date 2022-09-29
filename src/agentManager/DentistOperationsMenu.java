package agentManager;

import java.util.Scanner;

public final class DentistOperationsMenu implements Menu{

    @Override
    public void showMenu() {
        Dentist admin = (Dentist) Program.getInstance().getActiveUser();
        Scanner in = new Scanner(System.in);

        boolean quit = false;

        int menuItem;

        do {
            admin.viewCustomers();
            System.out.println("1. Aggiungi cliente");
            System.out.println("2. Elimina cliente");
            System.out.println("3. Vedi operazioni cliente");
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
                    createCustomers(admin);
                    break;

                case 2:
                    System.out.println("Inserisci il codice del cliente da cancellare");
                    try {
                        int idC = in.nextInt();
                        admin.deleteCustomer(idC);
                    }catch (Exception e){
                        System.err.println("ID errato!");
                    }
                    break;

                case 3:
                    System.out.println("Inserisci il codice del cliente per il quale vedere le operazioni");
                    try {
                        int idCustomer = in.nextInt();
                        admin.viewCustomerOrders(idCustomer);
                    }catch (Exception e){
                        System.err.println("ID errato!");
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

    private void createCustomers(Dentist activeUser){
        Scanner in = new Scanner(System.in);
        System.out.println("Inserisci nome :");
        String name = in.nextLine();
        System.out.println("Inserisci Email:");
        String email = in.nextLine();
        System.out.println("Inserisci Nazionalità :");
        String country = in.nextLine();
        activeUser.createOperation(name,country,email);
    }

}
