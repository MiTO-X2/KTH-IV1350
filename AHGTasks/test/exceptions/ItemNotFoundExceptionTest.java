package test.exceptions;

import exceptions.ItemNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemNotFoundExceptionTest {

    @Test
    void testExceptionMessageIsFormattedCorrectly() {
        String itemId = "ABC123";
        ItemNotFoundException exception = new ItemNotFoundException(itemId);

        String expectedMessage = "Item with ID '" + itemId + "' was not found in the inventory.";
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the expected format.");
    }

    @Test
    void testGetItemIdentifierReturnsCorrectValue() {
        String itemId = "XYZ999";
        ItemNotFoundException exception = new ItemNotFoundException(itemId);

        assertEquals(itemId, exception.getItemIdentifier(), "getItemIdentifier should return the original item ID.");
    }

    @Test
    void testThrowAndCatchItemNotFoundException() {
        String itemId = "FAIL42";

        Exception thrown = assertThrows(ItemNotFoundException.class, () -> {
            throw new ItemNotFoundException(itemId);
        });

        assertTrue(thrown.getMessage().contains(itemId), "Thrown exception should include the item identifier in the message.");
    }
}