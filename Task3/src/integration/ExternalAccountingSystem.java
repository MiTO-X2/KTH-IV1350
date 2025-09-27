package integration;

import model.Amount;

/**
 * Represents an interface to the external accounting system.
 * Used to update the accounting system with completed payments from sales.
 */
public class ExternalAccountingSystem {
    
    /**
     * Sends the payment information to the external accounting system.
     *
     * @param payment The total amount paid for the sale.
     */
    public void updateAccountingSystem(Amount payment) {
        System.out.println("Accounting system updated with payment: " + payment);
    }
}