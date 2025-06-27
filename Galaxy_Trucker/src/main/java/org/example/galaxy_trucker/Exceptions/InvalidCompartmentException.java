package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that a specified compartment is invalid in the given context.
 * This exception may be used to signal that an operation involving a compartment
 * cannot proceed due to the compartment being improperly defined, nonexistent,
 * or failing to meet certain criteria.
 */
public class InvalidCompartmentException extends RuntimeException {
    /**
     * Constructs an InvalidCompartmentException with the specified detail message.
     * This exception indicates that a specified compartment is invalid in the given context.
     *
     * @param message the detail message describing the specific reason why the compartment is invalid
     */
    public InvalidCompartmentException(String message) {
        super(message);
    }
}
