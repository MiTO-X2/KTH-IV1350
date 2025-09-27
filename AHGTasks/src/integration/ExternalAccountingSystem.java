package integration;

import model.Amount;

/**
 * Represents an interface to the external accounting system.
 * Used to update the accounting system with completed payments from sales.
 */
public class ExternalAccountingSystem {
    private static ExternalAccountingSystem instance;

    private ExternalAccountingSystem() {
    }

    /**
     * Checks if an instance of the external accounting system is created and fetches it.
     * Otherwise, it creates a new instance.
     */
    public static ExternalAccountingSystem getInstance() {
        if (instance == null) {
            instance = new ExternalAccountingSystem();
        }
        return instance;
    }

    /**
     * Sends the payment information to the external accounting system.
     *
     * @param payment The total amount paid for the sale.
     */
    public void updateAccountingSystem(Amount payment) {
        System.out.println("Accounting system updated with payment: " + payment);
    }
}