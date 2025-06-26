package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that an invalid defense configuration or operation was encountered.
 * This exception signifies that a defense-related action or setup has failed
 * due to logical inconsistencies, invalid parameters, or other domain-specific constraints.
 */
public class InvalidDefenceEceptiopn extends RuntimeException {
    /**
     * Constructs an InvalidDefenceEceptiopn with the specified detail message.
     * This exception indicates that an invalid defense configuration or operation
     * has been encountered, signifying logical inconsistencies, invalid parameters,
     * or other defense-related issues.
     *
     * @param message the detail message describing the specific reason for the exception
     */
    public InvalidDefenceEceptiopn(String message) {
        super(message);
    }
}
