package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that a housing unit is unexpectedly empty.
 * This exception is typically used to signal issues related to an unoccupied housing unit
 * when an operation requires it to contain occupants or objects.
 */
public class HousingUnitEmptyException extends RuntimeException {
    /**
     * Constructs a HousingUnitEmptyException with the specified detail message.
     * This exception indicates that a housing unit is unexpectedly empty during an operation
     * that requires it to be occupied or contain specific objects.
     *
     * @param message the detail message describing the specific cause of the exception
     */
    public HousingUnitEmptyException(String message) {
        super(message);
    }
}
