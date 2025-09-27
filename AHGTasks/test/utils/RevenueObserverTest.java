package test.utils;

import utils.RevenueObserver;
import model.Amount;

/**
 * A test implementation of RevenueObserver that records the latest revenue it receives.
 */
public class RevenueObserverTest implements RevenueObserver {
    private Amount latestRevenue;

    @Override
    public void newRevenue(Amount revenue) {
        this.latestRevenue = revenue;
    }

    public Amount getLatestRevenue() {
        return latestRevenue;
    }
}