package model;

import java.util.Locale;

/**
 * Represents a monetary amount with operations for basic arithmetic.
 * 
 * This class encapsulates a money value and provides methods
 * for adding, subtracting, and multiplying amounts.
 */
public class Amount {
    private double amount;

    /**
     * Creates a new Amount with a specified value.
     * 
     * @param amount The monetary value.
     */
    public Amount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the numeric value of this amount.
     * 
     * @return The monetary value.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Adds another Amount to this one and returns the result as a new Amount.
     * 
     * @param other The other amount to add.
     * @return A new Amount representing the sum.
     */
    public Amount add(Amount other) {
        return new Amount(this.amount + other.amount);
    }

    /**
     * Subtracts another Amount from this one and returns the result as a new Amount.
     * 
     * @param other The other amount to subtract.
     * @return A new Amount representing the difference.
     */
    public Amount subtract(Amount other) {
        return new Amount(this.amount - other.amount);
    }

    /**
     * Multiplies this amount by a factor and returns the result as a new Amount.
     * 
     * @param factor The multiplier.
     * @return A new Amount representing the product.
     */
    public Amount multiply(double factor) {
        return new Amount(this.amount * factor);
    }

    /**
     * Returns a string representation of the amount with two decimals.
     */
    @Override
    public String toString() {
        return String.format(Locale.US,"%.2f", amount);
    }
}