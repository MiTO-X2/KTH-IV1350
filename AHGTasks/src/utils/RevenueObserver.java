package utils;

import model.Amount;

/**
 * Observer interface for receiving notifications when a sale is completed.
 */
public interface RevenueObserver {
    /**
     * Called when a sale is completed and revenue should be updated.
     *
     * @param revenue The revenue from the latest sale.
     */
    void newRevenue(Amount revenue);
}