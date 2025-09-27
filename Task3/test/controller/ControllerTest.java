package test.controller;

import controller.Controller;
import dto.ItemDTO;
import model.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;

class ControllerTest {
    private Controller controller;

    @BeforeEach
    void setUp() {
        controller = new Controller();
    }

    @AfterEach
    void tearDown() {
        controller = null;
    }

    @Test
    void testInitiateSale() {
        controller.initiateSale();
        assertNotNull(controller.getRunningTotal(), "Running total should not be null after initiating a sale.");
    }

    @Test
    void testRegisterItem() {
        controller.initiateSale();
        ItemDTO registeredItem = controller.registerItem("1");

        assertNotNull(registeredItem, "Registered item should not be null.");
        assertEquals("1", registeredItem.getItemIdentifier(), "Item identifier should match.");
        assertEquals("BigWheel Oatmeal 500 ml", registeredItem.getItemDescription(), "Item description should match.");
    }

    @Test
    void testRegisterItemIncreasesQuantity() {
        controller.initiateSale();
        controller.registerItem("1");
        controller.registerItem("1");

        Amount runningTotal = controller.getRunningTotal();
        assertNotNull(runningTotal, "Running total should not be null.");
        assertTrue(runningTotal.getAmount() > 0, "Running total should increase after registering items.");
    }

    @Test
    void testEndSale() {
        controller.initiateSale();
        controller.registerItem("1");
        Amount totalPrice = controller.endSale();

        assertNotNull(totalPrice, "Total price should not be null.");
        assertTrue(totalPrice.getAmount() > 0, "Total price should be greater than 0.");
    }

    @Test
    void testConcludeSale() {
        controller.initiateSale();
        controller.registerItem("1");
        controller.endSale();

        Amount payment = new Amount(100.0);
        Amount change = controller.concludeSale(payment);

        assertNotNull(change, "Change should not be null.");
        assertTrue(change.getAmount() >= 0, "Change should be greater than or equal to 0.");
    }

    @Test
    void testGetRunningTotal() {
        controller.initiateSale();
        controller.registerItem("1");

        Amount runningTotal = controller.getRunningTotal();
        assertNotNull(runningTotal, "Running total should not be null.");
        assertTrue(runningTotal.getAmount() > 0, "Running total should be greater than 0.");
    }

    @Test
    void testGetRunningVAT() {
        controller.initiateSale();
        controller.registerItem("1");

        Amount runningVAT = controller.getRunningVAT();
        assertNotNull(runningVAT, "Running VAT should not be null.");
        assertTrue(runningVAT.getAmount() > 0, "Running VAT should be greater than 0.");
    }

    @Test 
    void testEndSaleWithoutItems() {
        controller.initiateSale();
        Amount total = controller.endSale();
        assertEquals(0.0, total.getAmount(), 0.001, "Total should be 0 if no items were registered.");
    }

    @Test 
    void testConcludeSaleWithoutItems() {
        controller.initiateSale();
        Amount change = controller.concludeSale(new Amount(100.0));
        assertEquals(100.0, change.getAmount(), 0.001, "Change should equal payment if no items were registered.");
    }
}