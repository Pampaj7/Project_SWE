package UnitTest;

import operationsManager.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class DentistTest {

    static Dentist admin = null;
    static Program p = Program.getInstance();

    @BeforeAll
    static void prepare(){

        try {
            p.load(DBConnectionTest.getInstance());
        } catch (SQLException e) {
            System.err.println("Unable to load data!");
            fail();
        }

        Program.getInstance().login("Admin","111");
        admin = (Dentist) Program.getInstance().getActiveUser();
        assertNotNull(admin);

    }

    @Test
    @DisplayName("Create Agent Test")
    void testCreateAgent() {

        Inventory inventory = p.getCatalogs().get( (int)((Math.random() * (p.getCatalogs().size()-1 - 1)) + 1) );
        admin.createAgent("UnitTest", "111",5.5F,inventory,"unitTest@gmail.com");
        User createUser = p.getUsers().get(p.getUsers().size()-1);

        assertTrue(createUser instanceof Assistant);

        Assistant createAgent = (Assistant) createUser;

        assertAll("Test create agent",
                () -> assertEquals(inventory, createAgent.getInventory()),
                () -> assertEquals(createAgent.getId(), p.getUsers().get(p.getUsers().size()-1).getId())
        );

    }

    @Test
    @DisplayName("Create Catalog Test")
    void testCreateCatalog() {

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(p.getArticles().get(1));
        articles.add(p.getArticles().get(2));
        articles.add(p.getArticles().get(3));
        int preSize = p.getCatalogs().size();
        admin.createCatalog("description","Italy",articles);

        assertAll("Test create agent",
                () -> assertEquals(preSize + 1, p.getCatalogs().size()),
                () -> assertEquals(articles, p.getCatalogs().get(p.getCatalogs().size()-1).getArticles())
        );

    }

    @Test
    @DisplayName("Create Product Test")
    void testCreateProduct() {
        int preSize = p.getArticles().size();
        admin.createProduct("ProductTestSingle",3.5F);

        assertAll("Single Article",
                () -> assertEquals(preSize + 1, p.getArticles().size()),
                () -> assertTrue(p.getArticles().get(p.getArticles().size()-1) instanceof Product),
                () -> assertEquals(p.getArticles().get(p.getArticles().size()-1).getPrice(), 3.5F)
        );

        int preSize2 = p.getArticles().size();
        ArrayList<Article> articles = new ArrayList<>();
        articles.add(p.getArticles().get(1));
        articles.add(p.getArticles().get(2));

        float tmp = 0 ;
        for (Article a : articles)
            tmp += a.getPrice();
        float prePrice = tmp;

        admin.createProduct("Compound article",articles);

        assertAll("Compound Article",
                () -> assertEquals(preSize2 + 1, p.getArticles().size()),
                () -> assertTrue(p.getArticles().get(p.getArticles().size()-1) instanceof Compound),
                () -> assertEquals(p.getArticles().get(p.getArticles().size()-1).getPrice(), prePrice)

        );

    }

    @Test
    @DisplayName("Delete Catalog Test")
    void testDeleteCatalog() {

        int preSize = p.getCatalogs().size();
        boolean check;

        check = checkCatalog(1);
        admin.deleteCatalog(1);

        if(check)
            assertEquals(preSize, p.getCatalogs().size());
        else
            assertEquals(preSize - 1, p.getCatalogs().size());

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(p.getArticles().get(1));
        articles.add(p.getArticles().get(2));
        articles.add(p.getArticles().get(3));
        admin.createCatalog("description","Italy",articles);
        preSize = p.getCatalogs().size();

        int lastCat = p.getCatalogs().get(p.getCatalogs().size()-1).getId();
        check = checkCatalog(lastCat);

        admin.deleteCatalog(lastCat);

        if(check)
            assertEquals(preSize, p.getCatalogs().size());
        else
            assertEquals(preSize - 1, p.getCatalogs().size());

    }

    @Test
    @DisplayName("Delete Product Test")
    void testDeleteArticle() {

        admin.createProduct("testProduct1 - can_delete",5.5F);
        Article P1 = p.getArticles().get(p.getArticles().size()-1);
        int A1 = p.getArticles().get(p.getArticles().size()-1).getId();
        admin.deleteProduct(A1);

        assertFalse(p.getArticles().contains(P1));

        int A2 = 1;
        Article P2 = null;

        for(Article a : p.getArticles()){
            if(a.getId()==1) {
                P2 = a;
            }
        }
        admin.deleteProduct(A2);
        assertTrue(p.getArticles().contains(P2)); //TODO rende falso

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(p.getArticles().get(2));
        articles.add(p.getArticles().get(3));
        admin.createProduct("testProduct2", articles);
        Article P3 = p.getArticles().get(2);
        int A3 = p.getArticles().get(2).getId();
        admin.deleteProduct(A3);

        assertTrue(p.getArticles().contains(P3));

    }

    @Test
    @DisplayName("Delete CustomerTest")
    void testDeleteCustomer() {

        Customer C1 = null;
        Customer C2 = null;

        for (Customer cli : p.getCustomers()){
            if(cli.getId() == 1)
                C1 = cli;
            if(cli.getId() == 2)
                C2 = cli;
        }

        admin.deleteCustomer(1); // SAFE DELETE
        assertFalse(p.getCustomers().contains(C1));

        admin.deleteCustomer(2); // BLOCK DELETE, the customer had an order
        assertTrue(p.getCustomers().contains(C2));

    }

    @Test
    @DisplayName("Delete Agent Test")
    void testDeleteAgent() {

        int id = 4;
        User userDel = null;
        admin.deleteAgent(id);
        boolean check = false;

        for (User u : p.getUsers()){
            if(u.getId() == id)
                userDel = u;
            check = u instanceof Dentist;
        }

        if(check)
            assertTrue(p.getUsers().contains(userDel));
        else {
            assertFalse(p.getUsers().contains(userDel));
            ArrayList<Operation> localOrders = new ArrayList<>();
            for(Operation o : p.getOperations()){
                if(o.getAssistant() == userDel){
                    localOrders.add(o);
                }
            }
            for(Operation o : localOrders)
                assertNull(o.getAssistant());
        }

    }

    private boolean checkCatalog(int id){
        for (Inventory t : p.getCatalogs()){
            if(t.getId()==id){
                for (User u : p.getUsers()){
                    if(u instanceof Assistant){
                        if(((Assistant)u).getInventory().equals(t)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
