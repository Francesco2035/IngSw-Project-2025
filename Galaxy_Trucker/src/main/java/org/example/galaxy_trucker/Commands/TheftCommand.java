package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.Actions.GetGoodAction;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a command for stealing cargo from a specific position and coordinate
 * within a game. This command modifies the state of the game by interacting
 * with the player's current card and applying the theft action.
 *
 * This class extends the base Command class and implements Serializable
 * to support serialization for distributing game state or saving progress.
 *
 * The TheftCommand is used to perform a specific action on a player's card,
 * allowing for cargo theft within the defined parameters.
 */
public class TheftCommand extends Command implements Serializable {

    @JsonProperty("position")
    int position;
    @JsonProperty("coordinate")
    IntegerPair coordinate;

    public TheftCommand(){}

    /**
     * Constructs a TheftCommand, representing a theft action in the game that
     * targets a specific position and coordinate (as an IntegerPair) to alter
     * the game's state by applying the theft logic.
     *
     * @param position   the position targeted by the theft action
     * @param coordinate the coordinate (as an IntegerPair) associated with the theft
     * @param gameId     the unique identifier of the game
     * @param playerId   the unique identifier of the player issuing the command
     * @param lv         the level associated with the command
     * @param title      the title or name of the command
     * @param token      the authentication or validation token for the command
     */
    public TheftCommand(int position, IntegerPair coordinate,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.position = position;
        this.coordinate = coordinate;
    }
    /**
     * Executes the current theft command for the provided player. This method modifies
     * the player's state by removing cargo from the specified position and coordinate
     * on their current card.
     * The method handles potential exceptions during the execution process.
     *
     * @param player the player object on which the command is executed
     * @throws IOException if an I/O error occurs during execution
     * @throws InterruptedException if the execution is interrupted
     */
    @Override
    public void execute(Player player) throws IOException, InterruptedException {
        try{
           player.getCurrentCard().loseCargo(coordinate,position);
//            PlayerBoard playerBoard = player.getmyPlayerBoard();
//            GetGoodAction action = new GetGoodAction(position,playerBoard,coordinate.getFirst(),coordinate.getSecond());
//            playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent()
//                    , action, player.getPlayerState());
        }
        catch (Exception e){

            System.out.println(e.getMessage());
        }

    }

    /**
     * Determines whether the current command is allowed in the given player state.
     * This method delegates the validation to the `allows` method of the provided
     * PlayerState instance, which evaluates if the command is permissible.
     *
     * @param playerState the current state of the player in the game, used to evaluate
     *                    if this command can be executed.
     * @return true if the command is allowed in the specified player state, false otherwise.
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
