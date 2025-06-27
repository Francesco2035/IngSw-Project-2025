package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.Actions.GetEnginePower;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a command that grants engine power to components on a player's game board.
 *
 * The `GiveSpeedCommand` processes a list of coordinates representing game tiles and applies
 * engine power to those tiles using a specific action. If no coordinates are provided, a generic
 * engine power action is triggered for the player. This command validates the movement rules
 * based on the power granted and ensures the action adheres to the player's current state.
 *
 * This command extends the abstract `Command` class, inheriting shared properties and behaviors
 * while defining specific logic for handling engine power distribution among game components.
 */
public class GiveSpeedCommand extends Command implements Serializable {

    /**
     * Represents a list of coordinate pairs used to target specific components or positions
     * on a player's game board. Each coordinate pair consists of two integers: row and column.
     *
     * This field is annotated for JSON serialization and deserialization with the key "coordinates".
     */
    @JsonProperty("coordinates")
    private ArrayList<IntegerPair> coordinates;

    /**
     * Default constructor for the GiveSpeedCommand class.
     * Initializes an instance of the command without specifying any parameters.
     * This command is used to provide engine power to specific components on a player's
     * game board or to perform a generic engine power action.
     */
    public GiveSpeedCommand() {}

    /**
     * Constructs a command that grants engine power to specific components on the player's game board
     * or triggers a generic engine power action if no coordinates are provided.
     *
     * @param coordinates a list of coordinate pairs (row and column) representing the target components on the player's game board
     * @param gameId      the unique identifier of the game
     * @param playerId    the unique identifier of the player executing the command
     * @param lv          the level or priority of the command
     * @param title       the title or name of the command
     * @param token       the authentication or validation token for executing the command
     */
    public GiveSpeedCommand( ArrayList<IntegerPair> coordinates,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.coordinates = coordinates;

    }

    /**
     * Executes the command for the given player, applying engine power to the specified game components
     * or performing a default action if no specific coordinates are provided.
     *
     * @param player the player for whom the command is being executed. The method processes the player's
     *               game board and performs actions based on the player's current state and provided coordinates.
     */
    @Override
    public void execute(Player player) {
        if (coordinates!= null){
            PlayerBoard playerBoard = player.getmyPlayerBoard();
            //action si salva la potenza singola
            GetEnginePower action = new GetEnginePower(playerBoard.getEnginePower());
            for (IntegerPair coordinate : coordinates) {
                playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),
                        action ,player.getPlayerState());
            }

            try {
                player.getCurrentCard().checkMovement(action.getPower(),action.getCountDoubleEngine());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else{

            PlayerBoard playerBoard = player.getmyPlayerBoard();
            GetEnginePower action = new GetEnginePower(playerBoard.getEnginePower());
            try {
                player.getCurrentCard().checkMovement(action.getPower(),action.getCountDoubleEngine());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // eroorino sksk
    }

    /**
     * Checks if the current command is allowed in the given player state.
     *
     * @param playerState The state of the player in which the command's validity will be checked.
     * @return {@code true} if the command is allowed in the given player state, otherwise {@code false}.
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
