package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that a storage compartment is unexpectedly empty.
 * This exception is typically used to signal issues related to an operation
 * that requires the storage compartment to contain items or resources,
 * but the compartment is found to be empty.
 */
public class StorageCompartmentEmptyException extends RuntimeException {
    /**
     * Constructs a StorageCompartmentEmptyException with the specified detail message.
     * This exception indicates that a storage compartment is unexpectedly empty
     * when it is required to contain items or resources for an operation to proceed.
     *
     * @param message the detail message describing the specific cause of the exception
     */
    public StorageCompartmentEmptyException(String message) {
        super(message);
    }
}
