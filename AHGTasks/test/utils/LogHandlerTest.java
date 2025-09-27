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

    @Test
    void testLogEntryHasCorrectFormat() throws IOException {
        LogHandler.logException(new RuntimeException("Formatted check"));

        List<String> lines = Files.readAllLines(Paths.get(LOG_FILE));
        assertTrue(lines.get(0).contains("=== ERROR LOG ENTRY ==="), "Should start with log entry separator.");
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Time: ")), "Should contain timestamp.");
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("    at ")), "Should contain stack trace.");
        assertTrue(lines.stream().anyMatch(line -> line.contains("========================")), "Should end with separator.");
    }

    @Test
    void testLogFormat_containsAllKeyComponents() throws IOException {
        Exception testException = new IllegalStateException("Test message format");

        LogHandler.logException(testException);

        List<String> lines = Files.readAllLines(Paths.get(LOG_FILE));

        assertTrue(lines.stream().anyMatch(line -> line.startsWith("Time:")), "Log should include a timestamp.");
        assertTrue(lines.stream().anyMatch(line -> line.contains("IllegalStateException")), "Log should include exception class.");
        assertTrue(lines.stream().anyMatch(line -> line.contains("Test message format")), "Log should include exception message.");
        assertTrue(lines.stream().anyMatch(line -> line.trim().startsWith("at ")), "Log should include stack trace lines.");
    }

    @Test
    void testIOExceptionDuringLogging_printsToStdErr() throws Exception {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        Path logPath = Paths.get(LOG_FILE);
        Files.createFile(logPath);
        logPath.toFile().setWritable(false);

        LogHandler.logException(new IOException("Simulated write failure"));

        System.setErr(originalErr); 
        String errorOutput = errContent.toString();

        assertTrue(errorOutput.contains("Failed to write to log file"), "Should print error to System.err");

        logPath.toFile().setWritable(true);
        Files.deleteIfExists(logPath);
    }
}