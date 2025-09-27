package view;

import model.Amount;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Logs the total revenue to a file each time a sale is completed.
 * 
 * Extends {@link AbstractRevenueObserver} to persist revenue updates to a file named "total-revenue-log.txt".
 * It also reads the previous total revenue from the file when initialized, to continue accumulating correctly.
 */
public class TotalRevenueFileOutput extends AbstractRevenueObserver {
    
    private static final String FILE_NAME = "total-revenue-log.txt";

    /**
     * Checks the previous revenue which the checkout machine has earned and sets it as revenue which it works with.
     */
    public TotalRevenueFileOutput() {
        totalRevenue = readPreviousRevenue();
    }
    
    /**
     * Appends the latest revenue addition and the updated total revenue to the log file.
     * This method is called internally when new revenue is reported.
     * 
     * @throws IOException If the log file cannot be written to.
     */
    @Override
    protected void doShowTotalIncome() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println("Added " + String.format("%.2f", lastRevenue.getAmount()) +
                        " SEK, total revenue so far: " + String.format("%.2f", totalRevenue.getAmount()) + " SEK");
        }
    }

    /**
     * Handles errors that occur during writing to the log file.
     * Prints an error message to standard output and attempts to log the stack trace to an error log file.
     * 
     * @param e The exception encountered during logging.
     */
    @Override
    protected void handleErrors(Exception e) {
        System.out.println("[LOGGING ERROR] Could not write to revenue log file.");
        e.printStackTrace();
    }

    /**
     * Reads the previously earned revenues from past sales.
     */
    private Amount readPreviousRevenue() {
        try {
            if (!Files.exists(Paths.get(FILE_NAME))) return new Amount(0);

            java.util.List<String> lines = Files.readAllLines(Paths.get(FILE_NAME));
            if (lines.isEmpty()) return new Amount(0);
            String lastLine = lines.get(lines.size() - 1);

            String[] parts = lastLine.split("total revenue so far: ");
            if (parts.length < 2) return new Amount(0);

            String totalPart = parts[1].replace(" SEK", "").trim();
            double value = Double.parseDouble(totalPart);
            return new Amount(value);
        } catch (Exception e) {
            System.out.println("[READ ERROR] Could not read previous revenue.");
            return new Amount(0);
        }
    }
}