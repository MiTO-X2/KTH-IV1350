package test.model;

import model.*;
import dto.ItemDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDiscountStrategyTest {

    @Test
    void testCalculateDiscountWithValidCustomer() {
        DiscountDatabase db = new DiscountDatabase() {
            @Override
            public Amount getDiscountFromItems(List<ItemDTO> items) {
                return new Amount(0);
            }

            @Override
            public Amount getDiscountFromTotal(Amount total) {
                return new Amount(0);
            }

            @Override
            public Amount getDiscountFromCustomerID(String customerID) {
                if ("cust123".equals(customerID)) {
                    return new Amount(0.1);
                }
                return new Amount(0);
            }
        };

        Sale sale = new Sale() {
            @Override
            public String getCustomerID() {
                return "cust123";
            }

            @Override
            public Amount calculateTotalPrice() {
                return new Amount(200.0);
            }
        };

        CustomerDiscountStrategy strategy = new CustomerDiscountStrategy("cust123", db);
        Amount discount = strategy.calculateDiscount(sale);

        assertEquals(20.0, discount.getAmount(), 0.001,
                "Expected 10% of 200.0 as discount.");
    }

    @Test
    void testCalculateDiscountWithNullCustomerID() {
        DiscountDatabase db = new DiscountDatabase() {
            @Override
            public Amount getDiscountFromItems(List<ItemDTO> items) {
                return new Amount(0);
            }

            @Override
            public Amount getDiscountFromTotal(Amount total) {
                return new Amount(0);
            }

            @Override
            public Amount getDiscountFromCustomerID(String customerID) {
                return new Amount(0.1);
            }
        };

        Sale sale = new Sale() {
            @Override
            public String getCustomerID() {
                return null;
            }

            @Override
            public Amount calculateTotalPrice() {
                return new Amount(300.0);
            }
        };

        CustomerDiscountStrategy strategy = new CustomerDiscountStrategy(null, db);
        Amount discount = strategy.calculateDiscount(sale);

        assertEquals(0.0, discount.getAmount(), 0.001,
                "No discount should apply when customer ID is null.");
    }

    @Test
    void testCalculateDiscountWithNullDatabase() {
        Sale sale = new Sale() {
            @Override
            public String getCustomerID() {
                return "cust999";
            }

            @Override
            public Amount calculateTotalPrice() {
                return new Amount(100.0);
            }
        };

        CustomerDiscountStrategy strategy = new CustomerDiscountStrategy("cust999", null);
        Amount discount = strategy.calculateDiscount(sale);

        assertEquals(0.0, discount.getAmount(), 
                "No discount should apply when discount database is null.");
    }
}