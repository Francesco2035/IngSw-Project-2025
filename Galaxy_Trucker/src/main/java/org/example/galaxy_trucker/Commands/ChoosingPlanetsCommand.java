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

    @JsonProperty("planet")
    int planet;

    @JsonProperty("commandType")
    private final String commandType = "ChoosingPlanetsCommand";

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
