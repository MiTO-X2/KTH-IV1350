package exceptions;

/**
 * Thrown when an item with the given identifier is not found in the inventory.
 */
public class ItemNotFoundException extends Exception {
    private final String itemIdentifier;

    /**
     * Throws an exception when an item ID can't be found in the inventory system.
     * @param itemIdentifier the ID of the item which wasn't found.
     */
    public ItemNotFoundException(String itemIdentifier) {
        super("Item with ID '" + itemIdentifier + "' was not found in the inventory.");
        this.itemIdentifier = itemIdentifier;
    }

    /**
     * 
     * Gets the item identifier that caused the exception.
     * @return The ID of the item that was not found.
     */
    public String getItemIdentifier() {
        return itemIdentifier;
    }
}