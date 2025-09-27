package exceptions;

/**
 * Thrown to simulate a database failure.
 */
public class DatabaseFailureException extends Exception {

    /**
     * Takes a message and throws it as an exception, specifically when an imaginary database connection problem occurs
     * @param message the string which is thrown as an exception.
     */
    public DatabaseFailureException(String message) {
        super(message);
    }
}
