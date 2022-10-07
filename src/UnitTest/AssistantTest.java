package UnitTest;

import operationsManager.*;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AssistantTest {

    private static Assistant assistant = null;
    private static final Program p = Program.getInstance();

    @BeforeAll
    static void prepare() {
        try {
            p.load(DBConnectionTest.getInstance());
        } catch (SQLException e) {
            System.err.println("Unable to load data!");
            fail();
        }

        p.login("Agent1", "111");
        assistant = (Assistant) p.getActiveUser();

        assertNotNull(assistant);
    }

    @Test
    @DisplayName("Create Order Test")
    void testOrderCreation() {

        ArrayList<Pair<Article, Integer>> articles = new ArrayList<>();

        articles.add(new Pair<>(assistant.getInventory().getArticles().get(1), 20));
        articles.add(new Pair<>(assistant.getInventory().getArticles().get(2), 50));

        Customer customer = p.getCustomers().get(2);

        assistant.createOperation(customer, articles);

        Operation createdOrder = p.getOperations().get(p.getOperations().size() - 1);

        //String messageNotification = Program.getInstance().getNotificationCenter().getNotification().get(Program.getInstance().getNotificationCenter().getNotification().size() - 1);

        assertAll("Order's Data",
                () -> assertEquals(createdOrder.getAssistant(), assistant),
                () -> assertEquals(createdOrder.getCustomer(), customer),
                () -> assertEquals(createdOrder.getArticles().get(0), articles.get(0).getValue0()),
                () -> assertEquals(createdOrder.getArticles().get(1), articles.get(1).getValue0())
                //() -> assertTrue(messageNotification.contains(assistant.getName()))
        );

    }

    @Test
    @DisplayName("Delete Order Test")
    void testDeleteOrder() {

        Operation order = p.getOperations().get(0);

        for (Operation i : p.getOperations()) {
            if (i.getAssistant() == assistant) {
                order = i;
            }
        }

        int orderCountBefore1 = p.getOperations().size();

        assistant.deleteOperation(order.getId());

        Operation finalOrder1 = order;
        assertAll("Order deleted",
                () -> assertTrue(p.getOperations().size() < orderCountBefore1),
                () -> assertFalse(p.getOperations().contains(finalOrder1))
        );

    }

}
