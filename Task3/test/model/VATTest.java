package test.model;

import model.VAT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VATTest {
    private VAT vat;
    
    @AfterEach
    void tearDown() {
        vat = null;
    }   

    @Test
    void testGetPercentage() {
        vat = VAT.VAT_6;
        assertEquals(6, vat.getPercentage(), "Something is wrong with the definition of VAT at 6%");

        vat = VAT.VAT_12;
        assertEquals(12, vat.getPercentage(), "Something is wrong with the definition of VAT at 12%");

        vat = VAT.VAT_25;
        assertEquals(25, vat.getPercentage(), "Something is wrong with the definition of VAT at 25%");
    }

    @Test 
    void testGetRate() {
        assertEquals(0.25, VAT.VAT_25.getRate(), "getRate should return the correct VAT rate");
    }
}
