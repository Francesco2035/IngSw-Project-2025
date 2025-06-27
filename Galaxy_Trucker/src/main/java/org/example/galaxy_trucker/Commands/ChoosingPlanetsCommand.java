package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * Represents a command that allows a player to choose a planet during gameplay.
 * It extends the abstract Command class and implements Serializable, enabling it
 * to be transmitted and executed within the game system.
 */
public class ChoosingPlanetsCommand extends Command implements Serializable {

    /**
     * Represents the identifier of the planet to be chosen by a player during gameplay.
     * This variable is used within the context of commands that involve selecting a specific
     * planet as part of the game's mechanics.
     */
    @JsonProperty("planet")
    int planet;

    /**
     * Represents the type of the command as a constant value. This variable identifies
     * the command as a "ChoosingPlanetsCommand" and is used to differentiate it from
     * other command types within the game system.
     *
     * The commandType is serialized and deserialized using the Jackson annotation
     * `@JsonProperty` to facilitate communication and data persistence in a structured
     * format.
     */
    @JsonProperty("commandType")
    private final String commandType = "ChoosingPlanetsCommand";

    /**
     * Represents a command that allows a player to choose a planet during gameplay.
     * This class facilitates the selection of a planet by the player and ensures that
     * the appropriate game logic is executed. It extends the abstract Command class
     * and is part of the game's command execution system.
     */
    public ChoosingPlanetsCommand() {}

    /**
     * Constructs a ChoosingPlanetsCommand instance.
     *
     * @param planet the identifier of the planet to be chosen by the player
     * @param gameId the unique identifier for the game
     * @param playerId the unique identifier for the player issuing the command
     * @param lv the level of the player within the game
     * @param title the title or name associated with the command or action
     * @param token the authentication token associated with the command or player
     */
    public ChoosingPlanetsCommand(int planet , String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.planet = planet;
    }

    /**
     * Executes the command for the specified player, allowing the player to choose a planet.
     *
     * @param player the player executing the command; the player's current card will be affected
     */
    @Override
    public void execute(Player player) {

        player.getCurrentCard().choosePlanet(planet);

    }
    /**
     * Determines whether the current command is allowed in the specified player state.
     *
     * @param playerState the state of the player in which this command's allowance is being checked
     * @return true if the command is allowed in the given player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }
}
