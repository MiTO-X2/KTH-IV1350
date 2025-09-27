package test.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;
import integration.ExternalAccountingSystem;
import model.Amount;

public class ExternalAccountingSystemTest {
    @Test
    void testGetInstanceReturnsSameObject() {
        ExternalAccountingSystem instance1 = ExternalAccountingSystem.getInstance();
        ExternalAccountingSystem instance2 = ExternalAccountingSystem.getInstance();

        assertSame(instance1, instance2, "getInstance() should return the same singleton instance.");
    }

    @Test
    void testUpdateAccountingSystemPrintsCorrectOutput() {
        ExternalAccountingSystem accountingSystem = ExternalAccountingSystem.getInstance();
        Amount payment = new Amount(150.0);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        accountingSystem.updateAccountingSystem(payment);

        String output = outContent.toString().trim();
        assertTrue(output.contains("Accounting system updated with payment"),
                "Output should indicate that accounting system was updated.");
        assertTrue(output.contains("150.0"),
                "Output should include the payment amount.");
    }

    @Test
    void testUpdateAccountingSystemWithNullAmount() {
        ExternalAccountingSystem accountingSystem = ExternalAccountingSystem.getInstance();
        assertDoesNotThrow(() -> accountingSystem.updateAccountingSystem(null),
            "Should handle null payments gracefully (even if prints 'null').");
    }
}