package test.view;

import controller.Controller;
import dto.ItemDTO;
import model.Amount;
import model.VAT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.View;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
    }

    /**
     * A stub implementation of the Controller class for testing purposes.
     */
    private static class ControllerStub extends Controller {
        @Override
        public void initiateSale() {
        }

        @Override
        public ItemDTO registerItem(String itemIdentifier) {
            return new ItemDTO(itemIdentifier, "Test Item " + itemIdentifier, new Amount(20.0), VAT.VAT_25, 1, "Test Description");
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
        public Amount endSale() {
            return new Amount(100.0);
        }

        @Override
        public Amount concludeSale(Amount payment) {
            return new Amount(400.0); 
        }

    }
    
}