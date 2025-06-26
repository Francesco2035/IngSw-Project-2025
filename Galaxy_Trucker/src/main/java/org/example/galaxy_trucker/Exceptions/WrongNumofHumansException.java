package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that the number of humans involved in a particular operation
 * is not as expected. This exception is used to signal scenarios where the count
 * of humans does not match the required or permissible criteria for the operation.
 */
public class WrongNumofHumansException extends RuntimeException {
    /**
     * Constructs a WrongNumofHumansException with the specified detail message.
     * This exception is thrown to indicate that the number of humans involved
     * in a specific operation does not meet the expected criteria or constraints.
     *
     * @param message the detail message describing the specific cause or context
     *                of the exception
     */
    public WrongNumofHumansException(String message) {
        super(message);
    }
}
