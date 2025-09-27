package model;

import dto.ReceiptDTO;

/**
 * Represents a finalized receipt of a sale transaction.
 * 
 * Contains a snapshot of the sale information at the time of purchase.
 */
public class Receipt {
    private ReceiptDTO receiptDTO;

    /**
     * Creates a new Receipt with a specified ReceiptDTO.
     * 
     * @param receiptDTO The DTO containing receipt details.
     */
    public Receipt(ReceiptDTO receiptDTO) { 
        this.receiptDTO = receiptDTO; 
    }

    /**
     * Gets the ReceiptDTO containing receipt information.
     * 
     * @return The receipt's DTO.
     */
    public ReceiptDTO getReceiptDTO() { 
        return receiptDTO; 
    }
}

