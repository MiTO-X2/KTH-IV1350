package test.model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class CompositeDiscountStrategyTest {
    private Sale mockSale;

    @BeforeEach
    void setUp() {
        mockSale = new Sale(); 
    }

    @Test
    void testCompositeWithNoStrategiesReturnsZeroDiscount() {
        CompositeDiscountStrategy composite = new CompositeDiscountStrategy(Collections.emptyList());
        Amount discount = composite.calculateDiscount(mockSale);

        assertEquals(0.0, discount.getAmount(), 
            "Composite discount with no strategies should return zero.");
    }

    @Test
    void testCompositeWithOneStrategyReturnsThatDiscount() {
        DiscountStrategy fixedStrategy = sale -> new Amount(10.0);
        CompositeDiscountStrategy composite = new CompositeDiscountStrategy(Collections.singletonList(fixedStrategy));

        Amount discount = composite.calculateDiscount(mockSale);
        assertEquals(10.0, discount.getAmount(), 
            "Composite discount with one strategy should equal that strategy's discount.");
    }

    @Test
    void testCompositeWithMultipleStrategiesSumsDiscounts() {
        DiscountStrategy strategy1 = sale -> new Amount(5.0);
        DiscountStrategy strategy2 = sale -> new Amount(7.5);
        DiscountStrategy strategy3 = sale -> new Amount(2.5);

        CompositeDiscountStrategy composite = new CompositeDiscountStrategy(
            Arrays.asList(strategy1, strategy2, strategy3)
        );

        Amount discount = composite.calculateDiscount(mockSale);
        assertEquals(15.0, discount.getAmount(), 
            "Composite discount should be the sum of all strategy discounts.");
    }
}