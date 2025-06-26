package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.Actions.ComponentAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a power center component in a game board. This class extends the {@code Component} class
 * and provides specific functionality related to managing power centers in a player's game board.
 *
 * A {@code PowerCenter} manages its coordinates on the game board, interacts with the player's energy
 * state, and allows interaction through visitor-based actions. It implements key operations such as insert,
 * remove, and energy utilization.
 */
public class PowerCenter extends Component{
    /**
     * Represents the x-coordinate position of the {@code PowerCenter} on the game board.
     * This value determines the horizontal placement of the PowerCenter within the game board grid.
     *
     * It is updated when the PowerCenter is inserted into a player's board and
     * retrieved when required for operations such as validation or cloning.
     *
     * The x-coordinate is managed internally within the {@code PowerCenter} class and
     * is used in conjunction with the y-coordinate to define the position of this component.
     */
    int x;
    /**
     * Represents the Y-coordinate of this component on the game board.
     * It is used to determine the vertical placement of the power center on the player's board.
     * This field is updated during operations such as placement and removal of the power center.
     */
    int y;

    /**
     * Default constructor for the PowerCenter class.
     *
     * Initializes a new instance of the PowerCenter component
     * with default values. This class represents a specific type
     * of component in a game board, managing its position,
     * energy state, and interactions with the player's board.
     */
    public PowerCenter() {}

    /**
     * Rotates the component either clockwise or counter-clockwise based on the specified direction.
     *
     * @param direction the direction of rotation. If {@code true}, the component rotates clockwise.
     *                  If {@code false}, the component rotates counter-clockwise.
     */
    @Override
    public void rotate(Boolean direction) {}

    /**
     * Verifies the validity of the specified coordinates (x, y) on the provided player board.
     *
     * @param pb the player board on which the validity of the coordinates is checked; must not be {@code null}
     * @param x the x-coordinate to be validated
     * @param y the y-coordinate to be validated
     * @return {@code true} if the specified coordinates are valid on the player board, {@code false} otherwise
     */
    @Override
    public boolean controlValidity(@NotNull PlayerBoard pb, int x, int y) {
        return true;
    }

    /**
     * Inserts the power center into the specified position on the player's game board.
     *
     * This method updates the energy level of the game board, adds the current power
     * center to the list of power centers, sends updates about the tile state, and
     * sets the coordinates of the power center on the board.
     *
     * @param playerBoard the game board where the power center is being inserted
     * @param x the x-coordinate of the power center's position
     * @param y the y-coordinate of the power center's position
     */
    @Override
    public void insert(@NotNull PlayerBoard playerBoard, int x, int y) {
        playerBoard.setEnergy(type);
        playerBoard.getPowerCenters().add(this);
        tile.sendUpdates(null,0, false, false, type);
        this.x = x;
        this.y = y;

    }

    /**
     * Removes this power center from the specified player's game board.
     * Adjusts the player's energy based on the type of the power center and removes
     * it from the player's list of power centers. Sends an update event after removal.
     *
     * @param playerBoard The game board of the player from which this power center
     *                    is being removed. Must not be null.
     */
    @Override
    public void remove(@NotNull PlayerBoard playerBoard) {
        playerBoard.setEnergy(-type);
        playerBoard.getPowerCenters().remove(this);
        tile.sendUpdates(new RemoveTileEvent());

    }


    /**
     * Accepts a visitor, represented by the {@code ComponentAction}, to perform an operation on this
     * {@code PowerCenter} component. The current state of the player, represented by {@code PlayerState},
     * is also provided to assist in determining the nature of the operation or validating the interaction.
     *
     * This method utilizes the Visitor design pattern to decouple the operations from the components,
     * allowing specific interactions to be implemented in the visitor.
     *
     * @param visitor the {@code ComponentAction} instance that represents the operation to be performed
     *                on this {@code PowerCenter} component
     * @param State the {@code PlayerState} instance representing the current state of the player
     *              interacting with the component
     */
    @Override
    public void accept(@NotNull ComponentAction visitor, @NotNull PlayerState State) {
        visitor.visit(this, State);
    }


    /**
     * Consumes one unit of energy from the {@code PowerCenter}.
     *
     * This method decreases the energy stored in the {@code PowerCenter} by one unit
     * and notifies the relevant subsystem of the updated state using the
     * {@link Tile#sendUpdates} method. If the energy is already at zero, an
     * {@link InvalidInput} exception is thrown to indicate that no further energy
     * can be consumed.
     *
     * @throws InvalidInput if the current energy level is zero.
     */
    public void useEnergy() {
        if(this.type == 0) {
            throw new InvalidInput("cannot exceed 0 energy");
        }
        this.type = this.type-1;
        tile.sendUpdates(null,0, false, false, type);

    }

    /**
     * Retrieves the x-coordinate of the {@code PowerCenter}.
     *
     * @return the x-coordinate of the {@code PowerCenter}.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of this component.
     *
     * @return the y-coordinate as an integer
     */
    public int getY() {
        return y;
    }

    /**
     * Creates and returns a new {@code PowerCenter} object that is a clone of the current instance.
     * The cloned object will have the same type as the current instance but does not retain references to
     * the original instance's properties, except for the type information.
     *
     * @param clonedPlayerBoard the {@code PlayerBoard} to which the cloned {@code PowerCenter} is associated.
     *                          Note that this parameter is not directly used in the cloning process
     *                          but is part of the method's contract structure in the application context.
     * @return a new {@code PowerCenter} instance with cloned values.
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard){
        PowerCenter clone = new PowerCenter();
        clone.type = this.type;
        return clone;
    }

}






