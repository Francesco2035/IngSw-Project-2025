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

    /**
     * Represents the type of command being executed in the game. This variable
     * is specific to the `FinishBuildingCommand` and is used to differentiate
     * and identify the command type within the game's command structure.
     *
     * The `commandType` is a predefined constant value indicating that the command
     * pertains to completing the construction of a building. This is primarily
     * utilized for serialization, deserialization, and logical identification
     * of the command type in the game's mechanics.
     *
     * This variable is annotated with `@JsonProperty` to ensure compatibility
     * with JSON data processes, allowing it to be recognized during
     * serialization and deserialization.
     */
    @JsonProperty("commandType")
    private final String commandType = "FinishBuildingCommand";

    /**
     * Represents the index indicating the specific building being finished.
     *
     * This variable is used to determine which building in the game is being targeted
     * by the finish building command. It is initialized to a default value of -1
     * to signify an uninitialized or invalid state until explicitly set during command
     * creation or operation.
     *
     * The `index` is serialized and deserialized using Jackson annotations,
     * facilitating its use in networked or persistence scenarios within the game.
     */
    @JsonProperty("index")
    private int index = -1;

    /**
     * Represents a command for finishing the building process in the game. This class is part
     * of the command system and is used to finalize the construction of a building for a player,
     * marking the building process as complete.
     *
     * This command interacts with the game's logic to update the player's state based on the
     * building completion and ensures that the construction process adheres to the game's rules.
     */
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
