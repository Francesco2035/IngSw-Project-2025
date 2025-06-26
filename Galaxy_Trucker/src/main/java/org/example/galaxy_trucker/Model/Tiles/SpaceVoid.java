package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;


/**
 * Represents an empty or void space on the player's board. The SpaceVoid component is
 * a special type of component that inherently does not support typical functionalities,
 * such as rotation or meaningful interactions, and primarily acts as a placeholder or
 * inert entity on the board.
 *
 * This class extends the abstract {@link Component} class to provide specific implementations
 * for its methods, which are intentionally limited in actions, reflecting its purpose as
 * a void space.
 */
public class SpaceVoid extends Component{


    /**
     * Throws an InvalidInput exception when an attempt is made to rotate the SpaceVoid tile,
     * as this type of tile cannot be rotated.
     *
     * @param direction the intended direction of rotation; true for clockwise, false for counterclockwise
     *                  (not applicable for a SpaceVoid tile as rotation is disallowed).
     */
    @Override
    public void rotate(Boolean direction) {throw new InvalidInput("you can't rotate spaceVoid tile");}


    /**
     * Validates the positioning of the SpaceVoid component on the player's board.
     * This method always returns true, indicating that SpaceVoid can be placed
     * at any position without additional checks.
     *
     * @param pb the player's board where the component is being validated
     * @param x the x-coordinate on the player's board
     * @param y the y-coordinate on the player's board
     * @return true, as the SpaceVoid component does not enforce specific placement rules
     */
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    /**
     * Inserts the SpaceVoid component into the specified position on the player's board.
     * The SpaceVoid acts as a placeholder and does not perform any specific action
     * when inserted, preserving its inert nature.
     *
     * @param playerBoard the player's board where the component is to be inserted
     * @param x the x-coordinate on the player's board where the component will be placed
     * @param y the y-coordinate on the player's board where the component will be placed
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {
        //tile.sendUpdates(null,0, false, false, 0);
    }

    /**
     * Removes the SpaceVoid component from the specified player's board.
     * This method does not perform any additional operations as the
     * SpaceVoid component is a placeholder and does not maintain
     * interactions or state within the board.
     *
     * @param playerBoard the player's board from which the SpaceVoid component is to be removed
     */
    @Override
    public void remove(PlayerBoard playerBoard) {}

    /**
     * Creates and returns a new instance of the SpaceVoid component. This method effectively
     * provides a duplicate of the current SpaceVoid component without transferring any state,
     * as the SpaceVoid component does not maintain any mutable characteristics.
     *
     * @param clonedPlayerBoard the player's board to be associated with the cloned component.
     *                          Although the SpaceVoid component does not interact with the board
     *                          state, this parameter is provided for consistency with the method
     *                          signature in the parent class.
     * @return a new instance of the SpaceVoid component.
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        return new SpaceVoid();
    }



}

