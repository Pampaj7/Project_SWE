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
    @DisplayName("Create AssistantTest")
    void testCreateAssistant() {

        Inventory inventory = p.getInventories().get( (int)((Math.random() * (p.getInventories().size()-1 - 1)) + 1) );
        admin.createAssistant("UnitTest", "111",5.5F,inventory,"unitTest@gmail.com");
        User createUser = p.getUsers().get(p.getUsers().size()-1);

        assertTrue(createUser instanceof Assistant);

        Assistant createAgent = (Assistant) createUser;

        assertAll("Test create agent",
                () -> assertEquals(inventory, createAgent.getInventory()),
                () -> assertEquals(createAgent.getId(), p.getUsers().get(p.getUsers().size()-1).getId())
        );

    }

    @Test
    @DisplayName("Create InventoryTest")
    void testCreateInventory() {

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(p.getArticles().get(1));
        articles.add(p.getArticles().get(2));
        articles.add(p.getArticles().get(3));
        int preSize = p.getInventories().size();
        admin.createInventory("description","Italy",articles);

        assertAll("Test create agent",
                () -> assertEquals(preSize + 1, p.getInventories().size()),
                () -> assertEquals(articles, p.getInventories().get(p.getInventories().size()-1).getArticles())
        );

    }

    @Test
    @DisplayName("Create ProductTest")
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
    @DisplayName("Delete InventoryTest")
    void testDeleteInventory() {

        int preSize = p.getInventories().size();
        boolean check;

        check = checkInventory(1);
        admin.deleteInventory(1);

        if(check)
            assertEquals(preSize - 1, p.getInventories().size());
        else
            assertEquals(preSize - 1, p.getInventories().size());

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(p.getArticles().get(1));
        articles.add(p.getArticles().get(2));
        articles.add(p.getArticles().get(3));
        admin.createInventory("description","Italy",articles);
        preSize = p.getInventories().size();

        int lastCat = p.getInventories().get(p.getInventories().size()-1).getId();
        check = checkInventory(lastCat);

        admin.deleteInventory(lastCat);

        if(check)
            assertEquals(preSize - 1, p.getInventories().size());
        else
            assertEquals(preSize - 1, p.getInventories().size());

    }

    @Test
    @DisplayName("Delete ArticleTest")
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
        assertFalse(p.getArticles().contains(P2));

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(p.getArticles().get(2));
        articles.add(p.getArticles().get(3));
        admin.createProduct("testProduct2", articles);
        Article P3 = p.getArticles().get(2);
        int A3 = p.getArticles().get(2).getId();
        admin.deleteProduct(A3);

        assertFalse(p.getArticles().contains(P3));

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

    }

    @Test
    @DisplayName("Delete AssistantTest")
    void testDeleteAssistant() {

        int id = 0;
        int id2 = 3;

        for (User u : p.getUsers()) {
            if (u.getId() == id) {
                admin.deleteAssistant(id);
            }
        }


        assertFalse(p.getUsers().contains(id));


    }

    private boolean checkInventory(int id){
        for (Inventory t : p.getInventories()){
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
