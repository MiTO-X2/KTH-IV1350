package test.integration;

import integration.Printer;
import dto.ItemDTO;
import dto.ReceiptDTO;
import model.Amount;
import model.VAT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrinterTest {
    private Printer printer;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        printer = new Printer();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        printer = null;
        outputStream = null;
    }

    @Test
    void testPrintReceipt() {
        List<ItemDTO> soldItems = new ArrayList<>();
        soldItems.add(new ItemDTO("123", "Test Item 1", new Amount(100.0), VAT.VAT_25, 2, "Item One"));
        soldItems.add(new ItemDTO("456", "Test Item 2", new Amount(200.0), VAT.VAT_25, 1, "Item Two"));

        ReceiptDTO receiptDTO = new ReceiptDTO(
            LocalDateTime.of(2025, 5, 1, 12, 0),
            soldItems,
            soldItems.size(),
            new Amount(500.0),
            new Amount(100.0),
            new Amount(600.0),
            new Amount(100.0)
        );

        printer.printReceipt(receiptDTO);

        String printedOutput = outputStream.toString();
        assertTrue(printedOutput.contains("Begin receipt"), "Receipt should start with 'Begin receipt'.");
        assertTrue(printedOutput.contains("Total: 500.0"), "Receipt should include the total price.");
        assertTrue(printedOutput.contains("VAT: 100.0"), "Receipt should include the total VAT.");
        assertTrue(printedOutput.contains("Cash: 600.0"), "Receipt should include the amount paid.");
        assertTrue(printedOutput.contains("Change: 100.0"), "Receipt should include the change.");
        assertTrue(printedOutput.contains("End receipt"), "Receipt should end with 'End receipt'.");
        assertTrue(printedOutput.contains("Change to give the customer: 100.00"), "Receipt should include change back to customer.");
        
    }

    @Test
    void testPrintReceiptWithEmptyItemList() {
        List<ItemDTO> soldItems = new ArrayList<>();

        ReceiptDTO receiptDTO = new ReceiptDTO(
            LocalDateTime.of(2025, 5, 1, 12, 0),
            soldItems,
            0,
            new Amount(0.0),
            new Amount(0.0),
            new Amount(0.0),
            new Amount(0.0)
        );

        printer.printReceipt(receiptDTO);

        String printedOutput = outputStream.toString();
        assertTrue(printedOutput.contains("Begin receipt"), "Receipt should start with 'Begin receipt'.");
        assertTrue(printedOutput.contains("Total: 0.0"), "Total should be 0.0 for empty item list.");
        assertTrue(printedOutput.contains("End receipt"), "Receipt should end with 'End receipt'.");
    }
}