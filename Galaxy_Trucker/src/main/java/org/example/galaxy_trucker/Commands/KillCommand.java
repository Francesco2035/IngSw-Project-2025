package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Cards.Card;
import org.example.galaxy_trucker.Model.IntegerPair;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The KillCommand class represents a game command that targets and eliminates
 * specific game elements located at certain coordinates. It extends the
 * {@link Command} class to leverage standard command behaviors and implements
 * Serializable to support serialization when needed.
 *
 * This class utilizes Jackson annotations to handle JSON serialization/deserialization
 * and supports initialization with a set of coordinates where the kill actions
 * should be applied.
 */
public class KillCommand extends Command implements Serializable {

    /**
     * Represents a list of coordinates in the form of IntegerPair objects, which
     * typically denote pairs of integers (e.g., x and y positions) relevant to the
     * game mechanics.
     *
     * This variable is serialized/deserialized using the "coordinates" JSON property
     * and is primarily used to define the target locations for specific commands
     * or actions in the game.
     */
    @JsonProperty("coordinates")
    ArrayList<IntegerPair> coordinates;

    /**
     * Default constructor for the KillCommand class.
     * Initializes a new instance without any predefined parameters.
     * This is primarily used for object creation in serialization/deserialization scenarios.
     */
    public KillCommand() {}

    /**
     * Constructs a KillCommand object with specified parameters.
     *
     * @param coordinates the list of IntegerPair objects representing the coordinates
     *                    where the kill actions should be applied
     * @param gameId      the unique identifier of the game
     * @param playerId    the unique identifier of the player initiating the command
     * @param lv          the level associated with the command
     * @param title       the title or name of the command
     * @param token       the token used for authentication or validation
     */
    public KillCommand(ArrayList<IntegerPair> coordinates,String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.coordinates = coordinates;
    }

    /**
     * Executes the kill command for the specified player. This method targets specific
     * game elements located at the coordinates associated with this command and performs
     * the defined action to eliminate those elements from the game board.
     *
     * @param player The player who initiated the command and will execute it.
     * @throws InterruptedException If the execution of the command is interrupted.
     */
    @Override
    public void execute(Player player) throws InterruptedException {
        player.getCurrentCard().killHumans(coordinates);
    }

    /**
     * Determines whether the current command is allowed in the given player state.
     * This method delegates the permission check to the provided {@link PlayerState} instance.
     *
     * @param playerState the current state of the player, used to determine if this command is allowed.
     * @return true if the command is allowed in the provided player state, false otherwise.
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}

