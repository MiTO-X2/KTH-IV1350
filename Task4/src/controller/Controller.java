package controller;

import integration.ExternalInventorySystem;
import integration.ExternalAccountingSystem;
import integration.Register;
import integration.Printer;
import model.Sale;
import model.TotalBasedDiscountStrategy;
import utils.RevenueObserver;
import model.Amount;
import model.CompositeDiscountStrategy;
import model.CustomerDiscountStrategy;
import model.DiscountDatabase;
import model.DiscountDatabaseImpl;
import model.DiscountStrategy;
import model.ItemBasedDiscountStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dto.ItemDTO;
import dto.ReceiptDTO;
import dto.SaleDTO;
import exceptions.DatabaseFailureException;
import exceptions.ItemNotFoundException;

/**
 * This class acts as the application's Controller in the MVC architecture.
 * It coordinates actions between the View and the Model layer,
 * managing system operations such as sale initiation, item registration,
 * payment processing, and updates to external systems like accounting and inventory.
 */
public class Controller {
    private List<RevenueObserver> revenueObservers = new ArrayList<>();
    private ExternalInventorySystem inventorySystem;
    private ExternalAccountingSystem accountingSystem;
    private Register register;
    private Printer printer;
    private Sale sale;
    private DiscountStrategy discountStrategy;
    private DiscountDatabase discountDatabase;

    /**
     * Creates a new Controller instance and initializes the external systems
     * (inventory system, accounting system, register, and printer).
     */
    public Controller() {
        this.inventorySystem = ExternalInventorySystem.getInstance();
        this.accountingSystem = ExternalAccountingSystem.getInstance();
        this.register = Register.getInstance(new Amount(0));
        this.printer = Printer.getInstance();
        this.discountDatabase = new DiscountDatabaseImpl();
    }

    /**
     * Initiates a new sale by creating a new Sale instance.
     * This must be called before registering any items.
     */
    public void initiateSale() {
        this.sale = new Sale();

        for (RevenueObserver observer : revenueObservers) {
            sale.addRevenueObserver(observer);
        }
    }

    /**
     * Registers an item during an ongoing sale, based on the item's identifier.
     * If the item is already part of the sale, its quantity is increased;
     * otherwise, the item is added as a new sale item.
     *
     * @param itemIdentifier The unique identifier for the item to register.
     * @return An ItemDTO representing the registered or updated item.
     * @throws ItemNotFoundException occurs when an item ID is not found in the inventory.
     * @throws DatabaseFailureException is thrown when a database connection issue occurs. This is an example implementation,
     * this is only thrown when a specific item ID is checked.
     */
    public ItemDTO registerItem(String itemIdentifier) throws ItemNotFoundException, DatabaseFailureException {
        boolean itemAlreadyRegistered = sale.checkIfItemRegistered(itemIdentifier);

        if (itemAlreadyRegistered) {
            sale.increaseQuantity(itemIdentifier, 1);
        } else {
            ItemDTO itemDTO = inventorySystem.retrieveItemInformation(itemIdentifier);
            sale.addItemToSale(itemDTO, 1);
        }

        if (discountStrategy != null) {
            sale.calculateDiscounts(discountStrategy);
        }

        return sale.getItemByIdentifier(itemIdentifier);

    }

    /**
     * Finalizes the item registration phase of the sale
     * and calculates the total price, including VAT.
     *
     * @return The total price for the current sale.
     */
    public Amount endSale() {
        return sale.calculateTotalPriceAfterDiscount();
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
        Amount totalAmount = sale.calculateTotalPriceAfterDiscount();
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
     * Applies discount using various strategies, based on customer and other factors.
     * 
     * @param customerID A string which identifies the buyer, so the appropriate discount can be applied to the sale.
     */
    public void applyDiscount(String customerID) {
        DiscountStrategy discount = new CompositeDiscountStrategy(Arrays.asList(
            new CustomerDiscountStrategy(customerID, discountDatabase),
            new ItemBasedDiscountStrategy(discountDatabase),
            new TotalBasedDiscountStrategy(discountDatabase)
        ));
        setDiscountStrategy(discount);
        sale.setCustomerID(customerID); 
        sale.calculateDiscounts(discount);
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

    /**
     * Returns the running discount of the current sale for all items registered so far 
     * 
     * @return An Amount representing the discount for the sale as of yet.
     */
    public Amount getRunningDiscount() {
        return sale.getTotalDiscount();
    }

    /**
     * A function which applies the strategy which will be used for the current sale. 
     * 
     * @param discountStrategy One of the available strategies to calculate the discount. 
     */
    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    /**
     * A view which checks when the sale is complete to update the systems revenue counter.
     * 
     * @param observer A view for when the sale ends.
     */
    public void addRevenueObserver(RevenueObserver observer) {
        revenueObservers.add(observer);
        if (sale != null) {
            sale.addRevenueObserver(observer);
        }
    }
}