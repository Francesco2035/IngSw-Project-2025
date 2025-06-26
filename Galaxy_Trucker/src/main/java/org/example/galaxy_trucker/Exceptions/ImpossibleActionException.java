package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that an attempted action cannot be performed because it is impossible
 * under the current circumstances. This exception signals that the action requested violates
 * logical or operational constraints of the system.
 */
public class ImpossibleActionException extends RuntimeException {
    /**
     * Constructs an ImpossibleActionException with the specified detail message.
     * This exception indicates that an attempted action cannot be performed because it is
     * deemed logically or operationally impossible under the current circumstances.
     *
     * @param message the detail message providing information about the cause of the exception
     */
    public ImpossibleActionException(String message) {
        super(message);
    }
}
