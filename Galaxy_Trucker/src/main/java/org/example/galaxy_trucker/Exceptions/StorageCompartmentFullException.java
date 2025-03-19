package org.example.galaxy_trucker.Exceptions;

public class StorageCompartmentFullException extends RuntimeException {
    public StorageCompartmentFullException(String message) {
        super(message);
    }
}
