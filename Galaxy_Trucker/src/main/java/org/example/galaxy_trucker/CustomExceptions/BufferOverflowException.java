package org.example.galaxy_trucker.CustomExceptions;

public class BufferOverflowException extends RuntimeException {
    public BufferOverflowException(String message) {
        super(message);
    }
}
