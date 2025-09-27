package test.exceptions;

import exceptions.DatabaseFailureException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseFailureExceptionTest {

    @Test
    void testExceptionMessageIsSetCorrectly() {
        String errorMessage = "Database connection lost";
        DatabaseFailureException exception = new DatabaseFailureException(errorMessage);

        assertEquals(errorMessage, exception.getMessage(), "Exception message should match the one passed in constructor.");
    }

    @Test
    void testInstanceOfException() {
        DatabaseFailureException exception = new DatabaseFailureException("Any message");
        assertTrue(exception instanceof Exception, "DatabaseFailureException should be a subclass of Exception.");
    }

    @Test
    void testThrowingAndCatchingDatabaseFailureException() {
        String message = "Simulated DB failure";

        Exception thrown = assertThrows(DatabaseFailureException.class, () -> {
            throw new DatabaseFailureException(message);
        });

        assertEquals(message, thrown.getMessage(), "Thrown exception should contain the correct message.");
    }
}