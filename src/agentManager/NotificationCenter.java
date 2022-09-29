package agentManager;

import java.util.ArrayList;

public final class NotificationCenter implements Observer {

    private ArrayList<String> notification;
    private static NotificationCenter instance;

    public NotificationCenter() {
        notification = new ArrayList<>();
    }

    public ArrayList<String> getNotification() {
        return notification;
    }

    public void viewNotification() {
        System.out.println("----------------------------------");
        for (String i: notification){
            System.out.println(i);
        }
        if(notification.size()==0) System.out.println("Non ci sono notifiche!");

        System.out.println("----------------------------------");
        resetNotification();
    }

    private void resetNotification(){
        notification = new ArrayList<>();
    }

    public void addNotification(String string){
        notification.add(string);
    }

    @Override
    public void update(Object obj) {
        Operation operation = (Operation)obj;
        this.notification.add("Una nuova operazione per " + operation.getClient().getBusinessName() + " è stata fatta da " + operation.getAgent().getName());
    }

}