package test.model;

import dto.ItemDTO;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TotalBasedDiscountStrategyTest {
    private TotalBasedDiscountStrategy discountStrategy;
    private Sale testSale;
    private ItemDTO testItemDTO;

    @BeforeEach
    void setUp() {
        DiscountDatabase discountDatabase = new DiscountDatabaseImpl();
        discountStrategy = new TotalBasedDiscountStrategy(discountDatabase);

        testSale = new Sale();

        testItemDTO = new ItemDTO(
            "123",
            "Test Item",
            new Amount(200.0),
            VAT.VAT_6,
            1,
            "Sample Item"
        );
    }

    @Test
    void testCalculateDiscount_totalBelowThreshold_returnsZeroDiscount() {
        testSale.addItemToSale(testItemDTO, 2); 
        Amount discount = discountStrategy.calculateDiscount(testSale);

        assertEquals(0.0, discount.getAmount(), "Discount should be 0 when total is below 500.");
    }

    @Test
    void testCalculateDiscount_emptySale_returnsZeroDiscount() {
        Amount discount = discountStrategy.calculateDiscount(new Sale());
        assertEquals(0.0, discount.getAmount(), "Discount for empty sale should be 0.");
    }
}