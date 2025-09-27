package test.view;

import model.Amount;
import org.junit.jupiter.api.*;
import view.TotalRevenueFileOutput;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TotalRevenueFileOutputTest {
    private static final String FILE_NAME = "total-revenue-log.txt";

    @BeforeEach
    void clearLogFile() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_NAME));
    }

    @Test
    void testNewRevenue_createsLogFileWithCorrectContent() throws IOException {
        TotalRevenueFileOutput revenueLogger = new TotalRevenueFileOutput();
        revenueLogger.newRevenue(new Amount(150.0));

        assertTrue(Files.exists(Paths.get(FILE_NAME)), "Log file should be created.");

        List<String> lines = Files.readAllLines(Paths.get(FILE_NAME));
        assertFalse(lines.isEmpty(), "Log file should contain at least one entry.");
        assertTrue(lines.get(0).contains("Added 150.00 SEK"), "Log should show added revenue.");
        assertTrue(lines.get(0).contains("total revenue so far: 150.00 SEK"), "Log should show total revenue.");
    }

    @Test
    void testNewRevenue_appendsToExistingRevenue() throws IOException {
        TotalRevenueFileOutput revenueLogger1 = new TotalRevenueFileOutput();
        revenueLogger1.newRevenue(new Amount(100.0));

        revenueLogger1.newRevenue(new Amount(50.0));

        List<String> lines = Files.readAllLines(Paths.get(FILE_NAME));
        assertEquals(2, lines.size(), "Log file should contain two entries.");
        assertTrue(lines.get(1).contains("total revenue so far: 150.00 SEK"), "Second entry should reflect total of both revenues.");
    }

    @Test
    void testReadPreviousRevenue_fileDoesNotExist_returnsZero() {
        TotalRevenueFileOutput revenueLogger = new TotalRevenueFileOutput();
        revenueLogger.newRevenue(new Amount(0.0));
        assertTrue(Files.exists(Paths.get(FILE_NAME)), "File should still be created even with 0 revenue.");
    }

    @Test
    void testRevenueAccumulationAfterRestart() throws IOException {
        TotalRevenueFileOutput logger1 = new TotalRevenueFileOutput();
        logger1.newRevenue(new Amount(100.0));

        TotalRevenueFileOutput logger2 = new TotalRevenueFileOutput();
        logger2.newRevenue(new Amount(50.0));

        List<String> lines = Files.readAllLines(Paths.get(FILE_NAME));
        assertEquals(2, lines.size(), "Two entries should exist after restart.");
        assertTrue(lines.get(1).contains("total revenue so far: 150.00 SEK"),
            "Second entry should reflect accumulated revenue across instances.");
    }

    @AfterEach
    void cleanUpFiles() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_NAME));
        Files.deleteIfExists(Paths.get("error.log"));
    }
}