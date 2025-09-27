package model;

import dto.ItemDTO;
import java.util.List;


/**
 * A simple concrete implementation of DiscountDatabase for testing.
 */
public class DiscountDatabaseImpl implements DiscountDatabase {
    
    /**
     * For example, this takes all items and takes a fixed 5 SEK discount for every item for demo purposes.
     * @param items all the items which are in the current sale.
     * @return the discount amount which will be subtracted in from the total price.
     */
    @Override
    public Amount getDiscountFromItems(List<ItemDTO> items) {
        double discount = items.size() * 5.0;
        return new Amount(discount);
    }

    /**
     * A demo function which takes a 10% discount if the total price is more than 500kr, otherwise
     * the discount is 0kr.
     * @param total the total price of the entire sale.
     * @return 0kr or 10% of the total price if it exceeds 500kr.
     */
    @Override
    public Amount getDiscountFromTotal(Amount total) {
        if (total.getAmount() > 500) {
            return new Amount(total.getAmount() * 0.10);
        }
        return new Amount(0);
    }

    /**
     * A demo function which takes 5% discount of the whole sale if it is customer with the ID "123"
     * 
     * @param customerID the id of the customer we are discounting in the current sale. 
     */
    @Override
    public Amount getDiscountFromCustomerID(String customerID) {
        if ("123".equals(customerID)) {
            return new Amount(0.05);
        }
        return new Amount(0);
    }
}