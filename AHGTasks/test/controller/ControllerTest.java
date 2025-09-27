package test.controller;

import controller.Controller;
import dto.ItemDTO;
import model.Amount;
import utils.RevenueObserver;
import exceptions.DatabaseFailureException;
import exceptions.ItemNotFoundException;

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
    void testRegisterItem() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        ItemDTO registeredItem = controller.registerItem("1");

        assertNotNull(registeredItem, "Registered item should not be null.");
        assertEquals("1", registeredItem.getItemIdentifier(), "Item identifier should match.");
        assertEquals("BigWheel Oatmeal 500 ml", registeredItem.getItemDescription(), "Item description should match.");
    }

    @Test
    void testRegisterItemIncreasesQuantity() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        controller.registerItem("1");

        Amount runningTotal = controller.getRunningTotal();
        assertNotNull(runningTotal, "Running total should not be null.");
        assertTrue(runningTotal.getAmount() > 0, "Running total should increase after registering items.");
    }

    @Test
    void testEndSale() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        Amount totalPrice = controller.endSale();

        assertNotNull(totalPrice, "Total price should not be null.");
        assertTrue(totalPrice.getAmount() > 0, "Total price should be greater than 0.");
    }

    @Test
    void testConcludeSale()  throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        controller.endSale();

        Amount payment = new Amount(100.0);
        Amount change = controller.concludeSale(payment);

        assertNotNull(change, "Change should not be null.");
        assertTrue(change.getAmount() >= 0, "Change should be greater than or equal to 0.");
    }

    @Test
    void testGetRunningTotal() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");

        Amount runningTotal = controller.getRunningTotal();
        assertNotNull(runningTotal, "Running total should not be null.");
        assertTrue(runningTotal.getAmount() > 0, "Running total should be greater than 0.");
    }

    @Test
    void testGetRunningVAT() throws ItemNotFoundException, DatabaseFailureException {
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

    @Test
    void testRegisterItem_InvalidIdentifier_ThrowsItemNotFoundException() {
        controller.initiateSale();
        assertThrows(ItemNotFoundException.class, () -> {
            controller.registerItem("invalid_id");
        }, "Should throw ItemNotFoundException for invalid identifier.");
    }

    @Test
    void testRegisterItem_DatabaseFailure_ThrowsDatabaseFailureException() {
        controller.initiateSale();
        assertThrows(DatabaseFailureException.class, () -> {
            controller.registerItem("DBFAIL");
        }, "Should throw DatabaseFailureException for database failure.");
    }

    @Test
    void testApplyCustomerDiscount() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        Amount totalBeforeDiscount = controller.getRunningTotal();

        controller.applyDiscount("customer123");
        Amount totalAfterDiscount = controller.endSale();

        assertTrue(totalAfterDiscount.getAmount() < totalBeforeDiscount.getAmount(), 
            "Total after discount should be less than total before discount.");
    }

    @Test
    void testRunningDiscountIncreasesWithDiscountEligibleItems() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        controller.applyDiscount("customer123");

        Amount discount = controller.getRunningDiscount();

        assertNotNull(discount, "Discount should not be null.");
        assertTrue(discount.getAmount() >= 0, "Discount should be non-negative.");
    }

    static class TestRevenueObserver implements RevenueObserver {
        public boolean notified = false;
        public Amount receivedAmount;

        @Override
        public void newRevenue(Amount revenue) {
            notified = true;
            receivedAmount = revenue;
        }
    }

    @Test
    void testRevenueObserverIsNotified() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        controller.endSale();

        TestRevenueObserver observer = new TestRevenueObserver();
        controller.addRevenueObserver(observer);

        controller.concludeSale(new Amount(100.0));

        assertTrue(observer.notified, "Observer should be notified.");
        assertNotNull(observer.receivedAmount, "Observer should receive the revenue amount.");
    }

    @Test
    void testRegisterMultipleItems() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        controller.registerItem("2");
        controller.registerItem("3");

        Amount total = controller.getRunningTotal();
        assertNotNull(total, "Total should not be null after registering multiple items.");
        assertTrue(total.getAmount() > 0, "Total should be greater than 0 after registering multiple items.");
    }

    @Test
    void testApplyDiscountWithInvalidCustomerIDDoesNotCrash() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        
        controller.applyDiscount("invalid_customer");
        Amount totalAfterDiscount = controller.endSale();

        assertTrue(totalAfterDiscount.getAmount() > 0, "Total should be greater than 0 after applying discount with invalid customer ID.");
    }

    @Test
    void testMultipleRevenueObserversAreNotified() throws ItemNotFoundException, DatabaseFailureException {
        controller.initiateSale();
        controller.registerItem("1");
        controller.endSale();

        TestRevenueObserver observer1 = new TestRevenueObserver();
        TestRevenueObserver observer2 = new TestRevenueObserver();

        controller.addRevenueObserver(observer1);
        controller.addRevenueObserver(observer2);

        controller.concludeSale(new Amount(100.0));

        assertTrue(observer1.notified, "First observer should be notified.");
        assertTrue(observer2.notified, "Second observer should be notified.");
    }
    
    @Test
    void testObserverAddedBeforeSaleIsStillNotified() throws ItemNotFoundException, DatabaseFailureException {
        TestRevenueObserver observer = new TestRevenueObserver();
        controller.addRevenueObserver(observer);

        controller.initiateSale();
        controller.registerItem("1");
        controller.endSale();
        controller.concludeSale(new Amount(100.0));

        assertTrue(observer.notified, "Observer added before sale should still be notified.");
    }
}