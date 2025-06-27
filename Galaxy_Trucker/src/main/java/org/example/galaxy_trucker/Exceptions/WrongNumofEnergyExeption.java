package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that the number of energy units provided or required in an operation
 * is incorrect. This exception is used to signal that the energy-related parameters or
 * values do not meet the expected criteria or rules for the specific context.
 */
public class WrongNumofEnergyExeption extends RuntimeException {
    /**
     * Constructs a WrongNumofEnergyExeption with the specified detail message.
     * This exception is used to indicate that the number of energy units
     * provided or required in an operation is incorrect or does not meet
     * the expected criteria.
     *
     * @param message the detail message describing the specific cause of the exception
     */
    public WrongNumofEnergyExeption(String message) {
        super(message);
    }
}
