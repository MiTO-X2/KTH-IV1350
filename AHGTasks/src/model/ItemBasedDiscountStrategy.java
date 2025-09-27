package model;

import java.util.List;

import dto.ItemDTO;

/*
 * A strategy which discounts the price of items depending on the discount of the individual item.
 */
public class ItemBasedDiscountStrategy implements DiscountStrategy {
    private DiscountDatabase discountDatabase;

    /**
     * Constructor of the item based discount strategy. 
     * @param discountDatabase takes the current made discount database.
     */
    public ItemBasedDiscountStrategy(DiscountDatabase discountDatabase) {
        this.discountDatabase = discountDatabase;
    }

    /**
     * Takes the list of items of the current sale and discounts them based on the item discount.
     * 
     * @param sale the lsit of bought items of the customer in the current sale.
     * @return an amount, which is discount to be deducted from the total price.
     */
    @Override
    public Amount calculateDiscount(Sale sale) {
        List<ItemDTO> itemDTOs = sale.getListOfItemDTOs();
        return discountDatabase.getDiscountFromItems(itemDTOs);
    }
}