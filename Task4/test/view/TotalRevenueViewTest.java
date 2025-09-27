package test.view;

import model.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.TotalRevenueView;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TotalRevenueViewTest {
    private TotalRevenueView revenueView;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        revenueView = new TotalRevenueView();
        System.setOut(new PrintStream(outContent)); 
    }

    @Test
    void testNewRevenue_displaysCorrectRevenue() {
        revenueView.newRevenue(new Amount(120.5));
        String output = outContent.toString().trim();
        assertTrue(output.contains("Total revenue: 120.50 SEK"), "Output should display the correct revenue.");
    }

    @Test
    void testNewRevenue_accumulatesCorrectly() {
        revenueView.newRevenue(new Amount(50));
        revenueView.newRevenue(new Amount(25));
        String output = outContent.toString().trim();

        assertTrue(output.contains("Total revenue: 50.00 SEK"), "Should display first revenue.");
        assertTrue(output.contains("Total revenue: 75.00 SEK"), "Should display accumulated revenue.");
    }

    @BeforeEach
    void resetOutputStream() {
        outContent.reset();
    }

    @org.junit.jupiter.api.AfterEach
    void restoreSystemOut() {
        System.setOut(originalOut);
    }
}