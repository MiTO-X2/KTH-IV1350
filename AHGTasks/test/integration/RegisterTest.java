package test.integration;

import integration.Register;
import model.Amount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class RegisterTest {
    private Register testRegister;
    private Amount testAmount;

    @BeforeEach
    void setUp() {
        testRegister = new Register(new Amount(100.0));
        testAmount = new Amount(150.0);
    }

    @AfterEach
    void tearDown() {
        testRegister = null;
        testAmount = null;
    }

    @Test
    void testUpdateRegisterWithoutReflection() {
        testRegister.updateRegister(new Amount(50.0));

        assertEquals(testAmount.getAmount(), testRegister.getAmount().getAmount(), 
            "Register should reflect updated total after sale.");
    }

    @Test
    void testGetAmountReturnsInitialAmount() {
        Amount initialAmount = new Amount(100.0);
        assertEquals(initialAmount.getAmount(), testRegister.getAmount().getAmount(), 
            "getAmount should return the register's initial value.");
    }

    @Test
    void testUpdateRegisterAddsCorrectly() {
        testRegister.updateRegister(new Amount(25.0));
        assertEquals(125.0, testRegister.getAmount().getAmount(),
            "Register should add the given amount correctly.");
    }

    @Test
    void testMultipleUpdatesAccumulateAmount() {
        testRegister.updateRegister(new Amount(30.0));
        testRegister.updateRegister(new Amount(20.0));
        assertEquals(150.0, testRegister.getAmount().getAmount(),
            "Register should reflect the total after multiple updates.");
    }

    @Test
    void testUpdateRegisterWithZeroAmountDoesNotChangeTotal() {
        testRegister.updateRegister(new Amount(0));
        assertEquals(100.0, testRegister.getAmount().getAmount(), "Register amount should remain unchanged when updating with zero.");
    }

    @Test
    void testUpdateRegisterWithNegativeAmount() {
        testRegister.updateRegister(new Amount(-50.0));
        assertEquals(50.0, testRegister.getAmount().getAmount(), 
            "Register should subtract correctly when negative amount is used.");
    }
}