package UnitTest;

import operationsManager.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class CoreTest {

    private static Program p = Program.getInstance();

    @BeforeAll
    static void prepare() {
        try {
            p.load(DBConnectionTest.getInstance());
        } catch (SQLException e) {
            System.err.println("Impossible to load datas!");
            fail();
        }
    }

    @Test
    @DisplayName("Upload/Load data Test")
    void testUploadLoadData() throws SQLException {
        String sql;
        Statement stmt;
        ResultSet rs;

        Connection c = DBConnectionTest.getInstance();
        p.upload(c);
        p = Program.getInstance();

        c = DBConnectionTest.getInstance();

        String customerName = "Paperino";
        String customerCountry = "Paperinolandia";
        String customerEmail = "paperino.senzacognome@gmail.com";

        sql = "INSERT INTO Customer (BusinessName,Country,Email) " + "VALUES ('" + customerName + "', '" + customerCountry + "', '" + customerEmail + "');";

        stmt = c.createStatement();
        stmt.executeUpdate(sql);
        c.commit();

        String articleName = "Penny";
        float articlePrice = 10.2F;

        sql = "INSERT INTO Article (Name,Price) " + "VALUES ('" + articleName + "', '" + articlePrice + "');";
        stmt = c.createStatement();
        stmt.executeUpdate(sql);
        c.commit();

        p.load(c);

        Customer newCustomer = null;
        Article newArticle = null;

        for (Customer i : p.getCustomers()) {
            if (i.getBusinessName().equals(customerName) && i.getCountry().equals(customerCountry) && i.getEmail().equals(customerEmail)) {
                newCustomer = i;
            }
        }

        for (Article i : p.getArticles()) {
            if (i.getName().equals(articleName) && i.getPrice() == articlePrice) {
                newArticle = i;
            }
        }

        Customer finalNewCustomer = newCustomer;
        Article finalNewArticle = newArticle;
        assertAll("Load From DB",
                () -> assertNotNull(finalNewCustomer),
                () -> assertNotNull(finalNewArticle)
        );

        p.login("Nicola", "ilmiostudio");
        Dentist admin = (Dentist) p.getActiveUser();

        admin.createProduct("Berretto", 12.2F);
        admin.createCustomer("Minnie", "paperinolandiaSud", "minnie.senzacognome@gmail.com");

        p.upload(c);

        c = DBConnectionTest.getInstance();
        stmt = c.createStatement();

        int risArticle = 0;
        rs = stmt.executeQuery("SELECT COUNT(*) as ris FROM Article WHERE name='Berretto';");
        while (rs.next()) {
            risArticle = rs.getInt("ris");
        }

        int risCustomer = 0;
        rs = stmt.executeQuery("SELECT COUNT(*) as ris FROM Customer WHERE businessname='Minnie' AND country='paperinolandiaSud' AND email='minnie.senzacognome@gmail.com';");
        while (rs.next()) {
            risCustomer = rs.getInt("ris");
        }

        int finalRisArticle = risArticle;
        int finalRisCustomer = risCustomer;
        assertAll("Upload To DB",
                () -> assertTrue(finalRisArticle >= 1),
                () -> assertTrue(finalRisCustomer >= 1)
        );

        sql = "DELETE FROM Customer WHERE LOWER(businessname) LIKE '%test%';";
        stmt.executeUpdate(sql);
        c.commit();

        sql = "DELETE FROM Article WHERE LOWER(name) LIKE '%test%';";
        stmt.executeUpdate(sql);
        c.commit();
    }

    @Test
    @DisplayName("Login user Test")
    void testLoginUser() {

        p.login("Pampa", "studiomartino");

        User user = null;

        for (User i : p.getUsers()) {
            if (i.getName().equals("Pampa")) {
                user = i;
            }
        }

        User expectedUser1 = user;

        assertAll("Assistant Login",
                () -> assertEquals(expectedUser1, p.getActiveUser()),
                () -> assertTrue(p.getActiveUser() instanceof Assistant)
        );

        p.logout();

        p.login("Nicola", "ilmiostudio");

        user = null;

        for (User i : p.getUsers()) {
            if (i.getName().equals("Nicola")) {
                user = i;
            }
        }

        User expectedUser2 = user;

        assertAll("Dentist Login",
                () -> assertEquals(expectedUser2, p.getActiveUser()),
                () -> assertTrue(p.getActiveUser() instanceof Dentist)
        );
    }

}
