package dto;

import model.Amount;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO representing the receipt information after a completed sale.
 */
public class ReceiptDTO {
    private final LocalDateTime dateAndTime;
    private final List<ItemDTO> soldItems;
    private final int items;
    private final Amount totalPrice;
    private final Amount totalVAT;
    private final Amount amountPaid;
    private final Amount change;

    /**
     * Creates a new ReceiptDTO with specified attributes.
     *
     * @param dateAndTime The date and time of the sale.
     * @param items The number of items sold.
     * @param totalPrice The total price of the sale.
     * @param totalVAT The total VAT of the sale.
     * @param amountPaid The amount paid by the customer.
     * @param change The change given to the customer.
     */
    public ReceiptDTO(LocalDateTime dateAndTime, List<ItemDTO> itemDTOList, int items, Amount totalPrice, Amount totalVAT, 
                      Amount amountPaid, Amount change) {
        this.dateAndTime = dateAndTime;
        this.soldItems = Collections.unmodifiableList(itemDTOList == null ? Collections.emptyList() : new ArrayList<>(itemDTOList));
        this.items = items;
        this.totalPrice = totalPrice;
        this.totalVAT = totalVAT; 
        this.amountPaid = amountPaid;
        this.change = change;
    }

    /**
     * Gets the items contained in the Sale class.
     *
     * @return A list of all the items in the Sale class
     */
    public List<ItemDTO> getSoldItems() {
        return soldItems;
    }    

    /**
     * Gets the date and time of the sale.
     *
     * @return The date and time.
     */
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    /**
     * Gets the number of items sold.
     *
     * @return The number of items.
     */
    public int getItems() {
        return items;
    }

    /**
     * Gets the total price of the sale.
     *
     * @return The total price.
     */
    public Amount getTotalPrice() {
        return totalPrice;
    }

    /**
     * Gets the total VAT of the sale.
     *
     * @return The total VAT.
     */
    public Amount getTotalVAT() { 
        return totalVAT;
    }

    /**
     * Gets the amount paid by the customer.
     *
     * @return The amount paid.
     */
    public Amount getAmountPaid() {
        return amountPaid;
    }

    /**
     * Gets the change given to the customer.
     *
     * @return The change.
     */
    public Amount getChange() {
        return change;
    }
}
