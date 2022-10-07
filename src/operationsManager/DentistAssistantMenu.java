package operationsManager;

import java.util.Scanner;

public final class DentistAssistantMenu implements Menu{

    @Override
    public void showMenu() {

        Dentist admin = (Dentist) Program.getInstance().getActiveUser();
        Scanner in = new Scanner(System.in);

        boolean quit = false;
        int menuItem;

        do {
            admin.viewAgent();
            System.out.println("1. Aggiungi assistente");
            System.out.println("2. Cancella assistente");
            System.out.println("3. Vedi inventario assistente");
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
                    createAgent(admin);
                    break;

                case 2:
                    System.out.println("Inserisci il codice dell'assistente da eliminare");
                    try {
                        int idA = in.nextInt();
                        admin.deleteAssistant(idA);
                    }catch (Exception e){
                        System.err.println("Invalid Id!");
                    }
                    break;

                case 3:
                    System.out.println("Inserisci il codice dell'agente per il quale vedere l'inventario");
                    try {
                        int idAgent = in.nextInt();
                        admin.viewCatalogAgent(idAgent);
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
                    System.err.println("Scelta non valida!");

            }

        } while (!quit);
    }

    private void createAgent(Dentist activeUser){

        Scanner in = new Scanner(System.in);

        System.out.println("Inserisci nome:");
        String name = in.nextLine();
        System.out.println("Inserisci Password :");
        String password = in.nextLine();
        System.out.println("Inserisci email :");
        String email = in.nextLine();
        float percentage ;
        do{
            System.out.println("Inserisci percentuale del costo personale :"); //TOD
            try {
                percentage = Math.abs(Float.parseFloat(in.next()));
            }catch (Exception e){
                System.err.println("Devi inserire un numero!");
                percentage = -1;
            }
        }while (percentage==-1);

        Inventory inventory = null;

        int idCatalog;
        do{
            activeUser.viewInventory();
            System.out.println("Inserisci ID inventario :");
            try {
                idCatalog = Integer.parseInt(in.next());

                for(Inventory i: Program.getInstance().getCatalogs()){
                    if (i.getId()==idCatalog){
                        inventory =i;
                    }
                }

            }catch (Exception ignored){}

            if (inventory ==null) System.err.println("Devi inserire un numero!");

        }while (inventory ==null);
        activeUser.createAgent(name,password,percentage, inventory,email);
    }

}
