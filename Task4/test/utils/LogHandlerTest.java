package test.utils;

import org.junit.jupiter.api.*;
import utils.LogHandler;

import java.io.*;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogHandlerTest {
    private static final String LOG_FILE = "error_log.txt";

    @BeforeEach
    void cleanUpLogFile() throws IOException {
        Files.deleteIfExists(Paths.get(LOG_FILE));
    }

    @Test
    void testLogException_createsLogFileAndWritesContent() throws IOException {
        Exception testException = new IllegalArgumentException("Test exception message");

        LogHandler.logException(testException);

        Path logFilePath = Paths.get(LOG_FILE);
        assertTrue(Files.exists(logFilePath), "Log file should be created.");

        List<String> lines = Files.readAllLines(logFilePath);
        assertTrue(lines.stream().anyMatch(line -> line.contains("IllegalArgumentException")), "Log should contain exception name.");
        assertTrue(lines.stream().anyMatch(line -> line.contains("Test exception message")), "Log should contain exception message.");
    }

    @Test
    void testLogException_appendsToExistingLogFile() throws IOException {
        Exception firstException = new RuntimeException("First error");
        Exception secondException = new NullPointerException("Second error");

        LogHandler.logException(firstException);
        LogHandler.logException(secondException);

        List<String> lines = Files.readAllLines(Paths.get(LOG_FILE));
        long entryCount = lines.stream().filter(line -> line.contains("=== ERROR LOG ENTRY ===")).count();

        assertEquals(2, entryCount, "Log file should contain two separate log entries.");
    }

    @Test
    void testLogExceptionWithNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            LogHandler.logException(null);
        });
    }
}