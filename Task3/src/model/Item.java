package model;

import dto.ItemDTO;

/**
 * Represents an item being sold in a sale transaction.
 * 
 * Each item has details like identifier, description, price, VAT, quantity, and name.
 */
public class Item {
    private String identifier;
    private String description;
    private Amount price;
    private VAT vatRate;
    private int quantity;
    private String name;

    /**
     * Creates a new Item based on a given ItemDTO and quantity.
     * 
     * @param itemDTO The DTO containing item details.
     * @param quantity The quantity of the item.
     */
    public Item(ItemDTO itemDTO, int quantity) {
        this.identifier = itemDTO.getItemIdentifier();
        this.description = itemDTO.getItemDescription();
        this.price = itemDTO.getPrice();
        this.vatRate = itemDTO.getVatRate();
        this.name = itemDTO.getName();
        this.quantity = quantity;
    }

    /**
     * Sets a new quantity for the item.
     * @param quantity The new quantity to set.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    } 

    /**
     * Increases the quantity of this item by a specified amount.
     *
     * @param amount The amount to add to the current quantity.
     */
    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    /**
     * Calculates and returns the price of this item including VAT.
     *
     * @return The price including VAT.
     */
    public Amount getPriceWithVAT() {
        double priceWithVAT = price.getAmount() * (1 + vatRate.getRate());
        return new Amount(priceWithVAT);
    }

    /**
     * Calculates the total price (with VAT) for all units of this item.
     * 
     * @return Total price with VAT.
     */
    public Amount getTotalPriceWithVAT() {
        double totalPriceWithVAT = getPriceWithVAT().getAmount() * quantity;
        return new Amount(totalPriceWithVAT);
    }

    /**
     * Calculates the total VAT amount for this item based on quantity.
     * 
     * @return Total VAT amount.
     */
    public Amount getTotalVAT() {
        double vatPerItem = price.getAmount() * vatRate.getRate();
        double totalVAT = vatPerItem * quantity;
        return new Amount(totalVAT);
    }   

    /**
     * Creates a DTO representation of this item.
     * 
     * @return An ItemDTO containing item information.
     */
    public ItemDTO getDTO() {
        return new ItemDTO(identifier, description, price, vatRate, quantity, name);
    }

    /**
     * Gets the unique identifier of this item.
     * @return The item identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the price (excluding VAT) of this item.
     * @return The price as an Amount.
     */
    public Amount getPrice() {
        return price;
    }

    /**
     * Gets the current quantity of this item.
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }
}
