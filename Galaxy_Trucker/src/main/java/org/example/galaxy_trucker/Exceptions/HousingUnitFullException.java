package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that a housing unit is full and cannot accept additional occupants or objects.
 * This exception is typically used to signal that an operation attempting to add to a housing unit
 * has failed due to reaching its capacity limit.
 */
public class HousingUnitFullException extends RuntimeException {
    /**
     * Constructs a HousingUnitFullException with the specified detail message.
     * This exception indicates that a housing unit is full and cannot accept additional occupants or objects.
     *
     * @param message the detail message describing the specific cause of the exception
     */
    public HousingUnitFullException(String message) {
        super(message);
    }
}
