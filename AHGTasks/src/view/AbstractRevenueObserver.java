package view;

import model.Amount;
import utils.RevenueObserver;

/**
 * An abstract base class implementing {@link RevenueObserver} to track and display revenue updates.
 * 
 * This class maintains the total accumulated revenue and the last revenue update.
 * Subclasses are required to define how the total income is displayed and how errors are handled during display.
 */
public abstract class AbstractRevenueObserver implements RevenueObserver {
    protected Amount totalRevenue = new Amount(0);
    protected Amount lastRevenue = new Amount(0);

    /**
     * Called when new revenue is reported.
     * Updates the last revenue and recalculates the total revenue.
     * Then attempts to display the updated total revenue.
     * 
     * @param revenue The new revenue amount to process.
     */
    @Override
    public void newRevenue(Amount revenue) {
        this.lastRevenue = revenue;
        calculateTotalIncome(revenue);
        showTotalIncome();
    }

    /**
     * Attempts to show the total income by calling the subclass-defined display method.
     * Catches and handles any exceptions by delegating to the subclass-defined error handler.
     */
    private void showTotalIncome() {
        try {
            doShowTotalIncome();
        } catch (Exception e) {
            handleErrors(e);
        }
    }

    /**
     * Abstract method to be implemented by subclasses to define how the total income should be displayed.
     * 
     * @throws Exception If displaying the total income fails.
     */
    protected abstract void doShowTotalIncome() throws Exception;

    /**
     * Abstract method to be implemented by subclasses to define how errors during displaying total income should be handled.
     * 
     * @param e The exception encountered during display.
     */
    protected abstract void handleErrors(Exception e);

    /**
     * Updates the total accumulated revenue by adding the new revenue.
     * 
     * @param revenue The amount of revenue to add to the total.
     */
    protected void calculateTotalIncome(Amount revenue) {
        totalRevenue = totalRevenue.add(revenue);
    }
}