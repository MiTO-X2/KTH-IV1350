package integration;

import model.Amount;

/**
 * Represents a register that tracks the store's accumulated revenue.
 */
public class Register {

    private static Register instance;
    private Amount amount;

    /**
     * Creates a new register initialized with a starting amount.
     *
     * @param amount The initial amount of cash in the register.
     */
    public Register(Amount amount) {
        this.amount = amount;
    }

    /**
     * Checks if an instance of a register is created and fetches it.
     * Otherwise, it creates a new instance.
     */
    public static Register getInstance(Amount initialAmount) {
        if (instance == null) {
            instance = new Register(initialAmount);
        }
        return instance;
    }

    /**
     * Updates the register by adding the total price of a completed sale.
     *
     * @param totalPrice The total sale price to add to the register.
     */
    public void updateRegister(Amount totalPrice) {
        this.amount = this.amount.add(totalPrice);
    }

    /**
     * Retrieves the amount of money from the register class.
     *
     * @return The total amount of money in the register
     */
    public Amount getAmount() {
        return amount;
    }
}