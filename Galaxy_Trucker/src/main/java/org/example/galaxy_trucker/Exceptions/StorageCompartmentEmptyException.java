package org.example.galaxy_trucker.Exceptions;

public class StorageCompartmentEmptyException extends RuntimeException {
    public StorageCompartmentEmptyException(String message) {
        super(message);
    }
}
