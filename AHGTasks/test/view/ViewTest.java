package test.view;

import controller.Controller;
import dto.ItemDTO;
import exceptions.DatabaseFailureException;
import exceptions.ItemNotFoundException;
import model.Amount;
import model.VAT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.View;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ViewTest {
    private View view;
    private ControllerStub controllerStub;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        controllerStub = new ControllerStub();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); 
        view = new View(controllerStub);
    }

    @Test
    void testSimulateSaleExecution() {
        String output = outputStream.toString();
        System.out.println(output);

        assertTrue(output.contains("Sale initiated."), "Output should indicate that the sale was initiated.");

        assertTrue(output.contains("Item ID: 1"), "Output should include details for item ID 1.");
        assertTrue(output.contains("Item ID: 2"), "Output should include details for item ID 2.");
        assertTrue(output.contains("Item ID: 3"), "Output should include details for item ID 3.");
        assertTrue(output.contains("Item ID: 4"), "Output should include details for item ID 4.");

        assertTrue(output.contains("Total cost (incl VAT):"), "Output should include the total cost.");
        assertTrue(output.contains("Total VAT:"), "Output should include the total VAT.");

        assertTrue(output.contains("End sale:"), "Output should indicate that the sale was ended.");
        assertTrue(output.contains("Total cost (incl VAT): "), "Output should include the total cost after ending the sale.");

        assertTrue(output.contains("Discount applied: 10.00 SEK"), "Discount should be printed.");
        assertTrue(output.contains("Total cost (incl VAT): 100.00"), "Final total cost should be printed.");
        assertTrue(output.contains("[USER MESSAGE] The item with ID 'fail' was not found."), "ItemNotFoundException should be handled.");
        assertTrue(output.contains("[USER MESSAGE] There is a technical issue accessing the database."), "DatabaseFailureException should be handled.");
    }

    @Test
    void testIllegalArgumentExceptionHandling() {
        controllerStub.throwIllegalArgument = true;

        new View(controllerStub);
        String output = outputStream.toString();

        assertTrue(output.contains("[USER MESSAGE] Invalid item registration:"), "IllegalArgumentException should be handled.");
    }

    /**
     * A stub implementation of the Controller class for testing purposes.
     */
    private static class ControllerStub extends Controller {
        boolean throwIllegalArgument = false;

        private final Map<String, ItemDTO> itemMap = new HashMap<>();

        @Override
        public void initiateSale() {
        }

        @Override
        public ItemDTO registerItem(String itemIdentifier) throws ItemNotFoundException, DatabaseFailureException {
            if ("fail".equals(itemIdentifier)) {
                throw new exceptions.ItemNotFoundException("fail");
            } else if ("DBFAIL".equals(itemIdentifier)) {
                throw new exceptions.DatabaseFailureException("Simulated DB error");
            } else if (throwIllegalArgument) {
                throw new IllegalArgumentException("Simulated illegal argument");
            }
            return itemMap.getOrDefault(itemIdentifier,
                    new ItemDTO(itemIdentifier, "Test Item " + itemIdentifier, new Amount(20.0), VAT.VAT_25, 1, "Test Description"));
        }

        @Override
        public Amount getRunningTotal() {
            return new Amount(100.0);
        }

        @Override
        public Amount getRunningVAT() {
            return new Amount(25.0);
        }

        @Override
        public Amount getRunningDiscount() {
            return new Amount(10.0);
        }

        @Override
        public void applyDiscount(String customerID) {}

        @Override
        public Amount endSale() {
            return new Amount(100.0);
        }

        @Override
        public Amount concludeSale(Amount payment) {
            return new Amount(400.0); 
        }
    }   
}