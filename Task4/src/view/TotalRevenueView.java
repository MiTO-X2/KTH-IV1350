package view;

import model.Amount;
import utils.RevenueObserver;

/**
 * Displays total revenue on the user interface (console).
 */
public class TotalRevenueView implements RevenueObserver {
    private Amount totalRevenue = new Amount(0);

    /**
     * The function takes previous revenue and adds it with the revenue of the latest sale, then displays it on the interface.
     * @param revenue The amount of money which the checkout machine hase earned previously.
     */
    @Override
    public void newRevenue(Amount revenue) {
        totalRevenue = totalRevenue.add(revenue);
        showRevenue();
    }

    /**
     * A function which displays the total revenue which has been earned by the checkout maschine from the previous and current sale.
     */
    private void showRevenue() {
        System.out.println("[TOTAL INCOME - VIEW] Total revenue: " 
            + String.format("%.2f", totalRevenue.getAmount()) + " SEK");
    }
}