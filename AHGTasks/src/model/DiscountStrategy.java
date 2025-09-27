package model;

/**
 * Interface representing a discount calculation strategy.
 * Different discount algorithms implement this interface to
 * provide custom discount calculation logic based on the Sale.
 */
public interface DiscountStrategy {
    /**
     * Calculates the discount amount for the given sale.
     *
     * @param sale The sale instance for which the discount is calculated.
     * @return The discount amount to be applied to the sale.
     */
    Amount calculateDiscount(Sale sale);
}