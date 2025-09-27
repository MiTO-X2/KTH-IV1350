package model;

// Customer-based percentage discount
public class CustomerDiscountStrategy implements DiscountStrategy {
    private String customerID;
    private DiscountDatabase discountDatabase;

    /**
     * Creates a class which efficiently stores the customer ID with the discount they have. 
     * 
     * @param customerID Is the customerID which we are finding a discount for
     * @param discountDatabase Is the database were discounts are stored
     */
    public CustomerDiscountStrategy(String customerID, DiscountDatabase discountDatabase) {
        this.customerID = customerID;
        this.discountDatabase = discountDatabase;
    }

    /**
     * Calculates discount using the customer discount strategy, taking the total price from a sale,
     * and removing a percentage of the price based on the customers discount,
     * 
     * @param sale Is the current sale we are deducting the price from.
     * @return The total price after the customers discount.
     */
    @Override
    public Amount calculateDiscount(Sale sale) {
        String id = sale.getCustomerID();
        if (id == null || discountDatabase == null) {
            return new Amount(0);
        }

        Amount total = sale.calculateTotalPrice();
        Amount percentage = discountDatabase.getDiscountFromCustomerID(id);
        return new Amount(total.getAmount() * percentage.getAmount());
    }
}