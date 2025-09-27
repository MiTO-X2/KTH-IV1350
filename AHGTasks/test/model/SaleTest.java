package test.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import model.Sale;
import model.VAT;
import test.utils.RevenueObserverTest;
import model.Amount;
import model.DiscountStrategy;

public class SaleTest {
    private ItemDTO testItemDTO; 
    private Sale testSale;

    @BeforeEach
    void setUp() {
        testItemDTO = new ItemDTO(
            "123", 
            "Test Item", 
            new Amount(100.0), 
            VAT.VAT_6,
            3, 
            "Sample Item Name"
        );
        
        testSale = new Sale();
    }

    @AfterEach
    void tearDown() {
        testItemDTO = null;
        testSale = null;
    }

    @Test
    void testAddItemToSale() {
        testSale.addItemToSale(testItemDTO, testItemDTO.getQuantity());

        List<ItemDTO> itemDTOList = testSale.getListOfItemDTOs(); 
        ItemDTO insertedItemDTO = itemDTOList.get(0);

        assertEquals(testItemDTO.getItemDescription(), insertedItemDTO.getItemDescription(), "Inserting ItemDTO to Sale has changed its description?");
        assertEquals(testItemDTO.getItemIdentifier(), insertedItemDTO.getItemIdentifier(), "Inserting ItemDTO to Sale has changed its identifier?");
        assertEquals(testItemDTO.getName(), insertedItemDTO.getName(), "Inserting ItemDTO to Sale has changed its name?");
        assertEquals(testItemDTO.getPrice(), insertedItemDTO.getPrice(), "Inserting ItemDTO to Sale has changed its price?");
        assertEquals(testItemDTO.getQuantity(), insertedItemDTO.getQuantity(), "Inserting ItemDTO to Sale has changed its quantity?");
        assertEquals(testItemDTO.getVatRate(), insertedItemDTO.getVatRate(), "Inserting ItemDTO to Sale has changed its VAT Rate?");
    }
    
    @Test
    void testAddItemToSaleIncreasesQuantity() {
        testSale.addItemToSale(testItemDTO, 1);
        testSale.addItemToSale(testItemDTO, 2);

        ItemDTO updatedItem = testSale.getItemByIdentifier("123");
        assertEquals(3, updatedItem.getQuantity(), "Quantity should be updated to 3.");
    }

    @Test
    void testAddItemWithMaxIntQuantity() {
        testSale.addItemToSale(testItemDTO, Integer.MAX_VALUE);
        ItemDTO addedItem = testSale.getItemByIdentifier(testItemDTO.getItemIdentifier());
        assertEquals(Integer.MAX_VALUE, addedItem.getQuantity(), "Should support large item quantities.");
    }

    @Test
    void testAddNullItem() {
        assertThrows(NullPointerException.class, () -> {
            testSale.addItemToSale(null, 1);
        }, "Should throw exception when adding null item.");
    }

    @Test
    void testCalculateTotalPrice() {
        ItemDTO exampleTestItemDTO = new ItemDTO("1234", "Item 1", new Amount(100.0), VAT.VAT_25, 1, "Item One");

        testSale.addItemToSale(exampleTestItemDTO, 1);
        testSale.addItemToSale(testItemDTO, 2);

        Amount totalPrice = testSale.calculateTotalPrice();
        assertEquals(337.0, totalPrice.getAmount(), "Total price should be 337.0.");
    }

    @Test
    void testCalculateTotalOnEmptySale() {
        Amount total = testSale.calculateTotalPrice();
        assertEquals(0.0, total.getAmount(), "Total should be 0 for empty sale.");
    }
    
    @Test
    void testCalculateTotalVAT() {
        ItemDTO exampleTestItemDTO = new ItemDTO("345", "Item 1", new Amount(100.0), VAT.VAT_12, 1, "Item One");

        testSale.addItemToSale(exampleTestItemDTO, 4);
        testSale.addItemToSale(testItemDTO, 5);

        Amount totalVAT = testSale.calculateTotalVAT();
        assertEquals(78.0, totalVAT.getAmount(), "Total VAT should be 78.0.");
    }

    @Test
    void testPayAndCalculateChange() {
        ItemDTO exampleTestItemDTO = new ItemDTO("345", "Item 1", new Amount(100.0), VAT.VAT_6, 1, "Item One");
        testSale.addItemToSale(exampleTestItemDTO, 3);

        testSale.pay(new Amount(400.0));
        Amount change = testSale.calculateChange();

        assertEquals(82.0, change.getAmount(), "Change should be 82.0.");
    }

