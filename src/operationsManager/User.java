package operationsManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class User {

    private final int id;
    private final String name;
    private final String password;
    private static int lastID;
    private final String email;

    public User(String name, String password,String email) {
        this.password =getHash(password);
        lastID++;
        this.id = lastID;
        this.name = name;
        this.email = email;
    }

    public User(String name, String password, String email, int id) {
        this.password = password;
        this.id=id;
        lastID = Math.max(lastID, id);
        this.name = name;
        this.email = email;
    }

    public static String getHash(String password){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public static int getLastID() {
        return lastID;
    }

    public void createCustomer(String businessName, String country, String email){
        Program.getInstance().getCustomers().add(new Customer(businessName,country,email));
        System.out.println("Creato!");
    }

    public void viewCustomers(){
        System.out.println();
        System.out.println("----------------------------------");
        for(Customer c : Program.getInstance().getCustomers()){
            System.out.println("Cliente -> ID: " + c.getId() + " Nome: " + c.getBusinessName() + " Email: " + c.getEmail() + " Nazionalità: " + c.getCountry());
        }
        System.out.println("----------------------------------");
        System.out.println();
    }

    public abstract void viewOperations();

    public abstract void viewInventory();

}