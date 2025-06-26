package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Boards.Actions.GetPlasmaDrillPower;
import org.example.galaxy_trucker.Model.Boards.PlayerBoard;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a specific command in the game, used to perform an attack action
 * with optional coordinates. This command operates on the player's board,
 * allowing interaction with plasma drills based on provided tile coordinates.
 *
 * Extends the {@link Command} class and provides implementations for the
 * `execute` and `allowedIn` methods to handle game-specific logic.
 */
public class GiveAttackCommand extends Command implements Serializable {

    /**
     * Represents the list of coordinates used in the context of an attack command within the game.
     * Each coordinate is an instance of the IntegerPair class, which contains two integer values
     * representing a specific position on the player's board. This field can be utilized for
     * targeting specific areas during the execution of an attack command.
     *
     * The list of coordinates can be null, indicating that the attack command does not target
     * specific positions, or it can contain one or more IntegerPair objects to define the targeted
     * locations.
     */
    @JsonProperty("coordinates")
    private ArrayList<IntegerPair> coordinates;

    /**
     * Represents a command in the game to execute an attack action.
     * The GiveAttackCommand can be used to target specific locations
     * on a player's board or perform a general attack without specific coordinates.
     *
     * This class is part of the game's command system and extends the
     * functionality of the base Command class.
     */
    public GiveAttackCommand(){}

    /**
     * Constructs a GiveAttackCommand object with specific parameters to execute
     * an attack command in the game. The attack can target specific coordinates
     * on the player's board or be a generic attack without defined coordinates.
     *
     * @param coordinates the list of IntegerPair objects representing the coordinates
     *                    on the player's board where the attack will be executed; can be null
     * @param gameId      the unique identifier of the game this command belongs to
     * @param playerId    the unique identifier of the player issuing the command
     * @param lv          the level associated with the command
     * @param title       the title or description of the command
     * @param token       the token used for authentication or validation of the command
     */
    public GiveAttackCommand(ArrayList<IntegerPair> coordinates,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.coordinates = coordinates;

    }

    /**
     * Executes the command for the specified player. This method interacts with the player's board
     * and performs actions based on the tiles identified by the provided coordinates, or defaults
     * to checking the player's plasma drill power if coordinates are absent. It verifies the
     * player's current card's power requirements against the calculated power from the action.
     *
     * @param player The player executing the command. The player's board and state are used for
     *               performing actions and validating the power requirements.
     */
    @Override
    public void execute(Player player) {
        if (coordinates!= null){
            PlayerBoard playerBoard = player.getmyPlayerBoard();
            GetPlasmaDrillPower action = new GetPlasmaDrillPower(playerBoard.getPlasmaDrillsPower());
            for (IntegerPair coordinate : coordinates) {
                playerBoard.performAction(playerBoard.getTile(coordinate.getFirst(), coordinate.getSecond()).getComponent(),
                        action ,player.getPlayerState());
            }

            try {
                player.getCurrentCard().checkPower(action.getPower() ,action.getCountDoublePlasmaDrills());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            PlayerBoard playerBoard = player.getmyPlayerBoard();
            GetPlasmaDrillPower action = new GetPlasmaDrillPower(playerBoard.getPlasmaDrillsPower());
            try {
                player.getCurrentCard().checkPower(action.getPower() ,action.getCountDoublePlasmaDrills());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Determines if the current command is allowed in the specified player state.
     *
     * @param playerState the current state of the player, which is used to determine
     *                    whether this command is allowed to execute.
     * @return true if the command is permitted in the given player state, false otherwise.
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}
