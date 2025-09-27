package model;

// Total-based discount
public class TotalBasedDiscountStrategy implements DiscountStrategy {
    private DiscountDatabase discountDatabase;

    /**
     * Constructs the discount strategy by taking the discount databas.
     * @param discountDatabase the discount database which implements different discount methods.
     */
    public TotalBasedDiscountStrategy(DiscountDatabase discountDatabase) {
        this.discountDatabase = discountDatabase;
    }

    /**
     * Calculates the discount of a sale by taking the whole price and discounting based on if the total price exceeds a certain limit.
     * For this example we set that if the total exceeds 500kr, the discount is 10%.
     * @param sale The current sale the discount will be applied on.
     * @return the price of the sale after discount.
     */
    @Override
    public Amount calculateDiscount(Sale sale) {
        Amount total = sale.calculateTotalPrice();
        Amount percentage = discountDatabase.getDiscountFromTotal(total);
        return new Amount(total.getAmount() * percentage.getAmount());
    }
}