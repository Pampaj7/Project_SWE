package operationsManager;

import org.javatuples.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public final class Program {

    private User activeUser;
    private final ArrayList<User> users;
    private final ArrayList<Article> articles;
    private final ArrayList<Inventory> inventories;
    private final ArrayList<Customer> customers;
    private final ArrayList<Operation> operations;
    private final NotificationCenter notCenter;
    private static Program instance;
    private Menu menu;
    private Boolean wantClose = false;
    private final NotificationEmail emailNot;

    private Program() {
        notCenter = new NotificationCenter();
        emailNot = new NotificationEmail();
        users = new ArrayList<>();
        articles = new ArrayList<>();
        inventories = new ArrayList<>();
        operations = new ArrayList<>();
        customers = new ArrayList<>();
        activeUser = null;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public NotificationCenter getNotificationCenter() {
        return notCenter;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public ArrayList<Inventory> getInventories() {
        return inventories;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public static Program getInstance() {

        if (instance == null)
            instance = new Program();

        return instance;
    }

    void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void run() {

        try {
            this.load(DBConnection.getInstance());
        } catch (SQLException e) {
            System.err.println("Impossibile caricare!");
            return;
        }

        this.setMenu(new loginMenu());

        while (!wantClose)
            menu.showMenu();

        System.out.println("Buona giornata!");
        this.upload(DBConnection.getInstance());

    }

    public boolean login(String name, String psw) {
        for (User i : users) {
            if (name.equals(i.getName())&&psw.equals(i.getPassword())) {
                activeUser = i;
                break;
            }
        }

        if (activeUser == null) {
            System.err.println("Password o username errati!");
            return false;
        }

        if (activeUser instanceof Dentist)
            this.setMenu(new DentistMainMenu());
        else {
            this.setMenu(new AssistantMainMenu());
            ((Assistant) activeUser).attach(notCenter);
            ((Assistant) activeUser).attach(emailNot);
        }

        return true;
    }

    public void logout() {
        if (activeUser instanceof Assistant) {
            ((Assistant) activeUser).detach(notCenter);
            ((Assistant) activeUser).detach(emailNot);
        }
        activeUser = null;
        this.setMenu(new loginMenu());
    }

    public void load(Connection c) throws SQLException {

        Statement stmt = c.createStatement();
        Statement stmt1 = c.createStatement();
        ResultSet rs, rs1;

        rs = stmt.executeQuery("SELECT * FROM Customer;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String businessName = rs.getString("BusinessName");
            String country = rs.getString("Country");
            String email = rs.getString("Email");
            customers.add(new Customer(id, businessName, country, email));
        }

        rs = stmt.executeQuery("SELECT * FROM Notification;");
        while (rs.next()) {
            String message = rs.getString("message");
            notCenter.addNotification(message);
        }

        rs = stmt.executeQuery("SELECT * FROM Article WHERE id not in (SELECT IdCompound FROM ArticleCompound );");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            float price = rs.getFloat("Price");
            articles.add(new Product(name, price, id));
        }
        rs = stmt.executeQuery("SELECT * FROM Article WHERE id in (SELECT IdCompound FROM ArticleCompound );");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            ArrayList<Article> components = new ArrayList<>();
            rs1 = stmt1.executeQuery("SELECT * FROM ArticleCompound WHERE IdCompound = " + id + " ;");
            while (rs1.next()) {
                int idComponent = rs1.getInt("idComponent");

                for (Article a : articles) {
                    if (a.getId() == idComponent) {
                        components.add(a);
                        break;
                    }
                }
            }
            articles.add(new Compound(name, components, id));
        }

        rs = stmt.executeQuery("SELECT * FROM InventoryUser;");
        while (rs.next()) {
            int id = rs.getInt("idHead");
            String description = rs.getString("Description");
            String marketZone = rs.getString("MarketZone");
            ArrayList<Article> tmp = new ArrayList<>();
            rs1 = stmt1.executeQuery("SELECT * FROM InventoryArticle WHERE IdHead = " + id + " ;");
            while (rs1.next()) {
                int idArticle = rs1.getInt("idArticle");
                for (Article a : articles) {
                    if (a.getId() == idArticle) {
                        tmp.add(a);
                        break;
                    }
                }
            }
            inventories.add(new Inventory(tmp, description, marketZone, id));
        }

        rs = stmt.executeQuery("SELECT * FROM User;");
        while (rs.next()) {
            int id = rs.getInt("id");                             //1 agent - 0 administrator
            String name = rs.getString("Name");
            String passHash = rs.getString("Password");
            int type = rs.getInt("Type");
            int idInventory = rs.getInt("IdInventory");
            float personalCost = rs.getFloat("PersonalCost");
            String email = rs.getString("email");

            if (type == 1) {
                Inventory tmp = null;
                for (Inventory i : inventories) {
                    if (i.getId() == idInventory) {
                        tmp = i;
                    }
                }

                if (tmp == null) {
                    System.err.println("Inventory don't exist!");
                    break;
                }

                users.add(new Assistant(name, passHash, personalCost, tmp, email, id));
            } else {
                users.add(new Dentist(name, passHash, email, id));
            }
        }

        rs = stmt.executeQuery("SELECT * FROM CustomerAssistant;");
        while (rs.next()) {
            int id = rs.getInt("idHead");
            int Assistant = rs.getInt("idAssistant");
            int idCustomers = rs.getInt("IdCustomer");
            float total = rs.getFloat("Total");
            float cost = rs.getFloat("PersonalCost");

            Assistant tmpAssistant = null;
            for (User i : users) {
                if (i.getId() == Assistant) {
                    tmpAssistant = (Assistant) i;
                    break;
                }
            }

            Customer tmpCustomer = null;
            for (Customer i : customers) {
                if (i.getId() == idCustomers) {
                    tmpCustomer = i;
                    break;
                }
            }

            if (tmpCustomer == null) {
                System.err.println("Customer don't exist!");
                break;
            }

            ArrayList<Pair<Article, Integer>> tmp = new ArrayList<>();
            rs1 = stmt1.executeQuery("SELECT * FROM CustomerAssistantArticle WHERE IdHead = " + id + " ;");
            while (rs1.next()) {
                int idArticle = rs1.getInt("idArticle");
                int qta = rs1.getInt("qta");
                for (Article a : articles) {
                    if (a.getId() == idArticle) {
                        tmp.add(new Pair<>(a, qta));
                        break;
                    }
                }
            }
            operations.add(new Operation(total, cost, tmpAssistant, tmp, tmpCustomer, id));
        }

    }

    public void upload(Connection c) {
        String sql;
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            for (String s : Arrays.asList("DELETE FROM User;", "DELETE FROM CustomerAssistant;", "DELETE FROM CustomerAssistantArticle;", "DELETE FROM Notification;", "DELETE FROM Customer;", "DELETE FROM InventoryArticle;", "DELETE FROM InventoryUser;", "DELETE FROM Article;", "DELETE FROM ArticleCompound;")) {
                sql = s;
                stmt.executeUpdate(sql);
                c.commit();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        int type;
        float perch;
        for (User user : users) {
            try {
                if (!(user instanceof Assistant)) {
                    type = 0;
                    perch = 0;
                    sql = "INSERT INTO User (Id,Name,Password,Type,PersonalCost,email) " + "VALUES (" + user.getId() + ", '" + user.getName() + "', '" + user.getPassword() + "', " + type + ", " + perch + " ,'" + user.getEmail() + "');";
                } else {
                    type = 1;
                    Assistant tmp = (Assistant) user;
                    perch = tmp.getPersonalCost();
                    sql = "INSERT INTO User (Id,Name,Password,Type,PersonalCost,idInventory,email) " + "VALUES (" + user.getId() + ", '" + user.getName() + "', '" + user.getPassword() + "', " + type + ", " + perch + " ," + tmp.getInventory().getId() + " ,'" + user.getEmail() + "');";
                }

                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                c.commit();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }

        for (Customer customer : customers) {
            try {
                sql = "INSERT INTO Customer (id,BusinessName,Country,Email) " + "VALUES (" + customer.getId() + ", '" + customer.getBusinessName() + "', '" + customer.getCountry() + "', '" + customer.getEmail() + "');";
                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                c.commit();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }

        for (Operation operation : operations) {
            try {
                if (operation.getAssistant() != null)
                    sql = "INSERT INTO CustomerAssistant (idHead,idAssistant,IdCustomer,Total,PersonalCost) " + "VALUES (" + operation.getId() + ", '" + operation.getAssistant().getId() + "', " + operation.getCustomer().getId() + " ,'" + operation.getTotal() + "', '" + operation.getOPersonalCost() + "');";
                else
                    sql = "INSERT INTO CustomerAssistant (idHead,idAssistant,IdCustomer,Total,PersonalCost) " + "VALUES (" + operation.getId() + ", '" + -1 + "', " + operation.getCustomer().getId() + " ,'" + operation.getTotal() + "', '" + operation.getOPersonalCost() + "');";
                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                c.commit();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
            try {
                for (Pair<Article, Integer> i : operation.getRows()) {
                    sql = "INSERT INTO CustomerAssistantArticle (idHead,idArticle,qta) " + "VALUES (" + operation.getId() + ", " + i.getValue0().getId() + "," + i.getValue1() + ");";
                    stmt = c.createStatement();
                    stmt.executeUpdate(sql);
                    c.commit();
                }
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }

        for (Inventory inventory : inventories) {
            try {
                sql = "INSERT INTO InventoryUser (idHead,Description,MarketZone) " + "VALUES (" + inventory.getId() + ", '" + inventory.getDescription() + "', '" + inventory.getZone() + "');";
                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                c.commit();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
            try {
                for (Article article : inventory.getArticles()) {
                    sql = "INSERT INTO InventoryArticle (idHead,idArticle) " + "VALUES (" + inventory.getId() + ", " + article.getId() + ");";
                    stmt = c.createStatement();
                    stmt.executeUpdate(sql);
                    c.commit();
                }
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }

        }

        for (Article article : articles) {
            if (article instanceof Compound) {
                Compound tmp = (Compound) article;
                for (Article a : tmp.getComponents()) {
                    try {
                        sql = "INSERT INTO ArticleCompound (IdCompound,IdComponent) " + "VALUES (" + article.getId() + ", " + a.getId() + ");";
                        stmt = c.createStatement();
                        stmt.executeUpdate(sql);
                        c.commit();
                    } catch (Exception e) {
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    }
                }
            }
            try {
                sql = "INSERT INTO Article (Id,Name,Price) " + "VALUES (" + article.getId() + ", '" + article.getName() + "', '" + article.getPrice() + "');";
                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                c.commit();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }

        for (String notify : notCenter.getNotification()) {
            try {
                sql = "INSERT INTO Notification (Message) " + "VALUES ('" + notify + "');";
                stmt = c.createStatement();
                stmt.executeUpdate(sql);
                c.commit();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }

        try {
            stmt.close();
            c.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        instance = null;
    }

    void close() {
        wantClose = true;
    }

}