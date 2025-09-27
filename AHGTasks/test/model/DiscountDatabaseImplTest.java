package test.model;

import dto.ItemDTO;
import model.DiscountDatabase;
import model.DiscountDatabaseImpl;
import model.VAT;
import model.Amount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiscountDatabaseImplTest {

    private DiscountDatabase discountDatabase;

    @BeforeEach
    void setUp() {
        discountDatabase = new DiscountDatabaseImpl();
    }

    @Test
    void testGetDiscountFromItems_withThreeItems_returns15SEK() {
        List<ItemDTO> items = Arrays.asList(
                createDummyItem(),
                createDummyItem(),
                createDummyItem()
        );

        Amount discount = discountDatabase.getDiscountFromItems(items);

        assertEquals(15.0, discount.getAmount());
    }

    @Test
    void testGetDiscountFromItems_withNoItems_returns0SEK() {
        List<ItemDTO> items = Collections.emptyList();

        Amount discount = discountDatabase.getDiscountFromItems(items);

        assertEquals(0.0, discount.getAmount());
    }

    @Test
    void testGetDiscountFromItems_withNullList_throwsExceptionOrReturnsZero() {
        assertThrows(NullPointerException.class, () -> discountDatabase.getDiscountFromItems(null));
    }

    @Test 
    void testGetDiscountFromTotal_belowThreshold_returns0Percent() {
        Amount discountRate = discountDatabase.getDiscountFromTotal(new Amount(499.99));

        assertEquals(0.0, discountRate.getAmount());
    }

    @Test
    void testGetDiscountFromTotal_atThreshold_returns0Percent() {
        Amount discountRate = discountDatabase.getDiscountFromTotal(new Amount(500));

        assertEquals(0.0, discountRate.getAmount());
    }

    @Test
    void testGetDiscountFromTotal_aboveThreshold_returns10Percent() {
        Amount discountRate = discountDatabase.getDiscountFromTotal(new Amount(600));

        assertEquals(60, discountRate.getAmount());
    }

    @Test
    void testGetDiscountFromCustomerID_goatCustomer_returns5Percent() {
        Amount discountRate = discountDatabase.getDiscountFromCustomerID("123");

        assertEquals(0.05, discountRate.getAmount());
    }

    @Test
    void testGetDiscountFromCustomerID_nonGoatCustomer_returns0Percent() {
        Amount discountRate = discountDatabase.getDiscountFromCustomerID("456");

        assertEquals(0.0, discountRate.getAmount());
    }

    @Test
    void testGetDiscountFromCustomerID_nullOrEmpty_returns0Percent() {
        assertEquals(0.0, discountDatabase.getDiscountFromCustomerID(null).getAmount());
        assertEquals(0.0, discountDatabase.getDiscountFromCustomerID("").getAmount());
    }

    private ItemDTO createDummyItem() {
        return new ItemDTO(
                "item1",
                "A dummy item for testing",
                new Amount(100.0),
                VAT.VAT_25,
                1,
                "TestItem"
        );
    }
}