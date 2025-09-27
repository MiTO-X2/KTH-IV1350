package model;

import dto.ItemDTO;
import java.util.List;

/*
 * An interface for the discount database. Its used for getting the price of all items in a current sale.
 * After that it discounts the price depending on what strategy is used.
 */
public interface DiscountDatabase {
    Amount getDiscountFromItems(List<ItemDTO> items); 
    Amount getDiscountFromTotal(Amount total);        
    Amount getDiscountFromCustomerID(String customerID); 
}