package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Handles logging of system errors to a file.
 */
public class LogHandler {
    private static final String LOG_FILE = "error_log.txt";

    /**
     * Function which logs errors in a document, specifiying when eror occured, 
     * which exception occured and the message which follows it.
     * @param e the exception which is logged.
     */
    public static void logException(Exception e) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println("=== ERROR LOG ENTRY ===");
            writer.println("Time: " + LocalDateTime.now());
            writer.println("Exception: " + e.getClass().getSimpleName());
            writer.println("Message: " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                writer.println("    at " + element.toString());
            }
            writer.println("========================\n");
        } catch (IOException ioException) {
            System.err.println("Failed to write to log file: " + ioException.getMessage());
        }
    }
}