package view;

/**
 * Displays total revenue on the user interface (console).
 */
public class TotalRevenueView extends AbstractRevenueObserver {
    
    /**
     * Displays the current total revenue to the console.
     * This method is called internally when new revenue is reported.
     */
    @Override
    protected void doShowTotalIncome() {
        System.out.println("[TOTAL INCOME - VIEW] Total revenue: " 
            + String.format("%.2f", totalRevenue.getAmount()) + " SEK");
    }

    /**
     * Handles any exceptions that occur during the display of total revenue.
     * For this simple console-based view, errors are printed directly to the console.
     * 
     * @param e The exception that occurred.
     */
    @Override
    protected void handleErrors(Exception e) {
        System.out.println("[ERROR] Unable to display total revenue: " + e.getMessage());
    }
}