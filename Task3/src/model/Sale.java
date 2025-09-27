package model;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an ongoing sale containing multiple items and payment details.
 * 
 * Supports adding items, calculating totals, handling payments, and producing DTOs.
 */
public class Sale {
    private LocalDateTime timeAndDate;
    private List<Item> saleItems = new ArrayList<>();
    private Amount amountPaid;

    /**
     * Creates a new Sale instance with the current time.
     */
    public Sale() {
        this.timeAndDate = LocalDateTime.now();
    }

    private Item findItemByIdentifier(String itemIdentifier) {
        for (Item currentSaleItem : saleItems) {
            if (currentSaleItem.getIdentifier().equals(itemIdentifier)) {
                return currentSaleItem;
            }
        }
        return null;
    }

    private boolean addItemToSaleEdgeCaseCheck(ItemDTO itemDTO, int quantity) {
        if (quantity == 0) {
            return true;
        }

        if (((itemDTO.getQuantity() + quantity) <= 0) && quantity != Integer.MAX_VALUE) {
            return true;
        }

        return false;
    }

    /**
     * Adds a new item to the sale, or increases quantity if already registered.
     * 
     * @param itemDTO The item information.
     * @param quantity The number of units to add.
     * @return An updated ItemDTO.
     */
    public ItemDTO addItemToSale(ItemDTO itemDTO, int quantity) {
        if (addItemToSaleEdgeCaseCheck(itemDTO, quantity)) {
            return null;
        }

        Item itemFound = findItemByIdentifier(itemDTO.getItemIdentifier());
        if (itemFound != null) {
            itemFound.increaseQuantity(quantity);
            return itemFound.getDTO();
        }

        Item newItem = new Item(itemDTO, quantity);
        saleItems.add(newItem);
        return newItem.getDTO();
    }

    /**
     * Checks if an item with the specified identifier exists in the sale.
     * 
     * @param itemIdentifier The item identifier.
     * @return True if found, otherwise false.
     */
    public boolean checkIfItemRegistered(String itemIdentifier) {
        return findItemByIdentifier(itemIdentifier) != null;
    }

    /**
     * Increases the quantity of an already registered item.
     * 
     * @param itemIdentifier The item identifier.
     * @param quantity The quantity to add.
     * @return Updated ItemDTO.
     * @throws IllegalArgumentException if the item is not found.
     */
    public ItemDTO increaseQuantity(String itemIdentifier, int quantity) {
        Item item = findItemByIdentifier(itemIdentifier);
        if (item != null) {
            item.increaseQuantity(quantity);
            return item.getDTO();
        }

        return null;
    }
    
    /**
     * Retrieves an ItemDTO for a specific item in the sale.
     * 
     * @param itemIdentifier The identifier of the item.
     * @return The matching ItemDTO, or null if not found.
     */
    public ItemDTO getItemByIdentifier(String itemIdentifier) {
        Item item = findItemByIdentifier(itemIdentifier);
        return (item != null) ? item.getDTO() : null;
    }

    /**
     * Calculates the total price (including VAT) for all items in the sale.
     * 
     * @return The total amount.
     */
    public Amount calculateTotalPrice() {
        double total = 0;
        for (Item currentItem : saleItems) {
            total += currentItem.getTotalPriceWithVAT().getAmount();
        }
        return new Amount(total);
    }

    /**
     * Calculates the total VAT for all items in the sale.
     * 
     * @return Total VAT amount.
     */
    public Amount calculateTotalVAT() {
        double totalVAT = 0;
        for (Item currentItem : saleItems) {
            totalVAT += currentItem.getTotalVAT().getAmount();
        }
        return new Amount(totalVAT);
    }

    /**
     * Registers the amount paid by the customer.
     * 
     * @param paidAmount The payment amount.
     */
    public void pay(Amount paidAmount) {
        this.amountPaid = paidAmount;
    }

    /**
     * Calculates the change to return to the customer.
     * 
     * @return The change amount.
     */
    public Amount calculateChange() {
        double change = amountPaid.getAmount() - calculateTotalPrice().getAmount();
        return new Amount(change);
    }

    /**
     * Creates a SaleDTO summarizing the sale information.
     * 
     * @return A SaleDTO.
     */
    public SaleDTO getSaleInformation() {
        return new SaleDTO(getListOfItemDTOs(), calculateTotalPrice(), calculateTotalVAT());
    }

    /**
     * Creates a ReceiptDTO with detailed receipt information.
     * 
     * @return A ReceiptDTO.
     */
    public ReceiptDTO getReceiptInformation() {
        return new ReceiptDTO(
            timeAndDate,
            getListOfItemDTOs(),
            saleItems.size(),
            calculateTotalPrice(),
            calculateTotalVAT(),
            amountPaid,
            calculateChange()
        );
    }

    /**
     * Converts the list of Item objects into a list of ItemDTOs.
     * 
     * Used internally by SaleDTO and ReceiptDTO creation.
     * 
     * @return List of ItemDTOs.
     */
    public List<ItemDTO> getListOfItemDTOs() {
        List<ItemDTO> itemDTOs = new ArrayList<>();
        for (Item currentItem : saleItems) {
            itemDTOs.add(currentItem.getDTO());
        }
        return itemDTOs;
    }   
}
