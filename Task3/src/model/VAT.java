package model;

/**
 * Enum representing available VAT (Value-Added Tax) rates.
 */
public enum VAT {
    VAT_6(0.06),
    VAT_12(0.12), 
    VAT_25(0.25); 

    private final double rate;

    /**
     * Initializes a VAT rate.
     * 
     * @param rate The fractional VAT rate (e.g., 0.06 for 6%).
     */
    VAT(double rate) {
        this.rate = rate;
    }

    /**
     * Gets the fractional VAT rate (e.g., 0.06).
     * 
     * @return The VAT rate.
     */
    public double getRate() {
        return rate;
    }

    /**
     * Gets the percentage form of the VAT rate (e.g., 6).
     * 
     * @return The VAT percentage.
     */
    public int getPercentage() {
        return (int) (rate * 100); 
    }
}

