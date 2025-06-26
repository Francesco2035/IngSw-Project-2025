package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that an invalid input has been provided. This exception is used
 * to signal that input values or parameters do not meet the expected criteria or
 * constraints for a particular operation or context.
 *
 * This exception may include additional contextual information about the invalid
 * input, such as specific coordinates or error details.
 */
public class InvalidInput extends RuntimeException {
    /**
     * Represents the x-coordinate associated with the invalid input.
     * This value is used to provide additional context about the location
     * or source of the invalid input in a coordinate system.
     *
     * The value is initialized during object construction and remains
     * constant throughout the object's lifecycle.
     */
    private final int x;
    /**
     * Represents the y-coordinate of an invalid input. This value is used to provide
     * additional context about the invalid input, typically representing a specific
     * coordinate or parameter involved in the error.
     */
    private final int y;
    /**
     * Specifies detailed information about the error that has occurred.
     * This variable provides a descriptive message or context related to
     * the specific nature of the exception, assisting in identifying
     * the cause and location of the invalid input or operation.
     */
    private final String errorDetails;

    /**
     * Constructs an InvalidInput exception with the specified detail message.
     * This constructor is used to signal an invalid input scenario with an accompanying
     * error message providing details about the nature of the invalid input.
     *
     * @param message the detail message describing the specific cause or context
     *                of the invalid input
     */
    public InvalidInput(String message){
        super(message);
        this.x = -1;
        this.y = -1;
        this.errorDetails = message;
    }

    /**
     * Constructs an InvalidInput exception with specific coordinates and a detailed error message.
     * This exception is used to indicate that the provided coordinates are invalid for the intended operation.
     *
     * @param x the x-coordinate that caused the exception
     * @param y the y-coordinate that caused the exception
     * @param message the detail message explaining the cause of the exception
     */
    public InvalidInput(int x, int y, String message) {
        super(message);
        this.x = x;
        this.y = y;
        this.errorDetails = "Invalid coordinates: x = " + x + ", y = " + y;
    }

    /**
     * Retrieves the x-coordinate associated with this exception.
     *
     * @return the x-coordinate value indicating a specific detail about the invalid input
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the value of the 'y' coordinate associated with this instance.
     *
     * @return the integer value representing the 'y' coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Retrieves the detailed error message associated with this exception.
     * This string typically provides additional context or information
     * about the nature and specifics of the exception.
     *
     * @return a string containing the error details for further diagnostic or informational purposes
     */
    public String getErrorDetails() {
        return errorDetails;
    }
}
