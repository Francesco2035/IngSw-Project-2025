package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

/**
 * Represents a sewer pipes component in the game. This class defines
 * specific functionality for the sewer pipes, including their
 * behavior when rotated, validated, inserted, or removed from the player's board.
 *
 * The `SewerPipes` class is a concrete implementation of the abstract `Component` class,
 * inheriting its behavior and extending it to meet the needs of the sewer pipes functionality.
 */
public class SewerPipes extends Component {


    /**
     * Constructs a new SewerPipes component. This constructor initializes
     * the sewer pipes as a specific type of component that can be used
     * in the player's board game. Instances of this class represent the
     * functionality and behavior of sewer pipes in the game context.
     *
     * This is a default constructor and does not take any parameters.
     */
    public SewerPipes() {}


    /**
     * Rotates the sewer pipe component in a specified direction.
     *
     * @param direction true to rotate the component clockwise, false to rotate it counterclockwise
     */
    @Override
    public void rotate(Boolean direction) {}

    /**
     * Checks the validity of placing a sewer pipe component on the specified coordinates
     * of the player's board.
     *
     * @param pb the player's board where the validity check is performed
     * @param x the x-coordinate on the player's board
     * @param y the y-coordinate on the player's board
     * @return true if the placement at the specified coordinates is valid, false otherwise
     */
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    /**
     * Inserts the sewer pipe component at the specified position on the player's board.
     * Updates are sent to notify relevant changes.
     *
     * @param playerBoard the player's board where the sewer pipe will be inserted
     * @param x the x-coordinate of the position where the sewer pipe will be inserted
     * @param y the y-coordinate of the position where the sewer pipe will be inserted
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        tile.sendUpdates(null,0, false, false, 0);

    }

    /**
     * Removes a sewer pipe component from the specified player's board.
     * This method triggers the necessary updates when the component is removed.
     *
     * @param playerBoard The player's board from which the sewer pipe component will be removed.
     */
    @Override
    public void remove(PlayerBoard playerBoard) {
        tile.sendUpdates();

    }

    /**
     * Creates and returns a duplicate instance of the SewerPipes component.
     *
     * The method provides a new instance of SewerPipes that is functionally
     * identical to the original, allowing for duplication and reuse on a
     * specified PlayerBoard. The cloned instance is independent of the
     * original component.
     *
     * @param clonedPlayerBoard the player's board to associate with the cloned component
     * @return a new instance of SewerPipes representing the cloned component
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        return new SewerPipes();
    }


}

