package UnitTest;

import operationsManager.*;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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

        p.login("Pampa", "studiomartino");
        assistant = (Assistant) p.getActiveUser();

        assertNotNull(assistant);
    }

    @Test
    @DisplayName("Create Operation Test")
    void testOperationCreation() {

        ArrayList<Pair<Article, Integer>> articles = new ArrayList<>();

        articles.add(new Pair<>(assistant.getInventory().getArticles().get(1), 20));
        articles.add(new Pair<>(assistant.getInventory().getArticles().get(2), 50));

        Customer customer = p.getCustomers().get(2);

        assistant.createOperation(customer, articles);

        Operation createdOperation = p.getOperations().get(p.getOperations().size() - 1);


        assertAll("Order's Data",
                () -> assertEquals(createdOperation.getAssistant(), assistant),
                () -> assertEquals(createdOperation.getCustomer(), customer),
                () -> assertEquals(createdOperation.getArticles().get(0), articles.get(0).getValue0()),
                () -> assertEquals(createdOperation.getArticles().get(1), articles.get(1).getValue0())
        );

    }

    @Test
    @DisplayName("Delete Operation Test")
    void testDeleteOperation() {

        Operation operation = p.getOperations().get(0);

        for (Operation i : p.getOperations()) {
            if (i.getAssistant() == assistant) {
                operation = i;
            }
        }

        int operationCountBefore1 = p.getOperations().size();

        assistant.deleteOperation(operation.getId());

        Operation finalOp = operation;
        assertAll("Operation deleted",
                () -> assertTrue(p.getOperations().size() < operationCountBefore1),
                () -> assertFalse(p.getOperations().contains(finalOp))
        );

    }

}
