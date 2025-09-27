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

    @org.junit.jupiter.api.AfterEach
    void restoreSystemOut() {
        System.setOut(originalOut);
    }

    @BeforeEach
    void resetOutputStream() {
        outContent.reset();
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

    @Test
    void testHandleErrors_displaysErrorMessage() {
        class TestableRevenueView extends TotalRevenueView {
            public void callHandleErrors(Exception e) {
                super.handleErrors(e);
            }
        }

        TestableRevenueView testView = new TestableRevenueView();
        testView.callHandleErrors(new Exception("Simulated failure"));

        String output = outContent.toString().trim();
        assertTrue(output.contains("[ERROR] Unable to display total revenue: Simulated failure"),
                "Output should show the error message passed to handleErrors.");
    }
}