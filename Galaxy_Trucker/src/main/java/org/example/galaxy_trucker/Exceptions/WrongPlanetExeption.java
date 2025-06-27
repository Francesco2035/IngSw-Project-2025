package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that the operation or action is being attempted on the wrong planet.
 * This exception is used to signal that the specific context or conditions expected
 * for the operation do not match the planet where the operation is being performed.
 *
 * Typically, this exception is raised when the planet-related criteria or constraints
 * necessary for an operation are violated.
 */
public class WrongPlanetExeption extends RuntimeException {
    /**
     * Constructs a WrongPlanetExeption with the specified detail message.
     * This exception is thrown to indicate that an operation or action
     * is being attempted on the wrong planet. It signifies that the expected
     * planet-related criteria or conditions for the operation are violated.
     *
     * @param message the detail message describing the specific cause of the exception
     */
    public WrongPlanetExeption(String message) {
        super(message);
    }
}
