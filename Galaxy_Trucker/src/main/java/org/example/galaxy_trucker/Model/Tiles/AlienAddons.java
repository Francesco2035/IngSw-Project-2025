package org.example.galaxy_trucker.Model.Tiles;

import org.example.galaxy_trucker.ClientServer.Messages.PlayerBoardEvents.RemoveTileEvent;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;

/**
 * Represents a specific type of {@code Component} known as AlienAddons.
 * This class defines behavior for a component that can be added, removed,
 * cloned, rotated, and validated in context with a {@code PlayerBoard}.
 *
 * AlienAddons introduces unique functionality to modify or interact with
 * the player's game state.
 */
public class AlienAddons extends Component{


//purple=true, brown=false

    /**
     * Constructs a new instance of the AlienAddons class.
     *
     * This constructor initializes an AlienAddons component, representing a specific
     * game element that can be added to a player's board. AlienAddons contribute
     * to gameplay with unique functionalities such as interaction with the player's
     * state, rotation logic, and board manipulation.
     */
    public AlienAddons() {}

    /**
     * Determines the validity of a specific action or position on the given {@code PlayerBoard}.
     *
     * @param pb the {@code PlayerBoard} instance to evaluate
     * @param x the x-coordinate to check on the board
     * @param y the y-coordinate to check on the board
     * @return {@code true} if the position or action is valid, otherwise {@code false}
     */
    @Override
    public boolean controlValidity(PlayerBoard pb, int x, int y) {
        return true;
    }

    /**
     * Rotates the component in the specified direction.
     *
     * @param direction the direction to rotate the component.
     *                  Pass {@code true} for clockwise rotation
     *                  and {@code false} for counterclockwise rotation.
     */
    @Override
    public void rotate(Boolean direction){}

    /**
     * Inserts the current {@code AlienAddons} instance into the specified {@code PlayerBoard}
     * at the specified coordinates. This method adds the AlienAddons instance to the player board's
     * list of addons and sends updates to the game state.
     *
     * @param playerBoard the {@code PlayerBoard} where the AlienAddons instance is to be inserted
     * @param x the x-coordinate where the AlienAddons instance is to be positioned
     * @param y the y-coordinate where the AlienAddons instance is to be positioned
     */
    @Override
    public void insert(PlayerBoard playerBoard, int x, int y) {

        playerBoard.getAlienAddons().add(this);
        tile.sendUpdates(null, 0,false, false, 0);

    }

    /**
     * Removes this AlienAddons component from the specified {@code PlayerBoard}.
     * This method updates the game state by removing the component from the
     * player's collection of AlienAddons and sending a removal event.
     *
     * @param playerBoard the {@code PlayerBoard} instance from which this
     *                    AlienAddons component will be removed
     */
    @Override
    public void remove(PlayerBoard playerBoard) {

        playerBoard.getAlienAddons().remove(this);
        tile.sendUpdates(new RemoveTileEvent());
    }

    /**
     * Creates and returns a copy of this {@code AlienAddons} object.
     * The clone shares the same type as the original object but does
     * not share any references to other objects, ensuring a distinct instance.
     *
     * @param clonedPlayerBoard the {@code PlayerBoard} associated with the cloning process,
     *                          though it is not used within this method.
     * @return a new {@code Component} instance that is a copy of this {@code AlienAddons} object.
     */
    @Override
    public Component clone(PlayerBoard clonedPlayerBoard) {
        AlienAddons clone = new AlienAddons();
        clone.type = type;
        return clone;
    }


}
