package model;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import utils.RevenueObserver;

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
    private Amount totalDiscount = new Amount(0);
    private String customerID;
    private List<RevenueObserver> revenueObservers = new ArrayList<>();

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
     * @throws IllegalArgumentException if quantity is invalid or less than allowed.
     */
    public ItemDTO addItemToSale(ItemDTO itemDTO, int quantity) {
        if (addItemToSaleEdgeCaseCheck(itemDTO, quantity)) {
            throw new IllegalArgumentException("Invalid quantity: " + quantity + " for item: " + itemDTO.getItemIdentifier());
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
        if (item == null) {
            throw new IllegalArgumentException("Item with identifier " + itemIdentifier + " not found in the sale.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to increase must be positive.");
        }
        item.increaseQuantity(quantity);
        return item.getDTO();
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
     * Calculates the total price including VAT and subtracting any discounts.
     * 
     * @return Final total after discounts.
     */
    public Amount calculateTotalPriceAfterDiscount() {
        return calculateTotalPrice().subtract(getTotalDiscount());
    }


    /**
     * Calculates and applies discounts to the current sale using the provided discount strategy.
     * If no strategy is provided (null), no discount is applied and the total discount is set to zero.
     *
     * @param discountStrategy The discount strategy to use for calculation.
     */
    public void calculateDiscounts(DiscountStrategy discountStrategy) {
        if (discountStrategy != null) {
            this.totalDiscount = discountStrategy.calculateDiscount(this);
        } else {
            this.totalDiscount = new Amount(0);
        }
    }

    /**
     * Registers the amount paid by the customer.
     * 
     * @param paidAmount The payment amount.
     * @throws IllegalArgumentException is thrown if the customer gives a payment which is less than the total price.
     */
    public void pay(Amount paidAmount) {
        Amount totalPriceAfterDiscount = calculateTotalPriceAfterDiscount();
        if (paidAmount.getAmount() < totalPriceAfterDiscount.getAmount()) {
            throw new IllegalArgumentException("Payment is less than the total price. Payment rejected.");
        }
        this.amountPaid = paidAmount;

        notifyObservers(totalPriceAfterDiscount);
    }

    /**
     * Calculates the change to return to the customer.
     * 
     * @return The change amount.
     */
    public Amount calculateChange() {
        double change = amountPaid.getAmount() - calculateTotalPriceAfterDiscount().getAmount();
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
            getTotalDiscount(),
            calculateTotalPriceAfterDiscount(),
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

    /**
     * Returns the list of items in the current sale.
     * This is used for applying item-based discounts.
     *
     * @return List of Item objects.
     */
    public List<Item> getItems() {
        return new ArrayList<>(saleItems);
    }

    /**
     * Gets the total discount of the sale.
     * @return an amount which is the value of the total sale.
     */
    public Amount getTotalDiscount() {
        return totalDiscount != null ? totalDiscount : new Amount(0);
    }

    /**
     * Sets the customer ID of the current sale.
     * @param customerID the ID of the customer of the current sale.
     */
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the current ID of the customer of this sale.
     * @return customerID.
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * A view which checks when the sale is complete to update the systems revenue counter.
     * 
     * @param observer A view for when the sale ends.
     */
    public void addRevenueObserver(RevenueObserver observer) {
        revenueObservers.add(observer);
    }

    /**
     * A function which gives the revenue values for the observer views.
     * 
     * @param revenue The amount of revenue which is sent to the observer to be displayed (example, total revenue of a sale)
     */
    private void notifyObservers(Amount revenue) {
        for (RevenueObserver observer : revenueObservers) {
            observer.newRevenue(revenue);
        }
    }
}