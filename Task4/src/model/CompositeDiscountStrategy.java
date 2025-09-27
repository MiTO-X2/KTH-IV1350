package model;

import java.util.List;

/**
 * CompositeDiscountStrategy allows combining multiple DiscountStrategy
 * implementations. Each strategy is applied to the given Sale, and
 * their individual discounts are summed up to compute the total discount.
 */
public class CompositeDiscountStrategy implements DiscountStrategy {
    private List<DiscountStrategy> strategies;

    /**
     * Constructs a CompositeDiscountStrategy with the specified list of
     * DiscountStrategy objects.
     *
     * @param strategies the list of discount strategies to apply.
     */
    public CompositeDiscountStrategy(List<DiscountStrategy> strategies) {
        this.strategies = strategies;
    }


    /**
     * Calculates the total discount by summing the discounts from each strategy
     * in the list.
     *
     * @param sale current sale to which the discount will be applied
     * @return an Amount representing the total discount
     */
    @Override
    public Amount calculateDiscount(Sale sale) {
        double totalDiscount = 0;
        for (DiscountStrategy strategy : strategies) {
            totalDiscount += strategy.calculateDiscount(sale).getAmount();
        }
        return new Amount(totalDiscount);
    }
}