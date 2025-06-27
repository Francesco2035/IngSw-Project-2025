package org.example.galaxy_trucker.Model.Boards.Actions;

import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Goods.Goods;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;
import org.example.galaxy_trucker.Model.Tiles.Storage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents an action to add a specified good to storage or a player board.
 * It extends the ComponentAction class and encapsulates a Goods instance, a PlayerBoard,
 * and the coordinates where the good should be placed.
 */
public class AddGoodAction extends ComponentAction {

    /**
     * Represents a specific type of goods to be added during the action.
     * The variable holds a reference to a Goods instance, which is an interface
     * implemented by various goods types such as BLUE, GREEN, YELLOW, and RED.
     *
     * The `good` variable is used to track the specific good being placed into
     * storage or onto the player's board at specified coordinates. The value
     * encapsulates the type and properties of the good.
     *
     * This variable is utilized in conjunction with the game logic to ensure that the
     * placement of goods adheres to the rules and constraints of the game.
     */
    private Goods good;
    /**
     * Represents the player board to which a good can be added.
     * It holds the current state of the player's board and is used
     * to store or manage goods during the execution of a game action.
     */
    private PlayerBoard playerBoard;
    /**
     * Represents the x-coordinate where a goods item will be placed on the player's board
     * as part of an add or get goods action.
     *
     * This variable is used to specify one of the grid positions on the storage or
     * player board where the action will be performed. The value must conform to
     * valid board coordinates as governed by the game's rules.
     */
    int x;
    /**
     * Represents the y-coordinate where a good should be placed on the player's board
     * or within the storage. This variable is used in the context of an action that
     * involves placing or interacting with a good at a specified position.
     *
     * Constraints and Usage:
     * - Must correspond to a valid position on the player's board or in storage.
     * - Used alongside the x variable to define a two-dimensional coordinate.
     */
    int y;
    /**
     * Constructs an AddGoodAction instance, representing the action to add a specified good
     * to a given PlayerBoard at specified coordinates.
     *
     * @param good the good to be added, represented by an implementation of the Goods interface
     * @param playerBoard the player board to which the good will be added
     * @param x the x-coordinate where the good will be placed on the player board
     * @param y the y-coordinate where the good will be placed on the player board
     */
    public AddGoodAction(Goods good,PlayerBoard playerBoard, int x, int y) {
        this.good = good;
        this.playerBoard = playerBoard;
        this.x = x;
        this.y = y;
    }

    /**
     * Visits the given storage and modifies it by adding a good.
     * The operation is performed only if the player's current state allows this action.
     *
     * @param storage the storage where the good is to be added
     * @param playerState the current state of the player that determines if the action is allowed
     * @throws IllegalStateException if the player's state does not permit this action
     */
    @Override
    public void visit(Storage storage, PlayerState playerState) {
        if (!playerState.allows(this)){
            throw new IllegalStateException("You are not allowed to perform this action in this state");
        }
        storage.addGood(good);
//        if(good==null){
//            return;
//        }
//        HashMap<Integer, ArrayList<IntegerPair>> storedgoods = playerBoard.getStoredGoods();
//        if (storedgoods.containsKey(good.getValue())){
//            storedgoods.get(good.getValue()).add(new IntegerPair(x, y));
//        }
//        else {
//            storedgoods.put(good.getValue(),new ArrayList<>());
//            storedgoods.get(good.getValue()).add(new IntegerPair(x, y));
//        }

    }

}
