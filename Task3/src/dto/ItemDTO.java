package dto;

import model.Amount;
import model.VAT;

/**
 * DTO containing information about an item being sold.
 */
public class ItemDTO {
    private final String itemIdentifier;
    private final String itemDescription;
    private final Amount price;
    private final VAT vatRate;
    private final int quantity;
    private final String name;

    /**
     * Creates a new ItemDTO with specified attributes.
     *
     * @param itemIdentifier The unique identifier of the item.
     * @param itemDescription A description of the item.
     * @param price The price of the item.
     * @param vatRate The VAT rate for the item.
     * @param name The name of the item.
     */
    public ItemDTO(String itemIdentifier, String itemDescription, Amount price, VAT vatRate, int quantity, String name) {
        this.itemIdentifier = itemIdentifier;
        this.itemDescription = itemDescription;
        this.price = price;
        this.vatRate = vatRate;
        this.quantity = quantity;
        this.name = name;
    }

    /**
     * Gets the unique identifier of the item.
     *
     * @return The item identifier.
     */
    public String getItemIdentifier() {
        return itemIdentifier;
    }

    /**
     * Gets the description of the item.
     *
     * @return The item description.
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Gets the price of the item.
     *
     * @return The price.
     */
    public Amount getPrice() {
        return price;
    }

    /**
     * Gets the VAT rate of the item.
     *
     * @return The VAT rate.
     */
    public VAT getVatRate() {
        return vatRate;
    }

    
    /**
     * Gets the quantity of the item.
     *
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the name of the item.
     *
     * @return The name of the item.
     */
    public String getName() {
        return name;
    }
}