    @Test
    void testGetSaleInformation() {
        ItemDTO exampleTestItemDTO = new ItemDTO("345", "Item 1", new Amount(100.0), VAT.VAT_6, 1, "Item One");
        testSale.addItemToSale(exampleTestItemDTO, 2);
        testSale.addItemToSale(testItemDTO, 4);

        SaleDTO saleDTO = testSale.getSaleInformation();
        List<ItemDTO> itemDTOs = saleDTO.getItems();

        assertEquals(2, itemDTOs.size(), "SaleDTO should contain two item (types).");
        assertEquals(636.0, saleDTO.getRunningTotal().getAmount(), "Total price in SaleDTO should be 636.0.");
        assertEquals(36.0, saleDTO.getTotalVAT().getAmount(), "Total VAT in SaleDTO should be 36.0.");
    }    

    @Test
    void testGetReceiptInformation() {
        ItemDTO exampleTestItemDTO = new ItemDTO("345", "Item 1", new Amount(100.0), VAT.VAT_6, 1, "Item One");
        testSale.addItemToSale(exampleTestItemDTO, 2);

        testSale.pay(new Amount(300.0));

        ReceiptDTO receiptDTO = testSale.getReceiptInformation();

        assertNotNull(receiptDTO, "ReceiptDTO should not be null.");
        assertEquals(1, receiptDTO.getItems(), "ReceiptDTO should contain one item (type).");
        assertEquals(212.0, receiptDTO.getTotalPrice().getAmount(), "Total price in ReceiptDTO should be 212.0.");
        assertEquals(88.0, receiptDTO.getChange().getAmount(), "Change in ReceiptDTO should be 75.0.");
    }

    @Test
    void testAddItemWithNegativeQuantityThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            testSale.addItemToSale(testItemDTO, -5);
        });
        assertTrue(exception.getMessage().contains("Invalid quantity"));
    }

    @Test
    void testAddItemWithZeroQuantityThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            testSale.addItemToSale(testItemDTO, 0);
        });
        assertTrue(exception.getMessage().contains("Invalid quantity"));
    }

    @Test
    void testPayWithLessThanTotalThrowsException() {
        testSale.addItemToSale(testItemDTO, 1);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            testSale.pay(new Amount(50.0));
        });
        assertTrue(exception.getMessage().contains("Payment is less than the total price"));
    }

    @Test
    void testIncreaseQuantityOnNonexistentItemThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            testSale.increaseQuantity("not-exist", 2);
        });
        assertTrue(exception.getMessage().contains("Item with identifier"));
    }

    @Test
    void testIncreaseQuantityWithNonPositiveAmountThrowsException() {
        testSale.addItemToSale(testItemDTO, 2);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            testSale.increaseQuantity(testItemDTO.getItemIdentifier(), 0);
        });
        assertTrue(exception.getMessage().contains("must be positive"));
    }

    @Test
    void testGetItemByIdentifierReturnsNullForUnknownId() {
        assertNull(testSale.getItemByIdentifier("unknown"), "Should return null for unknown item identifier.");
    }

    @Test
    void testIncreaseQuantityOfExistingItem() {
        testSale.addItemToSale(testItemDTO, 1);
        ItemDTO updatedItem = testSale.increaseQuantity("123", 2);
        assertEquals(3, updatedItem.getQuantity(), "Quantity should be updated to 3 after increase.");
    }

    @Test
    void testCheckIfItemIsRegisteredReturnsTrue() {
        testSale.addItemToSale(testItemDTO, 1);
        assertTrue(testSale.checkIfItemRegistered("123"), "Item should be registered.");
    }

    @Test
    void testCheckIfItemIsRegisteredReturnsFalse() {
        assertFalse(testSale.checkIfItemRegistered("nonexistent"), "Item should not be registered.");
    }

    @Test
    void testSetAndGetCustomerID() {
        testSale.setCustomerID("CUST123");
        assertEquals("CUST123", testSale.getCustomerID(), "Customer ID should match.");
    }

    @Test
    void testRevenueObserverIsNotified() {
        RevenueObserverTest observer = new RevenueObserverTest();
        testSale.addRevenueObserver(observer);

        testSale.addItemToSale(testItemDTO, 2);
        testSale.pay(new Amount(300));

        assertNotNull(observer.getLatestRevenue(), "Observer should be notified.");
        assertEquals(212.0, observer.getLatestRevenue().getAmount(), 0.01, "Revenue should be correct.");
    }

    @Test
    void testCalculateDiscountsWithNullStrategy() {
        testSale.addItemToSale(testItemDTO, 1);
        testSale.calculateDiscounts(null);

        Amount discountedTotal = testSale.calculateTotalPriceAfterDiscount();
        assertEquals(testSale.calculateTotalPrice().getAmount(), discountedTotal.getAmount(), "Without strategy, total should be unchanged.");
    }

    @Test
    void testCalculateDiscountsWithMockStrategy() {
        testSale.addItemToSale(testItemDTO, 2);

        DiscountStrategy mockDiscount = sale -> new Amount(50.0);
        testSale.calculateDiscounts(mockDiscount);

        Amount discountedTotal = testSale.calculateTotalPriceAfterDiscount();
        assertEquals(162.0, discountedTotal.getAmount(), "Discounted total should be 162.0.");
    }
}