package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that a storage compartment has reached its capacity limit
 * and cannot accept additional objects or items. This exception is used
 * to signal that an operation attempting to add to the storage compartment
 * has failed due to exceeding its capacity.
 */
public class StorageCompartmentFullException extends RuntimeException {
    /**
     * Constructs a StorageCompartmentFullException with the specified detail message.
     * This exception is thrown to indicate that a storage compartment has reached its
     * capacity limit and cannot accept additional objects or items.
     *
     * @param message the detail message providing information about the cause of the exception
     */
    public StorageCompartmentFullException(String message) {
        super(message);
    }
}
