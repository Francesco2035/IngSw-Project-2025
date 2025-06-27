package org.example.galaxy_trucker.Exceptions;

/**
 * Thrown to indicate that an attempted modification to a game board cannot be performed
 * because it violates the rules, constraints, or logical structure of the game.
 * This exception signals that the requested board change is deemed invalid or impossible.
 */
public class ImpossibleBoardChangeException extends RuntimeException {
    /**
     * Constructs an ImpossibleBoardChangeException with the specified detail message.
     * This exception indicates that an attempted modification to the game board cannot
     * be performed because it violates the rules, constraints, or logical structure of the game.
     *
     * @param message the detail message describing the specific cause of the exception
     */
    public ImpossibleBoardChangeException(String message) {
        super(message);
    }
}
