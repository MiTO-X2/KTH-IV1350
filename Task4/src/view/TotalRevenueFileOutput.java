package view;

import model.Amount;
import utils.RevenueObserver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Logs total revenue to a file each time a sale is completed.
 */
public class TotalRevenueFileOutput implements RevenueObserver {
    private Amount totalRevenue;
    private static final String FILE_NAME = "total-revenue-log.txt";

    /**
     * Checks the previous revenue which the checkout machine has earned and sets it as revenue which it works with.
     */
    public TotalRevenueFileOutput() {
        totalRevenue = readPreviousRevenue();
    }
    
    /**
     * The function takes previous revenue and adds it with the revenue of the latest sale, then logs it.
     * @param revenue The amount of money which the checkout machine hase earned previously.
     */
    @Override
    public void newRevenue(Amount revenue) {
        totalRevenue = totalRevenue.add(revenue);
        logRevenueToFile(revenue);
    }

    /**
     * The function logs the latest total revenue which the checkout machine has earned.
     * @param addedRevenue The combined revenue from previous transactions and the current one.
     */
    private void logRevenueToFile(Amount addedRevenue) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            writer.println("Added " + String.format("%.2f", addedRevenue.getAmount()) + " SEK, " +
                "total revenue so far: " + String.format("%.2f", totalRevenue.getAmount()) + " SEK");
        } catch (IOException e) {
            System.out.println("[LOGGING ERROR] Could not write to revenue log file.");
            e.printStackTrace();
        }
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