package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Player;

import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a command used to facilitate reconnection of a player to a game session.
 * This command is utilized in scenarios where a player needs to re-establish their
 * connection to continue participating in the game.
 *
 * The `ReconnectCommand` class extends the base `Command` class, inheriting properties
 * such as game ID, player ID, level, title, and token while adding specific behavior
 * related to reconnection functionality.
 *
 * This class includes functionality for setting and retrieving a `ClientInterface`
 * instance, which facilitates interaction with the client-side system.
 *
 * The `commandType` property uniquely identifies this command type during
 * serialization and deserialization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReconnectCommand extends Command implements Serializable {
    @JsonProperty("commandType")
    private final String commandType = "ReconnectCommand";

    @JsonIgnore
    private ClientInterface client;


    public ReconnectCommand() {}


    /**
     * Constructs a new ReconnectCommand with the specified parameters.
     *
     * @param token    the token used for authentication or validation during reconnection
     * @param gameId   the unique identifier of the game to reconnect to
     * @param playerId the unique identifier of the player attempting to reconnect
     * @param lv       the level associated with the player's session or progress
     * @param title    the title or name representing the player's current game state
     */
    public ReconnectCommand(String token, String gameId, String playerId, int lv, String title) {
        super(gameId, playerId, lv, title, token,-1);
    }

    /**
     * Executes the logic associated with this command. This method is intended to
     * perform the specific behavior defined for the concrete implementation of the command.
     *
     * The `execute` method in this context may be a placeholder or a convenience
     * method for invoking command-specific functionality that does not operate on
     * a specific `Player` instance.
     *
     * Note: For commands that require interaction with the `Player` object, an
     * overloaded `execute` method should be used.
     */
    public void execute() {}



    /**
     * Executes the reconnection command for the specified player, facilitating
     * the re-establishment of the player's connection to the game session.
     *
     * @param player the player attempting to reconnect to the game session
     * @throws IOException if an I/O error occurs during the reconnection process
     */
    @Override
    public void execute(Player player) throws IOException {

    }

    /**
     * Retrieves the client associated with this command.
     *
     * @return the client instance implementing the ClientInterface associated with this command.
     */
    @Override
    public ClientInterface getClient() {
        return client;
    }


    /**
     * Sets the client instance to interact with the client-side system.
     *
     * @param client the {@code ClientInterface} instance to be associated with this command
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }


}
