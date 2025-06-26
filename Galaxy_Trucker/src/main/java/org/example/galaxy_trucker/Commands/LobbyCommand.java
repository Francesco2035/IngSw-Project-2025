package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;

/**
 * Represents a command used within the lobby system of the game. This command
 * extends the base {@link Command} class and provides functionality specific to
 * managing or interacting with lobby operations.
 *
 * The `LobbyCommand` is uniquely identified by its `commandType` property, which is
 * set to "LobbyCommand". It is designed to be serialized and deserialized using
 * Jackson annotations.
 *
 * A `ClientInterface` can be associated with this command to enable client-specific
 * communication.
 */
public class LobbyCommand extends Command implements Serializable {


    /**
     * Represents the type of the command as a string, which is used to identify the specific
     * type of the command being executed within the lobby system. This value is statically
     * set to "LobbyCommand" to denote that the command pertains to the lobby management
     * or interaction functionality.
     *
     * This property is intended for use with Jackson serialization and deserialization
     * mechanisms through the `@JsonProperty` annotation.
     */
    @JsonProperty("commandType")
    private final String commandType = "LobbyCommand";

    /**
     * The client associated with this command, implementing the {@link ClientInterface}.
     * This variable is utilized to enable communication with a remote client or system
     * and provides the necessary methods for interacting with the client-side operations,
     * such as receiving events, managing tokens, or handling connectivity.
     *
     * The client variable is annotated with {@code @JsonIgnore} to exclude it from
     * the serialization and deserialization process during JSON handling.
     */
    @JsonIgnore
    private ClientInterface client;


    /**
     * Constructs a `LobbyCommand` object with the specified title.
     * This command is used to represent actions or operations specific to a lobby
     * in the game system and extends the base `Command` class.
     *
     * @param title the title or name of the lobby command
     */
    @JsonCreator
    public LobbyCommand(@JsonProperty("title") String title) {
        super("-1","-1", -1, title, "",-1);
    }


    /**
     * Executes the command for the specified player within the game lobby context.
     * This method contains the logic to apply the command's effect or behavior
     * to the provided player object.
     *
     * @param player the player for whom the command is executed. This player object
     *               represents a client or participant in the game.
     * @throws IOException if an input or output error occurs during the execution
     *                     of the command.
     */
    @Override
    public void execute(Player player) throws IOException {}

    /**
     * Determines if this command is allowed to execute within the provided player state.
     *
     * @param playerState the current state of the player in which the command's allowance is checked
     * @return true if the command is allowed in the given player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    /**
     * Retrieves the client associated with this command.
     *
     * @return the client as an instance of {@code ClientInterface}, or {@code null} if no client is set.
     */
    @JsonIgnore
    @Override
    public ClientInterface getClient() {
        return client;
    }


    /**
     * Sets the client associated with this command.
     *
     * @param client the {@link ClientInterface} implementation to be associated with this command.
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }

}
