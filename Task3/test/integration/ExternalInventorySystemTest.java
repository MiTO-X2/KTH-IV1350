package test.integration;

import integration.ExternalInventorySystem;
import dto.ItemDTO;
import dto.SaleDTO;
import model.Amount;
import model.VAT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExternalInventorySystemTest {
    private ExternalInventorySystem inventorySystem;

    @BeforeEach
    void setUp() {
        inventorySystem = new ExternalInventorySystem();
    }

    @AfterEach
    void tearDown() {
        inventorySystem = null;
    }

    @Test
    void testCheckIfItemIdentifierValid() {
        assertTrue(inventorySystem.checkIfItemIdentifierValid("1"), "Item identifier '1' should be valid.");
        assertFalse(inventorySystem.checkIfItemIdentifierValid("999"), "Item identifier '999' should be invalid.");
    }

    @Test
    void testRetrieveItemInformation() {
        ItemDTO item = inventorySystem.retrieveItemInformation("1");
        assertNotNull(item, "ItemDTO should not be null for a valid identifier.");
        assertEquals("1", item.getItemIdentifier(), "Item identifier should match.");
        assertEquals("BigWheel Oatmeal 500 ml", item.getItemDescription(), "Item description should match.");
        assertEquals(29.90, item.getPrice().getAmount(), "Item price should match.");
    }

    @Test
    void testUpdateInventory() {
        // Arrange
        List<ItemDTO> soldItems = new ArrayList<>();
        soldItems.add(new ItemDTO("1", "BigWheel Oatmeal 500 ml", new Amount(29.90), VAT.VAT_6, 5, "BigWheel Oatmeal"));
        soldItems.add(new ItemDTO("2", "YouGoGo Blueberry 240 g", new Amount(14.90), VAT.VAT_6, 3, "YouGoGo Blueberry"));

        SaleDTO saleDTO = new SaleDTO(soldItems, new Amount(0), new Amount(0));

        inventorySystem.updateInventory(saleDTO);

        ItemDTO updatedItem1 = inventorySystem.retrieveItemInformation("1");
        ItemDTO updatedItem2 = inventorySystem.retrieveItemInformation("2");

        assertEquals(15, updatedItem1.getQuantity(), "Quantity of item '1' should be updated to 15.");
        assertEquals(17, updatedItem2.getQuantity(), "Quantity of item '2' should be updated to 17.");
    }

    @Test
    void testCheckIfItemIdentifierValidWithEmptyString() {
        assertFalse(inventorySystem.checkIfItemIdentifierValid(""), "Empty string identifier should return false.");
    }

    @Test
    void testCheckIfItemIdentifierValidWithNull() {
        assertFalse(inventorySystem.checkIfItemIdentifierValid(null), "Null identifier should return false.");
    }
}