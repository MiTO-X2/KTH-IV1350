package test.model;

import model.Amount;
import model.Item;
import model.VAT;
import dto.ItemDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemTest {
    private Item testItem;
    private ItemDTO testItemDTO;

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
        testItem = new Item(testItemDTO, 3); 
    }

    @AfterEach
    void tearDown() {
        testItemDTO = null;
        testItem = null; 
    }

    @Test
    void testGetPriceWithVAT() {
        Amount priceWithVAT = testItem.getPriceWithVAT();
        assertEquals(106.0, priceWithVAT.getAmount(), "Price with VAT should be 106.0");
    }

    @Test
    void testGetTotalPriceWithVAT() {
        Amount totalPriceWithVAT = testItem.getTotalPriceWithVAT();
        assertEquals(318.0, totalPriceWithVAT.getAmount(), "Total price with VAT should be 318.0");
    }

    @Test
    void testGetTotalVAT() {
        Amount totalVAT = testItem.getTotalVAT();
        assertEquals(18.0, totalVAT.getAmount(), "Total VAT should be 18.0");
    }

    @Test
    void testIncreaseQuantity() {
        testItem.increaseQuantity(3);
        assertEquals(6, testItem.getQuantity(), "Quantity should be increased to 6");
    }

    @Test
    void testSetQuantity() {
        testItem.setQuantity(10);
        assertEquals(10, testItem.getQuantity(), "Quantity should be set to 10");
    }

    @Test
    void testGetDTO() {
        ItemDTO dto = testItem.getDTO();
        assertEquals("123", dto.getItemIdentifier(), "DTO should have the correct identifier");
        assertEquals("Test Item", dto.getItemDescription(), "DTO should have the correct description");
        assertEquals(100.0, dto.getPrice().getAmount(), "DTO should have the correct price");
        assertEquals(0.06, dto.getVatRate().getRate(), "DTO should have the correct VAT rate");
        assertEquals(3, dto.getQuantity(), "DTO should have the correct quantity");
        assertEquals("Sample Item Name", dto.getName(), "DTO should have the correct name");
    }
}