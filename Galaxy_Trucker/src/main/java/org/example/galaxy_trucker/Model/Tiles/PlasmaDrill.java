package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

/**
 * The PlasmaDrill class extends the Component class and represents a specialized game component
 * with additional functionalities such as rotation, validation, and interaction with the player's board.
 */
public class PlasmaDrill extends Component{


    /**
     * Represents the current direction of the cannon in the PlasmaDrill component.
     * The direction is encoded as an integer with the following possible values:
     * 0: Left
     * 1: Up
     * 2: Right
     * 3: Down
     *
     * This variable determines how the PlasmaDrill interacts with the game board,
     * such as validating nearby tiles, adjusting drill power, or establishing its orientation.
     * It is modified by the rotate method and used in various operations to ensure
     * the PlasmaDrill's behavior aligns with its current direction.
     */
    private int CannonDirection = 1;

    /**
     * Changes the direction of the cannon by rotating either clockwise or counterclockwise.
     *
     * @param direction If true, rotates the cannon clockwise. If false, rotates the cannon counterclockwise.
     */
    @Override
    public void rotate(Boolean direction) {
        if (direction) {
            CannonDirection = (CannonDirection + 1) % 4;
        }
        else{
            CannonDirection = (CannonDirection - 1) % 4;
        }
    }

    /**
     * Checks the validity of placing the PlasmaDrill component on the player's board
     * based on its current cannon direction and the specified coordinates.
     *
     * @param pb the player's board object containing the current state of the game board
     * @param x the x-coordinate on the board where the action is being validated
     * @param y the y-coordinate on the board where the action is being validated
     * @return true if the placement or positioning is valid; false if it conflicts with the rules
     */
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y){
        int[][] mat = pb.getValidPlayerBoard();
        if (CannonDirection == 0 && y-1 >= 0 && mat[x][y-1]==1) return false;
        if (CannonDirection == 1 && x-1 >= 0 && mat[x-1][y]==1) return false;
        if (CannonDirection == 2 && y+1 < 10 && mat[x][y+1]==1) return false;
        if (CannonDirection == 3 && x+1 < 10 && mat[x+1][y]==1) return false;
        return true;
    }

    /**
     * Accepts a visitor implementing the ComponentAction interface to perform a specific action
     * on the PlasmaDrill component within the context of the provided player state.
     *
     * This method leverages the Visitor design pattern to allow encapsulated logic for
     * actions specific to the PlasmaDrill component, without modifying the component itself.
     *
     * @param visitor the ComponentAction visitor that defines the logic to apply to this component
     * @param State the current state of the player, used to determine the effects of the action
     */
    @Override
    public void accept(ComponentAction visitor, PlayerState State) {
        visitor.visit(this, State);
    }


    /**
     * Inserts the current PlasmaDrill component into the specified player's board at the given position.
     * Updates the plasma drills power on the player board based on the type and cannon direction.
     *
     * @param playerBoard the PlayerBoard instance where the PlasmaDrill is to be inserted
     * @param x the x-coordinate of the position where the PlasmaDrill is placed
     * @param y the y-coordinate of the position where the PlasmaDrill is placed
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y)  {
        if (type == 1) {
            if (CannonDirection == 1){
                playerBoard.setPlasmaDrillsPower(1);
            }
            else {
                playerBoard.setPlasmaDrillsPower(0.5);
            }
        }
        playerBoard.getPlasmaDrills().add(this);
        tile.sendUpdates(null,0, false, false, 0);

    }

    /**
     * Removes this PlasmaDrill component from the specified player's board and updates its state accordingly.
     *
     * @param playerBoard the player's board from which this PlasmaDrill is to be removed
     */
    @Override
    public void remove(PlayerBoard playerBoard) {
        if (type == 1) {
            if (CannonDirection == 1) {
                playerBoard.setPlasmaDrillsPower(-1);
            } else {
                playerBoard.setPlasmaDrillsPower(-0.5);
            }

            playerBoard.getPlasmaDrills().remove(this);
        }
        tile.sendUpdates();
    }

    /**
     * Calculates and returns the power of the cannon based on its type and direction.
     *
     * @return the cannon power as a double. If the cannon type is 2 and direction is 1, it returns 2.
     *         If the cannon type is 2 and direction is not 1, it returns 1. Otherwise, it returns 0.
     */
    public double getCannonPower(){
        if (type == 2){
            if (CannonDirection == 1){
                return 2;
            }
            else {
                return 1;
            }
        }
        else {

            return 0;
        }
    }

    /**
     * Creates a clone of the current PlasmaDrill instance, copying its state
     * including CannonDirection and type, and associates it with a specified PlayerBoard.
     *
     * @param clonedPlayerBoard the PlayerBoard to which the cloned PlasmaDrill will be associated.
     * This parameter is not directly utilized for creating the clone but indicates the applicable context.
     * @return a new PlasmaDrill instance with the same CannonDirection and type as the original.
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        PlasmaDrill clone = new PlasmaDrill();
        clone.CannonDirection = CannonDirection;
        clone.type = type;
        return clone;
    }


}
