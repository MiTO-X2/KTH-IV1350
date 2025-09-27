package controller;

import integration.ExternalInventorySystem;
import integration.ExternalAccountingSystem;
import integration.Register;
import integration.Printer;
import model.Sale;
import model.Amount;
import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;

/**
 * This class acts as the application's Controller in the MVC architecture.
 * It coordinates actions between the View and the Model layer,
 * managing system operations such as sale initiation, item registration,
 * payment processing, and updates to external systems like accounting and inventory.
 */
public class Controller {
    private ExternalInventorySystem inventorySystem;
    private ExternalAccountingSystem accountingSystem;
    private Register register;
    private Printer printer;
    private Sale sale;

    /**
     * Creates a new Controller instance and initializes the external systems
     * (inventory system, accounting system, register, and printer).
     */
    public Controller() {
        this.inventorySystem = new ExternalInventorySystem();
        this.accountingSystem = new ExternalAccountingSystem();
        this.register = new Register(new Amount(0));
        this.printer = new Printer();
    }

    /**
     * Initiates a new sale by creating a new Sale instance.
     * This must be called before registering any items.
     */
    public void initiateSale() {
        this.sale = new Sale();
    }

    /**
     * Registers an item during an ongoing sale, based on the item's identifier.
     * If the item is already part of the sale, its quantity is increased;
     * otherwise, the item is added as a new sale item.
     *
     * @param itemIdentifier The unique identifier for the item to register.
     * @return An ItemDTO representing the registered or updated item.
     */
    public ItemDTO registerItem(String itemIdentifier) {
        boolean itemAlreadyRegistered = sale.checkIfItemRegistered(itemIdentifier);
        
        if (itemAlreadyRegistered) {
            sale.increaseQuantity(itemIdentifier, 1);
            return sale.getItemByIdentifier(itemIdentifier);
        }

        ItemDTO itemDTO = inventorySystem.retrieveItemInformation(itemIdentifier);
        return sale.addItemToSale(itemDTO, 1);
    }

    /**
     * Finalizes the item registration phase of the sale
     * and calculates the total price, including VAT.
     *
     * @return The total price for the current sale.
     */
    public Amount endSale() {
        Amount totalAmount = sale.calculateTotalPrice();
        return totalAmount;
    }

    /**
     * Processes the customer's payment, calculates the change,
     * and updates the external systems (register, accounting, inventory).
     * Also prints a receipt for the customer.
     *
     * @param payment The amount paid by the customer.
     * @return An Amount representing the change to be returned to the customer.
     */
    public Amount concludeSale(Amount payment) {
        Amount paymentAmount = payment;
        sale.pay(paymentAmount);
        Amount totalAmount = sale.calculateTotalPrice();
        Amount change = paymentAmount.subtract(totalAmount);

        SaleDTO saleDTO = sale.getSaleInformation();
        ReceiptDTO receiptDTO = sale.getReceiptInformation();

        register.updateRegister(totalAmount);
        accountingSystem.updateAccountingSystem(paymentAmount);
        inventorySystem.updateInventory(saleDTO);
        printer.printReceipt(receiptDTO);

        return change;
    }

    /**
     * Returns the current running total price (including VAT)
     * for all items registered so far during the ongoing sale.
     *
     * @return An Amount representing the current total price.
     */
    public Amount getRunningTotal() {
        return sale.calculateTotalPrice();
    }

    /**
     * Returns the running total amount of VAT
     * for all items registered so far during the ongoing sale.
     *
     * @return An Amount representing the current total VAT.
     */
    public Amount getRunningVAT() {
        return sale.calculateTotalVAT();
    }
}