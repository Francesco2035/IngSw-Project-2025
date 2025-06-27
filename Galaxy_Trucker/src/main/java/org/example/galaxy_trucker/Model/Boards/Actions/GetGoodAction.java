package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Storage;

/**
 * GetGoodAction represents an action to retrieve a good from a storage component
 * and manages interactions and validations related to this operation. It extends
 * the abstract class ComponentAction to specifically implement the functionality
 * for retrieving a good from storage, ensuring compliance with game rules and logic.
 *
 * This action is characterized by:
 * - The position index of the good to be retrieved from the Storage component.
 * - A reference to the player's PlayerBoard, which defines their game state and the handling of goods.
 * - Coordinates (x, y) to further contextualize the location of the good or its interaction on the board.
 *
 * The primary interaction for this action is defined by overriding the `visit` method for the
 * Storage component, allowing retrieval of the good based on validation rules. If invalid input
 * is provided or the action is not permitted given the state of the player, exceptions are thrown.
 *
 * Key Functional Notes:
 * - The retrieved good is stored within the class instance for further access or processing.
 * - Input validation is performed to ensure proper state and valid position indexes.
 * - Interception of invalid actions such as out-of-bounds positions ensures robustness.
 *
 * Supported Functionality:
 * - Interfacing with the Storage component.
 * - Validating the action against player state using the allows method of PlayerState.
 * - Accessing the retrieved good via a getter method.
 */
public class GetGoodAction extends ComponentAction {

    /**
     * Represents the position index of a good to be retrieved from a storage component.
     *
     * This variable is used within the `GetGoodAction` class to specify the index of a good
     * that should be removed from the storage. The value of this variable is validated during
     * the execution of the action to ensure it falls within the bounds of the available
     * storage and adheres to game rules.
     *
     * Constraints:
     * - The position must correspond to a valid index within the storage component.
     * - If the position is invalid or out of bounds, an exception will be thrown during the action execution.
     */
    private int position;
    /**
     * Represents the good retrieved as part of the action performed in the {@code GetGoodAction} class.
     * This variable holds the reference to a specific {@code Goods} object, which is removed
     * from the storage component as part of the action execution.
     *
     * Key Behavior:
     * - The value of this variable is set when the {@code visit} method of {@code GetGoodAction} is executed.
     * - It references the good removed from the specified position in the storage.
     * - If the position is invalid or no good is present at the position, its value remains {@code null}.
     *
     * Functional Usage:
     * - Stores the result of the good retrieval operation for further use in game logic or validation.
     * - Can be accessed externally using the getter method {@code getGood()}.
     *
     * Associated Constraints:
     * - The value is validated during retrieval; if the input position is out of bounds or the action
     *   is not allowed in the current player state, an exception is thrown, and no good is assigned.
     */
    private Goods good;
    /**
     * Represents the player's game board in the context of the game state.
     * The PlayerBoard tracks and manages the player's resources, actions,
     * and other elements that define their gameplay.
     *
     * It is used to interact with the game state during actions and ensures
     * proper handling of player-specific data, such as stored goods,
     * inventory, and other game-related elements.
     *
     * This variable is a core component for managing the player's in-game
     * behavior and validates actions based on the state of the PlayerBoard.
     */
    private PlayerBoard playerBoard;
    /**
     * Represents the x-coordinate in the context of the GetGoodAction class.
     *
     * This variable is used to define the x-coordinate of the location within the player's board
     * for identifying and retrieving the position of a good. It is paired with the y-coordinate
     * to specify a two-dimensional position on the board. The coordinate system is determined
     * by the implementation of the board in the game context.
     *
     * Functional Roles:
     * - Acts as part of the positional indexing for goods in the player's storage or board.
     * - Enables contextual interaction for defining or validating actions based on location.
     *
     * Constraints:
     * - The value of x should be valid according to the boundaries of the board or storage component.
     * - Input validation is required to ensure x does not fall outside allowable ranges.
     */
    private int x;
    /**
     * Represents the y-coordinate related to the context of the action being performed.
     * This variable is used to provide additional positional information alongside the
     * x-coordinate. Together, these coordinates may define the location of a specific
     * resource, tile, or action within the player's board or game state.
     *
     * Constraints and key usage details:
     * - The y-coordinate may be used during validation or processing of game actions
     *   to establish spatial context or enforce location-based rules.
     * - Used in conjunction with the x-coordinate to identify a specific point
     *   on a grid, board, or other positional systems within the game.
     */
    private int y;
    /**
     * Constructs a GetGoodAction instance that represents the action of retrieving a good
     * from a specified position within a storage component, under the game context defined
     * by the player's board and specified coordinates.
     *
     * @param position The index position of the good to be retrieved from the storage.
     * @param playerBoard A reference to the PlayerBoard representing the player's current game state.
     * @param x The x-coordinate representing additional context or location for the action.
     * @param y The y-coordinate representing additional context or location for the action.
     */
    public GetGoodAction(int position, PlayerBoard playerBoard, int x, int y) {
        this.position = position;
        this.playerBoard = playerBoard;
        this.x = x;
        this.y = y;
    }

    /**
     * Processes the action of retrieving a good from the provided storage component,
     * ensuring it complies with the player's current state and game rules.
     * If the player's state does not permit the action, or if the good cannot be removed
     * due to invalid input, corresponding exceptions are thrown.
     *
     * @param storage the storage component from which a good will be retrieved
     * @param playerState the state of the player, used to validate whether the action is allowed
     * @throws IllegalStateException if the player's state does not allow the action
     * @throws InvalidInput if the position is invalid or no good exists at the specified index
     */
    @Override
    public void visit(Storage storage, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }

        good = storage.removeGood(position);
        if(good == null){
            throw new InvalidInput("Cannot remove a good because it is out of bounds");
        }
//        if (good!=null){
//            playerBoard.getStoredGoods().get(good.getValue()).remove(new IntegerPair(x,y));
//            if (playerBoard.getStoredGoods().get(good.getValue()).isEmpty()){
//                playerBoard.getStoredGoods().remove(good.getValue());
//            }
//        }
//        else {
//        }
    }

    /**
     * Retrieves the good associated with this action. The good is determined and set during
     * the {@code visit} method when the action is performed on a {@code Storage}. If no good
     * has been retrieved due to an error, the method may return {@code null}.
     *
     * @return the good retrieved from the storage, or {@code null} if no good was set
     */
    public Goods getGood() {
        return good;
    }


}
