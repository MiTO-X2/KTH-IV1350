package dto;

import model.Amount;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO representing an ongoing sale.
 */
public class SaleDTO {
    private final List<ItemDTO> items;
    private final Amount runningTotal;
    private final Amount totalVAT; 

    /**
     * Creates a new SaleDTO with specified attributes.
     *
     * @param items The list of items in the sale.
     * @param runningTotal The current running total cost.
     * @param totalVAT The total VAT of the ongoing sale.
     */
    public SaleDTO(List<ItemDTO> itemDTOList, Amount runningTotal, Amount totalVAT) {
        this.items = Collections.unmodifiableList(new ArrayList<>(itemDTOList));
        this.runningTotal = runningTotal;
        this.totalVAT = totalVAT;
    }

    /**
     * Gets the list of items in the sale.
     *
     * @return The list of items.
     */
    public List<ItemDTO> getItems() {
        return items;
    }

    /**
     * Gets the running total cost of the sale.
     *
     * @return The running total.
     */
    public Amount getRunningTotal() {
        return runningTotal;
    }

    /**
     * Gets the total VAT of the ongoing sale.
     *
     * @return The total VAT.
     */
    public Amount getTotalVAT() {
        return totalVAT;
    }
}