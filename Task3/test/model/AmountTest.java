package test.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.Amount;

public class AmountTest {
    private Amount amount1;
    private Amount amount2;

    @BeforeEach
    void setUp() {
        amount1 = new Amount(100.0);
        amount2 = new Amount(50.0);
    }

    @AfterEach
    void tearDown() {
        amount1 = null;
        amount2 = null;
    }

    @Test
    void testGetAmount() {
        assertEquals(100.0, amount1.getAmount(), "Amount should be 100.0.");
    }

    @Test
    void testAdd() {
        Amount result = amount1.add(amount2);
        assertEquals(150.0, result.getAmount(), "Sum of 100.0 and 50.0 should be 150.0.");
    }

    @Test
    void testSubtract() {
        Amount result = amount1.subtract(amount2);
        assertEquals(50.0, result.getAmount(), "Difference of 100.0 and 50.0 should be 50.0.");
    }

    @Test
    void testMultiply() {
        Amount result = amount1.multiply(2);
        assertEquals(200.0, result.getAmount(), "100.0 multiplied by 2 should be 200.0.");
    }

    @Test
    void testToString() {
        assertEquals("100.00", amount1.toString(), "String representation should be '100.00'.");
    }

    @Test 
    void testSubtractionLeadingToNegativeResult() {
        Amount result = amount2.subtract(amount1);
        assertEquals(-50, result.getAmount(), "Difference of 50.0 and 100.0 should be -50.0.");
    }

    @Test 
    void testMultiplicationWithZero() {
        Amount result = amount1.multiply(0);
        assertEquals(0, result.getAmount(), "Multiplying by zero should return zero");
    }
}
