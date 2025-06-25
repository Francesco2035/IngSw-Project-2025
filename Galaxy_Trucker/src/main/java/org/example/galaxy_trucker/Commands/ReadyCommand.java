package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.Exceptions.InvalidInput;
import org.example.galaxy_trucker.Model.JsonHelper;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.Serializable;

/**
 * Represents the ReadyCommand used to handle player readiness in the game system.
 * This command allows a player to set their "ready" state to true or false, depending
 * on the gameplay requirements. It extends the base {@code Command} class and provides
 * behavior specific to readiness within the game.
 *
 * The {@code ReadyCommand} features:
 * - A predefined `commandType` identifying it as a "ReadyCommand".
 * - A boolean `ready` field determining the readiness state of the player.
 * - The ability to be executed within a player's context based on specific game logic.
 *
 * This command is associated with titles like "Ready" to toggle the player's readiness
 * and "Quit" to handle situations where the player chooses to leave the game.
 * It verifies valid commands based on input and triggers appropriate actions
 * according to the given `title`.
 */
public class ReadyCommand extends Command implements Serializable {


    @JsonProperty("commandType")
    private final String commandType = "ReadyCommand";

    @JsonProperty("ready")
    boolean ready;

    /**
     * Constructs a ReadyCommand object with specified parameters to manage the readiness state
     * of a player within the game.
     *
     * @param gameId   the unique identifier of the game
     * @param playerId the unique identifier of the player
     * @param lv       the level associated with the command or player
     * @param title    the title or name indicating the command type (e.g., "Ready", "Quit")
     * @param ready    a boolean value indicating the readiness state of the player (true if ready, false otherwise)
     * @param token    the token used for authentication or session validation
     */
    public ReadyCommand(String gameId, String playerId, int lv, String title, boolean ready, String token) {
        super(gameId, playerId, lv, title, token, -1);
        this.ready = ready;
    }

    /**
     * Executes the command logic based on the title and interacts with the given player.
     * The behavior differs based on the title:
     * - For "Quit", the player quits the game.
     * - For "Ready", the readiness state of the player is updated.
     * If the title is invalid, an exception is thrown.
     *
     * @param player The player object on which the command is executed.
     *               It should be a valid instance representing the current player.
     *               The method interacts with this object to perform specific actions.
     * @throws InvalidInput If the title does not match any valid commands.
     */
    @Override
    public void execute(Player player) {
        System.out.print("execute.... ");
        switch (title){
            case "Quit": {
             //player.getCommonBoard().abandonRace(player);
             System.out.println(player.GetID() + " quit");
             break;
            }
            case "Ready": {
                player.SetReady(ready);
                System.out.println("Ready state set to " + ready);
                break;
            }
            default: {
                throw new InvalidInput("invalid Title");
            }

        }
    }

    /**
     * Default constructor for the ReadyCommand class.
     * Initializes a ReadyCommand object with default or uninitialized properties.
     * Primarily used for deserialization or creating a command instance without specifying initial values.
     */
    public ReadyCommand() {}

    /**
     * Determines if this command is allowed in the given player state.
     *
     * @param playerState the current state of the player used to decide if the command can be executed
     * @return true if the command is allowed in the given player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

}
