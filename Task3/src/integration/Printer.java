package integration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dto.ItemDTO;
import dto.ReceiptDTO;
import model.Amount;

/**
 * Simulates a printer that prints a receipt after a sale is completed.
 */
public class Printer {
    
    /**
     * Prints a formatted receipt using the provided receipt information.
     *
     * @param receiptDTO The receipt information to be printed.
     */
    public void printReceipt(ReceiptDTO receiptDTO) {
        printReceiptDetails(receiptDTO);
    }

    private void printReceiptDetails(ReceiptDTO receiptDTO) {
        System.out.println("------------------------ Begin receipt ------------------------");
        
        LocalDateTime dateAndTime = receiptDTO.getDateAndTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateAndTime = dateAndTime.format(formatter);
        
        System.out.println("Time of Sale: " + formattedDateAndTime);
        System.out.println();

        for (ItemDTO itemDTO : receiptDTO.getSoldItems()) {
                String name = itemDTO.getName();
                int quantity = itemDTO.getQuantity();
                Amount price = itemDTO.getPrice();
                Amount totalItemPrice = price.multiply(quantity);  

                System.out.printf("%s %d x %.2f %.2f SEK%n", name, quantity, price.getAmount(), totalItemPrice.getAmount());
        }

        System.out.println();
        System.out.println("Total: " + receiptDTO.getTotalPrice());
        System.out.println("VAT: " + receiptDTO.getTotalVAT());
        System.out.println();
        System.out.println("Cash: " + receiptDTO.getAmountPaid());
        System.out.println("Change: " + receiptDTO.getChange());
        System.out.println("------------------------ End receipt ------------------------");
        System.out.println();
        System.out.println("Change to give the customer: " + receiptDTO.getChange());
    }
}


