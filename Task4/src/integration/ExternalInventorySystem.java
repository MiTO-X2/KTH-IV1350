package integration;

import dto.ItemDTO;
import dto.SaleDTO;
import exceptions.DatabaseFailureException;
import exceptions.ItemNotFoundException;
import model.Amount;
import model.Item;
import model.VAT;

import java.util.HashMap;
import java.util.Map;

/**
 * Simulates communication with an external inventory system.
 * Handles item lookup and updates item stock after a completed sale.
 */
public class ExternalInventorySystem {

    private static ExternalInventorySystem instance;
    private Map<String, Item> inventory = new HashMap<>();

    /**
     * Creates and initializes a simulated inventory database with predefined items.
     */
    public ExternalInventorySystem() {
        inventory.put("1", new Item(new ItemDTO("1", "BigWheel Oatmeal 500 ml", new Amount(29.90), VAT.VAT_6, 20, "BigWheel Oatmeal"), 20));
        inventory.put("2", new Item(new ItemDTO("2", "YouGoGo Blueberry 240 g", new Amount(14.90), VAT.VAT_6, 20, "YouGoGo Blueberry"), 20));
        inventory.put("3", new Item(new ItemDTO("3", "Just a normal bread", new Amount(49.90), VAT.VAT_12, 20, "Luxury Bread"), 20));
        inventory.put("4", new Item(new ItemDTO("4", "It's a golden one", new Amount(149.90), VAT.VAT_25, 20, "Golden Apple"), 20));
    }

    /**
     * Checks if an instance of the external inventory system is created and fetches it.
     * Otherwise, it creates a new instance.
     */
    public static ExternalInventorySystem getInstance() {
        if (instance == null) {
            instance = new ExternalInventorySystem();
        }
        return instance;
    }

    /**
     * Checks if a given item identifier exists in the inventory.
     *
     * @param identifier The identifier of the item to validate.
     * @return True if the item exists, false otherwise.
     */
    public boolean checkIfItemIdentifierValid(String identifier) {
        return inventory.containsKey(identifier);
    }

    /**
     * Retrieves the information of a specified item.
     *
     * @param identifier The identifier of the item to retrieve.
     * @return A DTO containing the item's information.
     * @throws ItemNotFoundException occurs when an item ID is not found in the inventory.
     * @throws DatabaseFailureException is thrown when a database connection issue occurs. This is an example implementation,
     * this is only thrown when a specific item ID is checked.
     */
    public ItemDTO retrieveItemInformation(String identifier) throws ItemNotFoundException, DatabaseFailureException {
        if ("DBFAIL".equalsIgnoreCase(identifier)) {
            throw new DatabaseFailureException("Failed to connect to the inventory database.");
        }

        Item item = inventory.get(identifier);
        if (item == null) {
            throw new ItemNotFoundException(identifier);
        }

        return item.getDTO();
    }

    /**
     * Updates the inventory after a completed sale by reducing stock quantities.
     *
     * @param saleDTO The sale information containing sold items and their quantities.
     */
    public void updateInventory(SaleDTO saleDTO) {
        for (ItemDTO soldItem : saleDTO.getItems()) {
            Item inventoryItem = inventory.get(soldItem.getItemIdentifier());
            int updatedQuantity = inventoryItem.getQuantity() - soldItem.getQuantity();
            inventoryItem.setQuantity(updatedQuantity);
        }
            System.out.println("Inventory system updated");
    }
}