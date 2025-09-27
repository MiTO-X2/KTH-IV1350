package test.model;

import dto.ItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.DiscountDatabase;
import model.DiscountDatabaseImpl;
import model.ItemBasedDiscountStrategy;
import model.Amount;
import model.Sale;
import model.VAT;

import static org.junit.jupiter.api.Assertions.*;

class ItemBasedDiscountStrategyTest {
    private ItemBasedDiscountStrategy discountStrategy;   
    private ItemDTO testItemDTO; 
    private Sale testSale;

    @BeforeEach
    void setUp() {
        testItemDTO = new ItemDTO(
            "123", 
            "Test Item", 
            new Amount(100.0), 
            VAT.VAT_6,
            3, 
            "Sample Item Name"
        );
        
        testSale = new Sale();

        DiscountDatabase discountDatabase = new DiscountDatabaseImpl();
        discountStrategy = new ItemBasedDiscountStrategy(discountDatabase);
    }

    @Test
    void testCalculateDiscount_withThreeItems_returns15SEK() {
        ItemDTO testItemDTO1 = new ItemDTO(
            "1234", 
            "Test Item", 
            new Amount(100.0), 
            VAT.VAT_6,
            3, 
            "Sample Item Name"
        );
        ItemDTO testItemDTO2 = new ItemDTO(
            "12345", 
            "Test Item", 
            new Amount(100.0), 
            VAT.VAT_6,
            3, 
            "Sample Item Name"
        );
        
        testSale.addItemToSale(testItemDTO, 1);
        testSale.addItemToSale(testItemDTO1, 1);
        testSale.addItemToSale(testItemDTO2, 1);
        Amount discount = discountStrategy.calculateDiscount(testSale);

        assertEquals(15.0, discount.getAmount());
    }

    @Test
    void testCalculateDiscount_withNoItems_returns0SEK() {
        Sale sale = new Sale(); 
        Amount discount = discountStrategy.calculateDiscount(sale);

        assertEquals(0.0, discount.getAmount());
    }
}