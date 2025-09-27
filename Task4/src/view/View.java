package view;

import controller.Controller;
import model.Amount;
import utils.LogHandler;
import dto.ItemDTO;
import exceptions.DatabaseFailureException;
import exceptions.ItemNotFoundException;

/** 
 * The View class represents the user interface part of the application.
 *
 * It simulates the actions of a cashier interacting with the system by
 * performing a sequence of method calls on the Controller. This
 * includes starting a sale, registering items, displaying running totals, 
 * ending the sale, and processing payment.
 * 
 * Exceptions are caught and logged if they occur.
 */
public class View {
    private Controller controller;

    /**
     * Creates a new View instance with a reference to Controller.
     * @param controller The application's controller, handling all system operations.
     */
    public View(Controller controller) {
        this.controller = controller;
        controller.addRevenueObserver(new TotalRevenueView());
        controller.addRevenueObserver(new TotalRevenueFileOutput());

        try {
            simulateSaleExecution();
        } catch (Exception e) {
            System.out.println("[FATAL ERROR] An unexpected error occurred. Please restart the system.");
            LogHandler.logException(e);
        }
    }

    /**
     * Simulates a series of operations in a sale. Adds items and concludes the sale.
     */
    private void simulateSaleExecution() {
        controller.initiateSale();
        System.out.println("Sale initiated.");

        String[] itemsToRegister = {"1", "1", "2", "3", "4", "1", "fail", "999", "DBFAIL"};
        for (String itemIdentifier : itemsToRegister) {
            registerItem(itemIdentifier);
            printRunningTotal();
        }

        System.out.println("\nEnd sale:");

        try {
            String customerID = "123"; 
            controller.applyDiscount(customerID);
        } catch (Exception e) {
            System.out.println("[USER MESSAGE] No discounts applied: " + e.getMessage());
        }

        Amount totalPrice = controller.endSale();
        printRunningDiscount();
        System.out.println("Total cost (incl VAT): " + String.format("%.2f", totalPrice.getAmount()));

        Amount payment = new Amount(1000);
        controller.concludeSale(payment);
    }

    /**
     * Simulates registering a single item during a sale.
     * Retrieves item details from the controller and displays them.
     *
     * @param itemIdentifier The identifier of the item to register.
     */
    private void registerItem(String itemIdentifier) {
        try {
        ItemDTO itemDTO = controller.registerItem(itemIdentifier);

        System.out.println("\nItem added:");
        System.out.println("Item ID: " + itemDTO.getItemIdentifier());
        System.out.println("Item name: " + itemDTO.getName());
        System.out.println("Item cost: " + String.format("%.2f", itemDTO.getPrice().getAmount()) + " SEK");
        System.out.println("VAT: " + (itemDTO.getVatRate().getRate() * 100) + "%");
        System.out.println("Item description: " + itemDTO.getItemDescription());

        } catch (ItemNotFoundException e) {
            System.out.println("[USER MESSAGE] The item with ID '" + itemIdentifier + "' was not found. Please check the ID and try again.");
        } catch (DatabaseFailureException e) {
            System.out.println("[USER MESSAGE] There is a technical issue accessing the database. Please contact support.");
            LogHandler.logException(e);
        } catch (IllegalArgumentException e) {
            System.out.println("[USER MESSAGE] Failed to register item: The provided item information is invalid. Please check the input and try again.");
            LogHandler.logException(e);
        }
    }

    /**
     * Prints the current running total cost and total VAT
     * after an item has been registered.
     */
    private void printRunningTotal() {
        Amount runningTotal = controller.getRunningTotal();
        Amount runningVAT = controller.getRunningVAT();   
        System.out.println("\nTotal cost (incl VAT): " + String.format("%.2f", runningTotal.getAmount()) + " SEK");
        System.out.println("Total VAT: " + String.format("%.2f", runningVAT.getAmount()) + " SEK");
    }

    /**
     * Prints the running discount of the current sale up to the latest item added.
     */
    private void printRunningDiscount() {
        Amount discount = controller.getRunningDiscount();
        System.out.println("Discount applied: " + String.format("%.2f", discount.getAmount()) + " SEK");
    }
}