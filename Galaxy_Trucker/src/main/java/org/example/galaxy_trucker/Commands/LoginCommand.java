package org.example.galaxy_trucker.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.galaxy_trucker.ClientServer.RMI.ClientInterface;
import org.example.galaxy_trucker.Model.Player;
import org.example.galaxy_trucker.Model.PlayerStates.PlayerState;

import java.io.IOException;
import java.io.Serializable;



/**
 * LoginCommand is a specific implementation of the abstract Command class,
 * representing the action of logging into a game. This command carries
 * the necessary information to identify the client initiating the login process
 * and includes game-related metadata inherited from the Command superclass.
 *
 * This class is primarily used to handle login-related operations within the game system.
 * It enforces state restrictions and interacts with the Player and PlayerState classes
 * to ensure the command is processed appropriately.
 */
public class LoginCommand extends Command implements Serializable {

    /**
     * Represents the type of the command associated with the current instance.
     *
     * This variable holds the command type identifier as a string, which in this case,
     * is initialized to "LoginCommand". It is used to specify the nature of the command
     * being executed, aiding in distinguishing different commands within the system.
     *
     * The `commandType` variable is immutable, ensuring that its value remains constant
     * once initialized. This property may be utilized during serialization or internal
     * operations where the command type is required for processing or validation.
     */
    @JsonProperty("commandType")
    private final String commandType = "LoginCommand";

    /**
     * Represents the client instance associated with the LoginCommand.
     * This variable holds a reference to a client implementing the
     * ClientInterface, which is used to facilitate communication and
     * interactions between the server and the client.
     *
     * The ClientInterface defines the required operations for the client side,
     * including establishing connections, handling remote events, managing
     * session tokens, and monitoring connectivity through pings.
     *
     * This field utilizes the @JsonIgnore annotation to exclude it from
     * serialization and deserialization processes, ensuring that the client
     * instance remains transient and is not included in JSON representations
     * of the LoginCommand.
     */
    @JsonIgnore
    private ClientInterface client;


    /**
     * Constructs a LoginCommand object with specified parameters.
     *
     * @param gameId     the unique identifier of the game
     * @param playerId   the unique identifier of the player
     * @param lv         the level associated with the command
     * @param title      the title or name of the command
     * @param maxPlayers the maximum number of players allowed
     */
    public LoginCommand(String gameId, String playerId, int lv, String title, int maxPlayers) {
        super(gameId, playerId, lv, title, "", maxPlayers);
    }


    /**
     * Default constructor for the LoginCommand class.
     * Initializes a new instance of LoginCommand with no parameters.
     *
     * LoginCommand is used to facilitate player login operations
     * within the game system. This constructor creates a blank command
     * instance that requires properties to be set before execution.
     */
    public LoginCommand(){

    }

    /**
     * Executes the login command for a specified player.
     * This method processes the login operation by interacting with the provided player
     * instance and updating its state as required for login functionality.
     *
     * @param player the instance of Player on which the login command is executed
     * @throws IOException if an I/O error occurs during the login process
     */
    @Override
    public void execute(Player player) throws IOException {

    }

    /**
     * Determines if this command is allowed in the provided player state.
     *
     * @param playerState the current state of the player
     * @return true if the command is allowed in the given player state, false otherwise
     */
    @Override
    public boolean allowedIn(PlayerState playerState) {
        return playerState.allows(this);
    }

    /**
     * Retrieves the client associated with the command.
     *
     * @return the client implementing the ClientInterface associated with this command
     */
    @Override
    public ClientInterface getClient() {
        return client;
    }


    /**
     * Sets the client instance for this command.
     * The client is used to facilitate communication between the server and the client.
     *
     * @param client the client instance implementing the ClientInterface
     *               to be associated with this command
     */
    public void setClient(ClientInterface client) {
        this.client = client;
    }


}
