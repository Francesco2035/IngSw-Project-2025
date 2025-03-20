package org.example.galaxy_trucker.Exceptions;

public class InvalidInput extends RuntimeException {
    private final int x;
    private final int y;
    private final String errorDetails;

    public InvalidInput(String message){
        super(message);
        this.x = -1;
        this.y = -1;
        this.errorDetails = message;
    }

    public InvalidInput(int x, int y, String message) {
        super(message);
        this.x = x;
        this.y = y;
        this.errorDetails = "Invalid coordinates: x = " + x + ", y = " + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getErrorDetails() {
        return errorDetails;
    }
}
