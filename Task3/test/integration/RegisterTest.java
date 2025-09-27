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

        assertEquals(testAmount.getAmount(), testRegister.getAmount().getAmount(), 0.001,
            "Register should reflect updated total after sale.");
    }
}
