package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

/**
 * The FinishBuildingCommand class represents a specific type of game command that
 * allows a player to finalize the construction of a building. This command is part
 * of the broader command framework in the game, designed for handling player actions.
 *
 * It extends the Command class, inheriting its properties and behavior while implementing
 * additional logic specific to the completion of building processes.
 */
public class FinishBuildingCommand extends Command {

    @JsonProperty("commandType")
    private final String commandType = "FinishBuildingCommand";

    @JsonProperty("index")
    private int index = -1;

    public FinishBuildingCommand() {}

    /**
     * Constructs a FinishBuildingCommand with specified parameters.
     *
     * @param index    the index indicating the specific building being finished
     * @param gameId   the unique identifier of the game
     * @param playerId the unique identifier of the player issuing the command
     * @param lv       the level associated with the command
     * @param title    the title or name of the command
     * @param token    the authentication or validation token for the command
     */
    public FinishBuildingCommand(int index, String gameId, String playerId, int lv, String title, String token) {
        super(gameId, playerId, lv, title, token,-1);
        this.index = index;
    }

    /**
     * Executes the finish building command for the specified player. This method finalizes
     * the construction process for a player based on the current game logic, checks the
     * player's construction level, and marks the player as ready. If any invalid operation
     * occurs during execution, an appropriate exception is thrown.
     *
     * @param player the player on whom the command is being executed. The player object
     *               contains the current game state and construction information.
     *               The execution modifies the player's readiness and handles their
     *               building completion.
     *
     * @throws InvalidInput if an invalid state or argument is encountered during execution.
     */
    @Override
    public void execute(Player player){
        try {
            if (player.getCommonBoard().getLevel() == 1)
                player.EndConstruction();
            else
                player.EndConstruction(index);

            player.SetReady(true);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new InvalidInput(e.getMessage());
        }
    }

    /**
     * Determines whether the current command is allowed in the specified player state.
     *
     * @param playerState the current state of the player in which the command is being evaluated
     * @return true if the command is permitted in the given player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}
