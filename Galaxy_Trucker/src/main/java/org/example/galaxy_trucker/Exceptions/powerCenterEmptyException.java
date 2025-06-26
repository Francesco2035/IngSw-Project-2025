package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that a power center is unexpectedly empty.
 * This exception is typically used to signal problems related to a power center
 * that is required to contain energy or other resources for an operation to proceed.
 */
public class powerCenterEmptyException extends RuntimeException {
  /**
   * Constructs a powerCenterEmptyException with the specified detail message.
   * This exception is thrown to indicate that a power center is unexpectedly empty,
   * typically signaling a problem in operations requiring a non-empty power center.
   *
   * @param message the detail message describing the specific cause of the exception
   */
  public powerCenterEmptyException(String message) {
    super(message);
  }
}
